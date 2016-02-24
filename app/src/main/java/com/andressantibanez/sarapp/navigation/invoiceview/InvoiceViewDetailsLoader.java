package com.andressantibanez.sarapp.navigation.invoiceview;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsSelection;

/**
 * Created by asantibanez on 2/24/16.
 */
public class InvoiceViewDetailsLoader extends AsyncTaskLoader<Cursor> {

    public InvoiceViewDetailsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        return null;
//        return new InvoiceDetailsSelection().get();
    }
}
