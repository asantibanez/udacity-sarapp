package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asantibanez on 2/24/16.
 */
public class GetInvoiceInfoResponse {

    public String id;

    @SerializedName("supplier_commercial_name")
    public String supplierName;

    @SerializedName("issuing_date")
    public String issuingDate;

    @SerializedName("supplier_taxpayer_id")
    public String supplierTaxpayerId;

    public double subtotal;
    public double tax;
    public double total;

    public List<InvoiceDetail> details;

    public List<String> errors;

    public GetInvoiceInfoResponse() {
        details = new ArrayList<>();
        errors = new ArrayList<>();
    }

    public Date issuingDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(issuingDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public boolean hasDetails() {
        return details.size() > 0;
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public static class InvoiceDetail {
        public String id;

        @SerializedName("invoice_id")
        public String invoiceId;

        @SerializedName("product_id")
        public String productId;

        @SerializedName("product_name")
        public String productName;

        public double quantity;
        public double price;
        public double discount;
        public double subtotal;

        @SerializedName("expense_type")
        public String expenseType;
    }
}
