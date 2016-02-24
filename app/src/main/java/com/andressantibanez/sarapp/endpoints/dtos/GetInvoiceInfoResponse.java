package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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

    public List<InvoiceDetail> details;

    public List<String> errors;

    public GetInvoiceInfoResponse() {
        details = new ArrayList<>();
        errors = new ArrayList<>();
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
