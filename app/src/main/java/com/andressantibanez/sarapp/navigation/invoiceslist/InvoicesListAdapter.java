package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;
import com.andressantibanez.sarapp.navigation.invoiceview.InvoiceViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by asantibanez on 2/21/16.
 */
public class InvoicesListAdapter extends RecyclerView.Adapter<InvoicesListAdapter.ViewHolder> {

    Context mContext;
    InvoicesCursor mInvoicesCursor;
    InvoicesListAdapterCallbacks mListener;

    public InvoicesListAdapter(Context context, InvoicesListAdapterCallbacks listener) {
        mContext = context;
        mListener = listener;
    }

    public void swapCursor(Cursor cursor) {
        if(mInvoicesCursor != null)
            mInvoicesCursor.discard();

        if(cursor != null)
            mInvoicesCursor = new InvoicesCursor(cursor);

        notifyDataSetChanged();
    }

    private void onInvoiceClicked(int position) {
        if(position == RecyclerView.NO_POSITION)
            return;

        String invoiceId = mInvoicesCursor.invoiceAt(position).id;
        mListener.onInvoiceClicked(position, invoiceId);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Invoice invoice = mInvoicesCursor.invoiceAt(position);

        //Bind data
        holder.supplierName.setText(invoice.supplierName);
        holder.issuingDate.setText(invoice.readableIssuingDate());
        holder.subtotal.setText(invoice.readableSubtotal());

        //Listeners
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInvoiceClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInvoicesCursor != null ? mInvoicesCursor.count() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root) View root;
        @Bind(R.id.supplier_name) TextView supplierName;
        @Bind(R.id.issuing_date) TextView issuingDate;
        @Bind(R.id.subtotal) TextView subtotal;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface InvoicesListAdapterCallbacks {
        void onInvoiceClicked(int position, String invoiceId);
    }

}
