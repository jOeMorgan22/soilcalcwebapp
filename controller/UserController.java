package com.soilwebapp.controller;

import com.soilwebapp.requestobj.UserLoginRequest;
import com.soilwebapp.requestobj.UserRegistrationRequest;
import com.soilwebapp.service.AppUserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class UserController {

    private final AppUserService userService;

    public UserController(AppUserService userService){
        this.userService = userService;
    }

    @RequestMapping("/register")
    public String registerNewUser(Model model){
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        model.addAttribute("userRegistrationRequest", userRegistrationRequest);
        return "registration-form";
    }

    @RequestMapping("/process-registration")
    public String processRegistration(@Valid @ModelAttribute("userRegistrationRequest") UserRegistrationRequest userRegistrationRequest,
                                      BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "registration-form";
        }
        try{
            userService.registerNewUser(userRegistrationRequest);
        } catch (IOException | InterruptedException e) {
            return "server-registration-form";
        } catch (DataIntegrityViolationException e){
            return "registration-error";
        }
        return "registration-confirmation";
    }

    @RequestMapping("/login")
    public String userLogin(Model model){
        UserLoginRequest userloginRequest = new UserLoginRequest();
        model.addAttribute("userLoginRequest", userloginRequest);
        return "login-form";
    }

    @RequestMapping("/process-login")
    public String processLogin(@Valid @ModelAttribute("userLoginRequest") UserLoginRequest userloginRequest,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "login-form";
        }
        try{
            userService.loginUser(userloginRequest);
        } catch (AuthenticationException e) {
            return "login-error";
        } catch (IOException | InterruptedException e) {
            return "server-login-error";
        }
        return "helloworld";
    }

    @RequestMapping("/delete-account")
    public String deleteAccount(@ModelAttribute("userRequest") UserLoginRequest userloginRequest){
        try{
            userService.deleteUser(userloginRequest.getUsername());
        } catch (IOException | InterruptedException e) {
            return "server-error";
        }
        return "account-successfully-deleted";
    }
}
