package com.andressantibanez.sarapp.navigation.addinvoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.andressantibanez.sarapp.R;
import com.nononsenseapps.filepicker.FilePickerActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddInvoiceActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddInvoiceActivity.class.getSimpleName();
    private static final int FILE_PICKER_REQUEST_CODE = 5000;

    @Bind(R.id.attach_invoice_button) Button mAttachInvoiceButton;

    public static Intent launchIntent(Context context) {
        return new Intent(context, AddInvoiceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        ButterKnife.bind(this);
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
            Log.d(LOG_TAG, "Result: " + data.getData());
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
