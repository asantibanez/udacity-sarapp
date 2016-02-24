package com.andressantibanez.sarapp.navigation.invoiceview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;
import com.andressantibanez.sarapp.database.invoices.InvoicesSelection;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoiceViewActivity extends AppCompatActivity {

    private static final String LOG_TAG = InvoiceViewActivity.class.getSimpleName();
    public static final String INVOICE_ID = "invoice_id";

    String mInvoiceId;

    @Bind(R.id.supplier_name) TextView mSupplierNameView;
    @Bind(R.id.issuing_date) TextView mIssuingDateView;
    @Bind(R.id.subtotal) TextView mSubtotalView;

    public static Intent launchIntent(Context context, String invoiceId) {
        Intent intent = new Intent(context, InvoiceViewActivity.class);
        intent.putExtra(INVOICE_ID, invoiceId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_view);
        ButterKnife.bind(this);

        getParameters();

        //Get invoice data
        getSupportLoaderManager().initLoader(1000, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new InvoicesSelection().whereIdEquals(mInvoiceId).getAsCursorLoader();
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Invoice invoice = new InvoicesCursor(data).first();
                displayInvoiceInfo(invoice);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

        //Get invoice details data
    }

    public void getParameters() {
        mInvoiceId = getIntent().getStringExtra(INVOICE_ID);
        Log.d(LOG_TAG, "Id: " + mInvoiceId);
    }

    public void displayInvoiceInfo(Invoice invoice) {
        mSupplierNameView.setText(invoice.supplierName);
        mIssuingDateView.setText(invoice.readableIssuingDate());
        mSubtotalView.setText(invoice.readableSubtotal());
    }
}
