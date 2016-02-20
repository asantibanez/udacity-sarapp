package com.andressantibanez.sarapp.database.invoicedetails;

import android.content.ContentValues;
import android.net.Uri;

import com.andressantibanez.sarapp.database.SarappProvider;
import com.andressantibanez.sarapp.exceptions.CreateRecordException;
import com.andressantibanez.sarapp.exceptions.RecordNotFoundException;

/**
 * Created by asantibanez on 2/20/16.
 */
public class InvoiceDetail {

    public String id;
    public String invoiceId;
    public String productId;
    public String productName;
    public double quantity;
    public double price;
    public double discount;
    public double subtotal;
    public String expenseType;

    public static class ExpenseTypes {
        public final static String NONE = "none";
        public final static String FEEDING = "feeding";
        public final static String HEALTH = "health";
        public final static String DWELLING = "dwelling";
        public final static String CLOTHING = "clothing";
        public final static String EDUCATION = "education";
    }

    public InvoiceDetail() {
        id = "";
        invoiceId = "";
        productId = "";
        productName = "";
        quantity = 0;
        price = 0;
        discount = 0;
        subtotal = 0;
        expenseType = "";
    }

    public static InvoiceDetail fromParts(String id, String invoiceId, String productId,String  productName, double quantity, double price, double discount, double subtotal, String expenseType) {
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.id = id;
        invoiceDetail.invoiceId = invoiceId;
        invoiceDetail.productId = productId;
        invoiceDetail.productName = productName;
        invoiceDetail.quantity = quantity;
        invoiceDetail.price = price;
        invoiceDetail.discount = discount;
        invoiceDetail.subtotal = subtotal;

        invoiceDetail.expenseType =
                expenseType != null && expenseType.length() > 0 ?
                        expenseType :
                        ExpenseTypes.NONE;

        return invoiceDetail;
    }

    public void create() throws CreateRecordException {
        Uri uri = SarappProvider.insertValues(InvoiceDetailsContract.CONTENT_URI, contentValues());
        if (uri == null)
            throw new CreateRecordException();
    }

    public void update() {
        InvoiceDetailsSelection selection = new InvoiceDetailsSelection().whereIdEquals(this.id);
        SarappProvider.updateValues(
                InvoiceDetailsContract.CONTENT_URI,
                contentValues(),
                selection.selection(),
                selection.selectionArgs()
        );
    }

    public static InvoiceDetail find(String id) throws RecordNotFoundException {
        InvoiceDetail invoiceDetail = new InvoiceDetailsSelection().whereIdEquals(id).get().first();
        if(invoiceDetail == null)
            throw new RecordNotFoundException();

        return invoiceDetail;
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(InvoiceDetailsContract.Columns.ID, id);
        values.put(InvoiceDetailsContract.Columns.INVOICE_ID, invoiceId);
        values.put(InvoiceDetailsContract.Columns.PRODUCT_ID, productId);
        values.put(InvoiceDetailsContract.Columns.PRODUCT_NAME, productName);
        values.put(InvoiceDetailsContract.Columns.QUANTITY, quantity);
        values.put(InvoiceDetailsContract.Columns.PRICE, price);
        values.put(InvoiceDetailsContract.Columns.DISCOUNT, discount);
        values.put(InvoiceDetailsContract.Columns.SUBTOTAL, subtotal);
        values.put(InvoiceDetailsContract.Columns.EXPENSE_TYPE, expenseType);

        return values;
    }

}
