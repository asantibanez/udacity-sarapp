package com.andressantibanez.sarapp.database.invoicedetails;

import android.net.Uri;
import android.provider.BaseColumns;

import com.andressantibanez.sarapp.database.SarappProvider;

/**
 * Created by asantibanez on 2/16/16.
 */
public class InvoiceDetailsContract {

    public static final String TABLE_NAME = "invoice_details";

    public static final Uri CONTENT_URI = Uri.parse(SarappProvider.BASE_CONTENT_URI + "/" + TABLE_NAME);

    public static class Columns implements BaseColumns {
        public static final String ID = "id";
        public static final String INVOICE_ID = "invoice_id";
        public static final String PRODUCT_ID = "product_id";
        public static final String PRODUCT_NAME = "product_name";
        public static final String QUANTITY = "quantity";
        public static final String PRICE = "price";
        public static final String DISCOUNT = "discount";
        public static final String SUBTOTAL = "subtotal";
        public static final String EXPENSE_TYPE = "expense_type";
    }

    public static class OrderBy {
    }

}
