package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by asantibanez on 2/21/16.
 */
public class InvoicesListAdapter extends RecyclerView.Adapter<InvoicesListAdapter.ViewHolder> {

    InvoicesCursor mInvoicesCursor;

    public InvoicesListAdapter() {

    }

    public void swapCursor(Cursor cursor) {
        if(mInvoicesCursor != null)
            mInvoicesCursor.discard();

        if(cursor != null)
            mInvoicesCursor = new InvoicesCursor(cursor);

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Invoice invoice = mInvoicesCursor.invoiceAt(position);
        holder.supplierName.setText(invoice.supplierName);
        holder.issuingDate.setText(invoice.readableIssuingDate());
        holder.subtotal.setText(invoice.readableSubtotal());
    }

    @Override
    public int getItemCount() {
        return mInvoicesCursor != null ? mInvoicesCursor.count() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.supplier_name) TextView supplierName;
        @Bind(R.id.issuing_date) TextView issuingDate;
        @Bind(R.id.subtotal) TextView subtotal;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
