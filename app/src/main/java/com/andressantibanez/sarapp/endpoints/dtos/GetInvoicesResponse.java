package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

import net.danlew.android.joda.JodaTimeAndroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by asantibanez on 2/21/16.
 */
public class GetInvoicesResponse {

    @SerializedName("data")
    public ArrayList<Invoice> invoices;
    public ArrayList<String> errors;

    public GetInvoicesResponse() {
        invoices = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public static class Invoice {
        public String id;

        @SerializedName("supplier_commercial_name")
        public String supplierName;

        @SerializedName("supplier_taxpayer_id")
        public String supplierTaxpayerId;

        @SerializedName("issuing_date")
        public String issuingDate;

        public double subtotal;
        public double discount;
        public double tax;
        public double total;

        public Date issuingDate() {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(issuingDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }
    }
}
