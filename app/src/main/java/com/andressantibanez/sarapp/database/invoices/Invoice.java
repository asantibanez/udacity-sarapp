package com.andressantibanez.sarapp.database.invoices;

import android.content.ContentValues;
import android.net.Uri;

import com.andressantibanez.sarapp.Utils;
import com.andressantibanez.sarapp.database.SarappProvider;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsContract;
import com.andressantibanez.sarapp.exceptions.CreateRecordException;
import com.andressantibanez.sarapp.exceptions.RecordNotFoundException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asantibanez on 2/20/16.
 */
public class Invoice {

    public String id;
    public String supplierName;
    public String supplierTaxpayerId;
    public Date issuingDate;
    public double subtotal;
    public double tax;
    public double total;

    public Invoice() {
        id = "";
        supplierName = "";
        supplierTaxpayerId = "";
        issuingDate = new Date(0);
        subtotal = 0;
        tax = 0;
        total = 0;
    }

    public static Invoice fromParts(String id, String supplierName, String supplierTaxpayerId, Date issuingDate, double subtotal, double tax, double total) {
        Invoice invoice = new Invoice();
        invoice.id = id;
        invoice.supplierName = supplierName;
        invoice.supplierTaxpayerId = supplierTaxpayerId;
        invoice.issuingDate = issuingDate;
        invoice.subtotal = subtotal;
        invoice.tax = tax;
        invoice.total = total;
        return invoice;
    }

    public void create() throws CreateRecordException {
        Uri uri = SarappProvider.insertValues(InvoicesContract.CONTENT_URI, contentValues());
        if (uri == null)
            throw new CreateRecordException();
    }

    public void update() {
        InvoicesSelection selection = new InvoicesSelection().whereIdEquals(this.id);
        SarappProvider.updateValues(
                InvoiceDetailsContract.CONTENT_URI,
                contentValues(),
                selection.selection(),
                selection.selectionArgs()
        );
    }

    public static Invoice find(String id) throws RecordNotFoundException {
        Invoice invoice = new InvoicesSelection().whereIdEquals(id).get().first();
        if(invoice == null)
            throw new RecordNotFoundException();

        return invoice;
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(InvoicesContract.Columns.ID, id);
        values.put(InvoicesContract.Columns.SUPPLIER_NAME, supplierName);
        values.put(InvoicesContract.Columns.SUPPLIER_TAXPAYER_ID, supplierTaxpayerId);
        values.put(InvoicesContract.Columns.ISSUING_DATE, issuingDate.getTime());
        values.put(InvoicesContract.Columns.SUBTOTAL, subtotal);
        values.put(InvoicesContract.Columns.TAX, tax);
        values.put(InvoicesContract.Columns.TOTAL, total);

        return values;
    }

    public String readableSubtotal() {
        return Utils.asMoneyString(subtotal);
    }

    public String readableIssuingDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        return dateFormat.format(issuingDate);
    }

}
