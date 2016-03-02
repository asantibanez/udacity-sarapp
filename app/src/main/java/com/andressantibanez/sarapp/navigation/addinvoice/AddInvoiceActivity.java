package com.andressantibanez.sarapp.navigation.addinvoice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.navigation.authentication.AuthenticationActivity;
import com.andressantibanez.sarapp.navigation.invoiceview.InvoiceViewActivity;
import com.nononsenseapps.filepicker.FilePickerActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddInvoiceActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddInvoiceActivity.class.getSimpleName();
    private static final int FILE_PICKER_REQUEST_CODE = 5000;
    private static final String BROADCAST_CHANNEL = "com.andressantibanez.sarapp.ADD_INVOICE";
    private static final String CURRENT_FILE_PATH = "current_file_path";

    String mCurrentFilePath;
    BroadcastReceiver mBroadcastReceiver;

    @Bind(R.id.root) View mRootView;
    @Bind(R.id.toolbar) Toolbar mToolbarView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.attach_invoice_button) Button mAttachInvoiceButton;

    public static Intent launchIntent(Context context) {
        return new Intent(context, AddInvoiceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        ButterKnife.bind(this);

        if(Sarapp.instance().getToken().length() == 0) {
            startActivity(AuthenticationActivity.launchIntent(this));
            finish();
        }

        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_invoice);

        registerForUpdates();

        //Check if intent has data to work with
        Uri uri = getIntent().getData();
        if(uri != null && savedInstanceState == null) {
            mCurrentFilePath = uri.getPath();
            addInvoice();
        }

        //Check saved state
        if(savedInstanceState != null)
            mCurrentFilePath = savedInstanceState.getString(CURRENT_FILE_PATH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterForUpdates();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerForUpdates() {
        IntentFilter filters = new IntentFilter(BROADCAST_CHANNEL);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean operationSuccessful =
                        intent.getBooleanExtra(AddInvoiceIntentService.OPERATION_SUCCESSFUL, false);

                if(!operationSuccessful) {
                    String error = intent.getStringExtra(AddInvoiceIntentService.OPERATION_MESSAGE);
                    Snackbar.make(mRootView, error, Snackbar.LENGTH_LONG).show();
                } else {
                    String invoiceId = intent.getStringExtra(AddInvoiceIntentService.OPERATION_INVOICE_ID);
                    startActivity(InvoiceViewActivity.launchIntent(AddInvoiceActivity.this, invoiceId));
                    finish();
                }

                mProgressBar.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        };

        registerReceiver(mBroadcastReceiver, filters);
    }

    public void unregisterForUpdates() {
        if(mBroadcastReceiver != null)
            unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FILE_PATH, mCurrentFilePath);
    }

    public void addInvoice() {
        mProgressBar.setVisibility(View.VISIBLE);
        mAttachInvoiceButton.setEnabled(false);

        AddInvoiceIntentService.execute(this, BROADCAST_CHANNEL, mCurrentFilePath);
    }

    @OnClick(R.id.attach_invoice_button)
    public void onAttachInvoiceClicked() {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mCurrentFilePath = data.getData().getPath();
            addInvoice();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
