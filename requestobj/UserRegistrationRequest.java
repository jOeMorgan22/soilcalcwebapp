package com.soilwebapp.requestobj;

import com.soilwebapp.validation.Password;
import com.soilwebapp.validation.Username;

public class UserRegistrationRequest {

    @Username
    private String username;

    @Password
    private String password;

    public UserRegistrationRequest(){}

    public UserRegistrationRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

}
