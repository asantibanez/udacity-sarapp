package com.andressantibanez.sarapp.database.invoicedetails;

import android.database.Cursor;

import com.andressantibanez.sarapp.database.common.BaseCursor;
import com.andressantibanez.sarapp.database.invoices.Invoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asantibanez on 2/20/16.
 */
public class InvoiceDetailsCursor extends BaseCursor {

    public InvoiceDetailsCursor(Cursor cursor) {
        super(cursor);
    }

    private InvoiceDetail invoiceDetail() {
        String id = getString(getColumnIndex(InvoiceDetailsContract.Columns.ID));
        String invoiceId = getString(getColumnIndex(InvoiceDetailsContract.Columns.INVOICE_ID));
        String productId = getString(getColumnIndex(InvoiceDetailsContract.Columns.PRODUCT_ID));
        String productName = getString(getColumnIndex(InvoiceDetailsContract.Columns.PRODUCT_NAME));
        double quantity = getDouble(getColumnIndex(InvoiceDetailsContract.Columns.QUANTITY));
        double price = getDouble(getColumnIndex(InvoiceDetailsContract.Columns.PRICE));
        double discount = getDouble(getColumnIndex(InvoiceDetailsContract.Columns.DISCOUNT));
        double subtotal = getDouble(getColumnIndex(InvoiceDetailsContract.Columns.SUBTOTAL));
        String expenseType = getString(getColumnIndex(InvoiceDetailsContract.Columns.EXPENSE_TYPE));

        return InvoiceDetail.fromParts(id, invoiceId, productId, productName, quantity, price, discount, subtotal, expenseType);
    }

    public InvoiceDetail invoiceDetailAt(int position) {
        if(!moveToPosition(position))
            return null;

        return invoiceDetail();
    }

    public List<InvoiceDetail> all() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();

        moveToFirst();
        while(!isAfterLast()) {
            details.add(invoiceDetail());
            moveToNext();
        }

        return details;
    }

    public InvoiceDetail first() {
        if(!hasRecords())
            return null;

        moveToFirst();
        return invoiceDetail();
    }

    public boolean hasRecords() {
        return count() > 0;
    }

}
