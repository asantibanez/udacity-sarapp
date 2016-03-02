package com.andressantibanez.sarapp;

import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetail;
import com.andressantibanez.sarapp.navigation.invoiceview.InvoiceViewDetailsLoader;
import com.andressantibanez.sarapp.testing.TestHelper;

import java.util.List;

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

        List<InvoiceDetail> result = getLoaderResultSynchronously(loader);
        assertThat(result, notNullValue());

        assertThat(result.size() > 0, is(true));
    }



}
