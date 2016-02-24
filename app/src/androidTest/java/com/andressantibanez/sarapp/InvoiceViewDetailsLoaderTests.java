package com.andressantibanez.sarapp;

import android.database.Cursor;

import com.andressantibanez.sarapp.navigation.invoiceview.InvoiceViewDetailsLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by asantibanez on 2/24/16.
 */
public class InvoiceViewDetailsLoaderTests extends SupportLoaderTestCase {

    public void testLoader() {
        Cursor cursor = getLoaderResultSynchronously(new InvoiceViewDetailsLoader(getContext()));
        assertThat(cursor, notNullValue());
    }




}
