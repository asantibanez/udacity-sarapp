package com.andressantibanez.sarapp.navigation.invoiceview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andressantibanez.sarapp.R;

import butterknife.ButterKnife;

public class InvoiceViewActivity extends AppCompatActivity {

    private static final String LOG_TAG = InvoiceViewActivity.class.getSimpleName();
    public static final String INVOICE_ID = "invoice_id";

    String mInvoiceId;

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
    }

    public void getParameters() {
        mInvoiceId = getIntent().getStringExtra(INVOICE_ID);
        Log.d(LOG_TAG, "Id: " + mInvoiceId);
    }
}
