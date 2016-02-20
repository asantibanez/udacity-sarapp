package com.andressantibanez.sarapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsContract;
import com.andressantibanez.sarapp.database.invoices.InvoicesContract;

import java.util.ArrayList;

/**
 * Created by asantibanez on 2/20/16.
 */
public class DatabaseSetup {

    private static final String LOG_TAG = DatabaseSetup.class.getSimpleName();

    public static void execute(SQLiteDatabase database) {
        Log.d(LOG_TAG, "Running setup");

        ArrayList<String> columns;
        ArrayList<String> keyColumns;
        String sql;


        //********************************************************
        // Invoices Table
        //********************************************************
        columns = new ArrayList<>();
        keyColumns = new ArrayList<>();

        //Columns
        columns.add(String.format("%s %s PRIMARY KEY AUTOINCREMENT", InvoicesContract.Columns._ID, "INTEGER"));
        columns.add(String.format("%s %s NOT NULL", InvoicesContract.Columns.ID, "TEXT"));
        columns.add(String.format("%s %s NOT NULL", InvoicesContract.Columns.SUPPLIER_TAXPAYER_ID, "TEXT"));
        columns.add(String.format("%s %s NOT NULL", InvoicesContract.Columns.SUPPLIER_NAME, "TEXT"));
        columns.add(String.format("%s %s NOT NULL", InvoicesContract.Columns.ISSUING_DATE, "INTEGER"));
        columns.add(String.format("%s %s NOT NULL", InvoicesContract.Columns.SUBTOTAL, "REAL"));
        columns.add(String.format("%s %s NOT NULL", InvoicesContract.Columns.TAX, "REAL"));
        columns.add(String.format("%s %s NOT NULL", InvoicesContract.Columns.TOTAL, "REAL"));

        //Key Columns
        keyColumns.add(InvoicesContract.Columns.ID);

        //Create Table
        sql = "";
        sql += String.format("CREATE TABLE %s (%s, CONSTRAINT primary_key UNIQUE (%s) ON CONFLICT REPLACE)",
                InvoicesContract.TABLE_NAME,
                TextUtils.join(",", columns.toArray(new String[columns.size()])),
                TextUtils.join(",", keyColumns.toArray(new String[keyColumns.size()]))
        );
        database.execSQL(sql);



        //********************************************************
        // Invoice Details Table
        //********************************************************
        columns = new ArrayList<>();
        keyColumns = new ArrayList<>();

        //Columns
        columns.add(String.format("%s %s PRIMARY KEY AUTOINCREMENT", InvoiceDetailsContract.Columns._ID, "INTEGER"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.ID, "TEXT"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.INVOICE_ID, "TEXT"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.PRODUCT_ID, "TEXT"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.PRODUCT_NAME, "TEXT"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.QUANTITY, "REAL"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.PRICE, "REAL"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.DISCOUNT, "REAL"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.SUBTOTAL, "REAL"));
        columns.add(String.format("%s %s NOT NULL", InvoiceDetailsContract.Columns.EXPENSE_TYPE, "TEXT"));

        //Key Columns
        keyColumns.add(InvoiceDetailsContract.Columns.ID);

        //Create Table
        sql = "";
        sql += String.format("CREATE TABLE %s (%s, CONSTRAINT primary_key UNIQUE (%s) ON CONFLICT REPLACE)",
                InvoiceDetailsContract.TABLE_NAME,
                TextUtils.join(",", columns.toArray(new String[columns.size()])),
                TextUtils.join(",", keyColumns.toArray(new String[keyColumns.size()]))
        );
        database.execSQL(sql);
    }

}
