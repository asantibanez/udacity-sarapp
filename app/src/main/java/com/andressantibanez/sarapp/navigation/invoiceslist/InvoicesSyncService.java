package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.exceptions.CreateRecordException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class InvoicesSyncService extends IntentService {

    public InvoicesSyncService() {
        super("InvoicesSyncService");
    }

    public static void execute(Context context) {
        Intent intent = new Intent(context, InvoicesSyncService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = Sarapp.instance().getToken();
        GetInvoicesResponse getInvoicesResponse = SarappWebService.create().getInvoices(token);

        for (GetInvoicesResponse.Invoice data: getInvoicesResponse.invoices) {
            Invoice invoice = Invoice.fromParts(
                    data.id, data.supplierName, data.supplierTaxpayerId,
                    data.issuingDate(), data.subtotal, data.tax, data.total
            );

            try {
                invoice.create();
            } catch (CreateRecordException e) {
                e.printStackTrace();
            }
        }
    }

}
