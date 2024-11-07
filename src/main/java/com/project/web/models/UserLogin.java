package com.project.web.models;

public class UserLogin
{
    @lombok.Getter
    private String username;
    private String password;

    public UserLogin()
    {
    }

    public UserLogin(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

}
