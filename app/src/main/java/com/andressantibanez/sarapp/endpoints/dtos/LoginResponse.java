package com.andressantibanez.sarapp.endpoints.dtos;

import java.util.ArrayList;

/**
 * Created by asantibanez on 2/21/16.
 */
public class LoginResponse {

    public String token;
    public ArrayList<String> errors;

    public LoginResponse() {
        token = "";
        errors = new ArrayList<>();
    }

}
