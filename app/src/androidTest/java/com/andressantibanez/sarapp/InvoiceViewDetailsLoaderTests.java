package com.andressantibanez.sarapp;

import android.database.Cursor;

import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsCursor;
import com.andressantibanez.sarapp.navigation.invoiceview.InvoiceViewDetailsLoader;
import com.andressantibanez.sarapp.testing.TestHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by asantibanez on 2/24/16.
 */
public class InvoiceViewDetailsLoaderTests extends SupportLoaderTestCase {

    public void testLoader() {
        InvoiceViewDetailsLoader loader = new InvoiceViewDetailsLoader(
                getContext(), TestHelper.getValidLoginToken(), TestHelper.getValidInvoice().id
        );

        Cursor cursor = getLoaderResultSynchronously(loader);
        assertThat(cursor, notNullValue());

        assertThat(new InvoiceDetailsCursor(cursor).count() > 0, is(true));
    }



}
