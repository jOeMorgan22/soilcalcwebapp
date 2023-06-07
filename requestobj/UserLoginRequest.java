package com.soilwebapp.requestobj;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserLoginRequest {

    @NotBlank(message = "Username can not be blank")
    @NotEmpty(message = "Username can not be empty")
    @NotNull(message = "Username can not be null")
    private String username;

    @NotBlank(message = "Password can not be blank")
    @NotEmpty(message = "Password can not be empty")
    @NotNull(message = "Password can not be null")
    private String password;

    public UserLoginRequest(){}

    public UserLoginRequest(String username, String password){
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
