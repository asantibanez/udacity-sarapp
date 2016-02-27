package com.andressantibanez.sarapp.endpoints.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asantibanez on 2/27/16.
 */
public class InvoiceUploadResponse {

    public String id;
    public List<String> errors;

    public InvoiceUploadResponse() {
        errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
}
