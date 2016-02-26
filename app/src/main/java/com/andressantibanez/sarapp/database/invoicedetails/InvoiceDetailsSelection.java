package com.andressantibanez.sarapp.database.invoicedetails;

import android.net.Uri;

import com.andressantibanez.sarapp.database.common.BaseSelection;
import com.andressantibanez.sarapp.database.invoices.InvoicesContract;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;

/**
 * Created by asantibanez on 2/20/16.
 */
public class InvoiceDetailsSelection extends BaseSelection<InvoiceDetailsSelection> {

    @Override
    public Uri uri() {
        return InvoiceDetailsContract.CONTENT_URI;
    }

    @Override
    public InvoiceDetailsCursor get() {
        return new InvoiceDetailsCursor(getCursor());
    }

    public InvoiceDetailsSelection whereIdEquals(String id) {
        return addSelection(InvoiceDetailsContract.Columns.ID, EQUALS, id);
    }

    public InvoiceDetailsSelection whereInvoiceIdEquals(String invoiceId) {
        return addSelection(InvoiceDetailsContract.Columns.INVOICE_ID, EQUALS, invoiceId);
    }

}
