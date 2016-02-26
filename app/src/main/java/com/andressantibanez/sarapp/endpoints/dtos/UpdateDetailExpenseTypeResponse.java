package com.andressantibanez.sarapp.endpoints.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asantibanez on 2/25/16.
 */
public class UpdateDetailExpenseTypeResponse {

    public String id;
    public List<String> errors;

    public UpdateDetailExpenseTypeResponse() {
        errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

}
