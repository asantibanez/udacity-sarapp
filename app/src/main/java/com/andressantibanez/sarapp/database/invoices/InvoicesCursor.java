package com.andressantibanez.sarapp.database.invoices;

import android.database.Cursor;

import com.andressantibanez.sarapp.database.common.BaseCursor;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by asantibanez on 2/20/16.
 */
public class InvoicesCursor extends BaseCursor {

    public InvoicesCursor(Cursor cursor) {
        super(cursor);
    }

    public Invoice invoiceAt(int position) {
        if(!moveToPosition(position))
            return null;

        return invoice();
    }

    private Invoice invoice() {
        String id = getString(getColumnIndex(InvoicesContract.Columns.ID));
        String supplierName = getString(getColumnIndex(InvoicesContract.Columns.SUPPLIER_NAME));
        String supplierTaxpayerId = getString(getColumnIndex(InvoicesContract.Columns.SUPPLIER_TAXPAYER_ID));
        Date issuingDate = new Date(getLong(getColumnIndex(InvoicesContract.Columns.ISSUING_DATE)));
        double subtotal = getDouble(getColumnIndex(InvoicesContract.Columns.SUBTOTAL));
        double tax = getDouble(getColumnIndex(InvoicesContract.Columns.TAX));
        double total = getDouble(getColumnIndex(InvoicesContract.Columns.TOTAL));

        return Invoice.fromParts(id, supplierName, supplierTaxpayerId, issuingDate, subtotal, tax, total);
    }

    public ArrayList<Invoice> all() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        while (hasRecords() && !isAfterLast()) {
            invoices.add(invoice());
            moveToNext();
        }

        return invoices;
    }

    public Invoice first() {
        if(!hasRecords())
            return null;

        moveToFirst();
        return invoice();
    }

    public boolean hasRecords() {
        return count() > 0;
    }

}
