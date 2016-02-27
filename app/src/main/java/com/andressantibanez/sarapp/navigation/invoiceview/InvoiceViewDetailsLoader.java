package com.andressantibanez.sarapp.navigation.invoiceview;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetail;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsCursor;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsSelection;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.endpoints.SarappWebApi;
import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoiceInfoResponse;
import com.andressantibanez.sarapp.exceptions.CreateRecordException;

import java.util.List;

/**
 * Created by asantibanez on 2/24/16.
 */
public class InvoiceViewDetailsLoader extends AsyncTaskLoader<List<InvoiceDetail>> {

    boolean mRequiresLoad;
    String mInvoiceId;
    String mToken;

    public InvoiceViewDetailsLoader(Context context, String invoiceId) {
        super(context);
        mRequiresLoad = true;
        mInvoiceId = invoiceId;
        mToken = Sarapp.instance().getToken();
    }

    public InvoiceViewDetailsLoader(Context context, String token, String invoiceId) {
        super(context);
        mRequiresLoad = true;
        mInvoiceId = invoiceId;
        mToken = token;
    }

    @Override
    protected void onStartLoading() {
        if(mRequiresLoad)
            forceLoad();

        mRequiresLoad = false;
    }

    @Override
    public List<InvoiceDetail> loadInBackground() {
        GetInvoiceInfoResponse getInvoiceInfoResponse;
        getInvoiceInfoResponse = SarappWebService.create()
                .getInvoiceInfo(mToken, mInvoiceId);

        if(getInvoiceInfoResponse.hasErrors())
            return null;

        //Sync invoice
        try {
            Invoice.fromParts(
                    getInvoiceInfoResponse.id,
                    getInvoiceInfoResponse.supplierName,
                    getInvoiceInfoResponse.supplierTaxpayerId,
                    getInvoiceInfoResponse.issuingDate(),
                    getInvoiceInfoResponse.subtotal,
                    getInvoiceInfoResponse.tax,
                    getInvoiceInfoResponse.total
            ).create();
        } catch (CreateRecordException e) {
            e.printStackTrace();
            return null;
        }

        //Sync details
        for (GetInvoiceInfoResponse.InvoiceDetail detailData : getInvoiceInfoResponse.details) {
            try {
                InvoiceDetail.fromParts(
                        detailData.id, detailData.invoiceId, detailData.productId,
                        detailData.productName, detailData.quantity, detailData.price,
                        detailData.discount, detailData.subtotal, detailData.expenseType
                ).create();
            } catch (CreateRecordException e) {
                e.printStackTrace();
                return null;
            }
        }

        InvoiceDetailsCursor invoiceDetailsCursor = new InvoiceDetailsSelection()
                .whereInvoiceIdEquals(mInvoiceId)
                .get();

        List<InvoiceDetail> data = invoiceDetailsCursor.all();

        invoiceDetailsCursor.discard();

        return data;
    }
}
