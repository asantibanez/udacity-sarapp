package com.andressantibanez.sarapp.endpoints.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrés on 2/26/16.
 */
public class ExpenseSummaryResponse {

    public double total;

    @SerializedName("expense_types")
    public ExpenseTypes expenseTypes;

    public List<String> errors;

    public ExpenseSummaryResponse() {
        errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public static class ExpenseTypes {

        @SerializedName("NONE")
        public ExpenseType none;

        @SerializedName("FEEDING")
        public ExpenseType feeding;

        @SerializedName("HEALTH")
        public ExpenseType health;

        @SerializedName("CLOTHING")
        public ExpenseType clothing;

        @SerializedName("DWELLING")
        public ExpenseType dwelling;

        @SerializedName("EDUCATION")
        public ExpenseType education;

        @SerializedName("OTHER")
        public ExpenseType other;

        public static class ExpenseType {
            public String name;
            public double total;

            @SerializedName("in_invoices")
            public double inInvoices;

            @SerializedName("in_additional_expenses")
            public double inAdditionalExpenses;

            public double limit;
            public double compliance;
        }

    }

}
