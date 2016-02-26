package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asantibanez on 2/25/16.
 */
public class UpdateInvoiceDetailExpenseTypeRequest {

    @SerializedName("expense_type")
    public String expenseType;

    public UpdateInvoiceDetailExpenseTypeRequest(String expenseType) {
        this.expenseType = expenseType;
    }
}
