package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asantibanez on 2/25/16.
 */
public class UpdateDetailExpenseTypeRequest {

    @SerializedName("expense_type")
    public String expenseType;

    public UpdateDetailExpenseTypeRequest(String expenseType) {
        this.expenseType = expenseType;
    }
}
