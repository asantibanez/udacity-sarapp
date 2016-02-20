package com.andressantibanez.sarapp;

import android.test.ProviderTestCase2;

import com.andressantibanez.sarapp.database.SarappProvider;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetail;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsSelection;
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
public class InvoiceDetailsTests extends ProviderTestCase2<SarappProvider> {

    public InvoiceDetailsTests() {
        super(SarappProvider.class, SarappProvider.AUTHORITY);
    }

    public void testCreate() throws CreateRecordException, RecordNotFoundException {
        InvoiceDetail.fromParts("001", "001002003", "001", "Product Name", 2, 4, 1, 2, InvoiceDetail.ExpenseTypes.FEEDING).create();
        assertThat(new InvoiceDetailsSelection().get().count(), is(1));
    }

    public void testFind() throws CreateRecordException, RecordNotFoundException {
        InvoiceDetail.fromParts("001", "001002003", "001", "Bread", 1, 2, 0, 2, InvoiceDetail.ExpenseTypes.FEEDING).create();
        InvoiceDetail.fromParts("002", "001002003", "002", "Shirt", 1, 3, 0, 3, InvoiceDetail.ExpenseTypes.CLOTHING).create();
        InvoiceDetail.fromParts("003", "001002003", "003", "Insurance", 1, 6, 1, 5, InvoiceDetail.ExpenseTypes.HEALTH).create();
        assertThat(new InvoiceDetailsSelection().get().count(), is(3));

        InvoiceDetail invoiceDetail = InvoiceDetail.find("003");
        assertThat(invoiceDetail.productName, is("Insurance"));
        assertThat(invoiceDetail.subtotal, is(5d));
    }

    public void testUpdate() throws CreateRecordException, RecordNotFoundException {
        InvoiceDetail invoiceDetail = InvoiceDetail.fromParts("001", "001002003", "001", "Bread", 1, 2, 0, 2, InvoiceDetail.ExpenseTypes.NONE);
        invoiceDetail.create();
        assertThat(InvoiceDetail.find("001").expenseType, is(InvoiceDetail.ExpenseTypes.NONE));

        invoiceDetail.expenseType = InvoiceDetail.ExpenseTypes.FEEDING;
        invoiceDetail.update();
        assertThat(InvoiceDetail.find("001").expenseType, is(InvoiceDetail.ExpenseTypes.FEEDING));
    }

}
