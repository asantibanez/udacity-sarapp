package com.andressantibanez.sarapp;

import android.test.ProviderTestCase2;

import com.andressantibanez.sarapp.database.SarappProvider;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;
import com.andressantibanez.sarapp.database.invoices.InvoicesSelection;
import com.andressantibanez.sarapp.exceptions.CreateRecordException;
import com.andressantibanez.sarapp.exceptions.RecordNotFoundException;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by asantibanez on 2/20/16.
 */
public class InvoicesTests extends ProviderTestCase2<SarappProvider> {

    public InvoicesTests() {
        super(SarappProvider.class, SarappProvider.AUTHORITY);
    }

    public void testCreate() throws CreateRecordException {
        Invoice invoice = new Invoice();
        invoice.id = "001";
        invoice.create();

        InvoicesSelection selection = new InvoicesSelection();
        InvoicesCursor cursor = selection.get();
        assertThat(cursor, notNullValue());
        assertThat(cursor.count(), is(1));
    }

    public void testFind() throws CreateRecordException, RecordNotFoundException {
        Date date = new Date();

        Invoice.fromParts("001", "Pamela", "1111111111", date, 100, 12, 112).create();
        Invoice.fromParts("002", "Andres", "2222222222", date, 200, 24, 224).create();
        Invoice.fromParts("003", "Sara", "3333333333", date, 300, 36, 336).create();

        InvoicesSelection selection = new InvoicesSelection();
        InvoicesCursor cursor = selection.get();
        assertThat(cursor, notNullValue());
        assertThat(cursor.count(), is(3));

        Invoice invoice = Invoice.find("001");
        assertThat(invoice.supplierName, is("Pamela"));
        assertThat(invoice.supplierTaxpayerId, is("1111111111"));
        assertThat(invoice.issuingDate, is(date));
        assertThat(invoice.subtotal, is(100d));
        assertThat(invoice.tax, is(12d));
        assertThat(invoice.total, is(112d));

        assertThat(Invoice.find("002").subtotal, is(200d));
        assertThat(Invoice.find("003").subtotal, is(300d));
    }

    public void testUpdate() throws CreateRecordException, RecordNotFoundException {
        Invoice invoice = Invoice.fromParts("001", "Pamela", "1111111111", new Date(), 100, 12, 112);
        invoice.create();

        invoice.subtotal = 300;
        invoice.tax = 36;
        invoice.total = 336;
        invoice.update();

        assertThat(Invoice.find("001").subtotal, is(300d));
        assertThat(Invoice.find("001").total, is(336d));
    }

}
