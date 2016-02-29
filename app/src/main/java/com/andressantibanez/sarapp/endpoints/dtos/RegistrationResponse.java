package com.andressantibanez.sarapp.endpoints.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrés on 2/29/16.
 */
public class RegistrationResponse {

    public String token;
    public List<String> errors;

    public RegistrationResponse() {
        token = "";
        errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

}
