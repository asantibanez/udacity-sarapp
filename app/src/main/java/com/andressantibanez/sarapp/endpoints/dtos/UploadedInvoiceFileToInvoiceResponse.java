package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asantibanez on 2/27/16.
 */
public class UploadedInvoiceFileToInvoiceResponse {

    @SerializedName("invoice_id")
    public String invoiceId;

    public List<String> errors;

    public UploadedInvoiceFileToInvoiceResponse() {
        errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

}
