package com.soilwebapp.service;

import com.google.gson.Gson;
import com.soilwebapp.cache.BasicCache;
import com.soilwebapp.requestobj.UserLoginRequest;
import com.soilwebapp.requestobj.UserRegistrationRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Optional;

@Service
public class AppUserService {

    public static final String REGISTRATION_URL = "http://localhost:6060/register";

    public static final String LOGIN_URL = "http://localhost:6060/login";

    public static final String DELETE_ACCOUNT_URL = "http://localhost:6060/delete-account";


    public String registerNewUser(UserRegistrationRequest userRegistrationRequest) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpRequest request = HttpRequest.newBuilder()
            .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userRegistrationRequest)))
                .uri(URI.create(REGISTRATION_URL))
                .build();
        HttpResponse<String> response = HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 404){
            return "fuck shit";
        }
        if(response.statusCode() == 409){
            throw new DataIntegrityViolationException("Username is taken");
        }
        return response.body();
    }

    public String getAuthenticationHeader(UserLoginRequest userLoginRequest) {
        String valueToEncode = userLoginRequest.getUsername() + ":" + userLoginRequest.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    public String loginUser(UserLoginRequest userLoginRequest) throws IOException, InterruptedException,
            AuthenticationException {
        Gson gson = new Gson();
        HttpRequest loginRequest = HttpRequest.newBuilder()
                //.header("Content-Type", "application/json")
                .header("Authorization", getAuthenticationHeader(userLoginRequest))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userLoginRequest)))
                .uri(URI.create(LOGIN_URL))
                .build();
        HttpResponse<String> response = HttpClient
                .newHttpClient()
                .send(loginRequest, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 401){
            throw new AuthenticationException("Invalid user credentials");
        }
        if(response.statusCode() == 404 | response.statusCode() == 500){
            throw new IOException("Something went wrong on our end. Please try again.");
        }
        Optional<String> jwt = response.headers().firstValue("Authorization");
        if(jwt.isEmpty()){
            throw new AuthenticationException("Invalid user credentials");
        }

        BasicCache.CACHED_AUTH.put(userLoginRequest.getUsername(), jwt.get());
        return response.body();
    }

    public boolean deleteUser(String username) throws IOException, InterruptedException {
        String jwt = BasicCache.CACHED_AUTH.get(username);
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(DELETE_ACCOUNT_URL))
                .header("Authorization", jwt)
                .build();
        HttpResponse<String> httpResponse = HttpClient
                .newHttpClient()
                .send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        if(httpResponse.statusCode() == 404 | httpResponse.statusCode() == 500){
            throw new IOException("Something went wrong on our end. Please try again.");
        }
        if(httpResponse.statusCode() == 204){
            BasicCache.CACHED_AUTH.remove(username, BasicCache.CACHED_AUTH.get(username));
            return true;
        }
        return false;
    }

}
