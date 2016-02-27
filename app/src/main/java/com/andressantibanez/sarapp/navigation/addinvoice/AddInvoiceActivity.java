package com.andressantibanez.sarapp.navigation.addinvoice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.navigation.invoiceslist.InvoicesListActivity;

import butterknife.ButterKnife;

public class AddInvoiceActivity extends AppCompatActivity {

    public static Intent launchIntent(Context context) {
        return new Intent(context, AddInvoiceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        ButterKnife.bind(this);
    }

}
