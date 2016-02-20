package com.andressantibanez.sarapp.database.invoices;

import android.net.Uri;

import com.andressantibanez.sarapp.database.common.BaseSelection;

/**
 * Created by asantibanez on 2/20/16.
 */
public class InvoicesSelection extends BaseSelection<InvoicesSelection> {

    @Override
    public Uri uri() {
        return InvoicesContract.CONTENT_URI;
    }

    @Override
    public InvoicesCursor get() {
        return new InvoicesCursor(getCursor());
    }

    public InvoicesSelection whereIdEquals(String id) {
        return addSelection(InvoicesContract.Columns.ID, EQUALS, id);
    }

}
