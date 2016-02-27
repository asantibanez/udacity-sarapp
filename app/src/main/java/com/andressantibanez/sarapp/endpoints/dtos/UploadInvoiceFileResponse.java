package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asantibanez on 2/27/16.
 */
public class UploadInvoiceFileResponse {

    @SerializedName("id")
    public String electronicInvoiceId;

    public List<String> errors;

    public UploadInvoiceFileResponse() {
        errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
}
