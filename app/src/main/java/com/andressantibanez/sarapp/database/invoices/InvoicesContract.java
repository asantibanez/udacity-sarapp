package com.andressantibanez.sarapp.database.invoices;

import android.net.Uri;
import android.provider.BaseColumns;

import com.andressantibanez.sarapp.database.SarappProvider;

/**
 * Created by asantibanez on 2/16/16.
 */
public class InvoicesContract {

    public static final String TABLE_NAME = "invoices";

    public static final Uri CONTENT_URI = Uri.parse(SarappProvider.BASE_CONTENT_URI + "/" + TABLE_NAME);

    public static class Columns implements BaseColumns {
        public static final String ID = "id";
        public static final String SUPPLIER_TAXPAYER_ID = "supplier_taxpayer_id";
        public static final String SUPPLIER_NAME = "supplier_name";
        public static final String ISSUING_DATE = "issuing_date";
        public static final String SUBTOTAL = "subtotal";
        public static final String TAX = "tax";
        public static final String TOTAL = "total";
    }

    public static class OrderBy {
        public static final String ISSUING_DATE_ASC = Columns.ISSUING_DATE + " ASC";
        public static final String ISSUING_DATE_DESC = Columns.ISSUING_DATE + " DESC";
    }

}
