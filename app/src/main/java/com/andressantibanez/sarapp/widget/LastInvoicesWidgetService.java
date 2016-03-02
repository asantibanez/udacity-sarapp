package com.andressantibanez.sarapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.database.invoices.InvoicesContract;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;
import com.andressantibanez.sarapp.database.invoices.InvoicesSelection;

import java.util.ArrayList;

/**
 * Created by asantibanez on 3/1/16.
 */
public class LastInvoicesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(getApplicationContext(), intent);
    }

}

class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public Context mContext;
    public ArrayList<Invoice> mInvoicesList;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mInvoicesList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        mInvoicesList = new InvoicesSelection()
                .orderBy(InvoicesContract.OrderBy.ISSUING_DATE_DESC)
                .get().all();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        mInvoicesList.clear();
    }

    @Override
    public int getCount() {
        //Only 10 items
        return mInvoicesList.size() > 10 ? 10 : mInvoicesList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Invoice invoice = mInvoicesList.get(i);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_invoice);
        remoteViews.setTextViewText(R.id.supplier_name, invoice.supplierName);
        remoteViews.setTextViewText(R.id.issuing_date, invoice.readableIssuingDate());
        remoteViews.setTextViewText(R.id.subtotal, invoice.readableSubtotal());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
