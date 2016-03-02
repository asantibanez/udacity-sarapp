package com.andressantibanez.sarapp.navigation.addinvoice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.UploadInvoiceFileResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UploadedInvoiceFileToInvoiceResponse;

import java.io.File;

/**
 * Created by asantibanez on 2/27/16.
 */
public class AddInvoiceIntentService extends IntentService {

    private static final String LOG_TAG = AddInvoiceIntentService.class.getSimpleName();

    private static final String BROADCAST_CHANNEL = "broadcast_channel";
    private static final String FILE_PATH = "file_path";

    public static final String OPERATION_SUCCESSFUL = "operation_result";
    public static final String OPERATION_MESSAGE = "operation_message";
    public static final String OPERATION_INVOICE_ID = "operation_invoice_id";

    public AddInvoiceIntentService() {
        super("AddInvoiceIntentService");
    }

    public static void execute(Context context, String channel, String filePath) {
        Intent intent = new Intent(context, AddInvoiceIntentService.class);
        intent.putExtra(BROADCAST_CHANNEL, channel);
        intent.putExtra(FILE_PATH, filePath);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String broadcastChannel = intent.getStringExtra(BROADCAST_CHANNEL);
        String filePath = intent.getStringExtra(FILE_PATH);

        //Check if file exists
        if( !new File(filePath).exists() ) {
            sendOperationErrorBroadcast(broadcastChannel, getString(R.string.invalid_file));
            return;
        }

        //Upload file
        UploadInvoiceFileResponse uploadInvoiceFileResponse;
        uploadInvoiceFileResponse = SarappWebService.create().uploadInvoceFile(
                Sarapp.instance().getToken(),
                filePath
        );
        //Check for errors
        if(uploadInvoiceFileResponse.hasErrors()) {
            String errors = TextUtils.join("\n", uploadInvoiceFileResponse.errors.toArray());
            sendOperationErrorBroadcast(broadcastChannel, errors);
            return;
        }

        //Transform to Invoice
        UploadedInvoiceFileToInvoiceResponse uploadedInvoiceFileToInvoiceResponse;
        uploadedInvoiceFileToInvoiceResponse = SarappWebService.create().uploadedInvoiceFileToInvoice(
                Sarapp.instance().getToken(),
                uploadInvoiceFileResponse.electronicInvoiceId
        );
        //Check for errors
        if(uploadedInvoiceFileToInvoiceResponse.hasErrors()) {
            String errors = TextUtils.join("\n", uploadedInvoiceFileToInvoiceResponse.errors.toArray());
            sendOperationErrorBroadcast(broadcastChannel, errors);
            return;
        }

        //Send operation successful
        sendOperationOkBroadcast(broadcastChannel, uploadedInvoiceFileToInvoiceResponse.invoiceId);
    }

    public void sendOperationOkBroadcast(String channel, String invoiceId) {
        Intent intent = new Intent(channel);
        intent.putExtra(OPERATION_SUCCESSFUL, true);
        intent.putExtra(OPERATION_INVOICE_ID, invoiceId);
        sendBroadcast(intent);
    }

    public void sendOperationErrorBroadcast(String channel, String message) {
        Intent intent = new Intent(channel);
        intent.putExtra(OPERATION_SUCCESSFUL, false);
        intent.putExtra(OPERATION_MESSAGE, message);
        sendBroadcast(intent);
    }
}
