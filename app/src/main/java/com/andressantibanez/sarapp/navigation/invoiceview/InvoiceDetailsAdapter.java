package com.andressantibanez.sarapp.navigation.invoiceview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetail;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by asantibanez on 2/24/16.
 */
public class InvoiceDetailsAdapter extends RecyclerView.Adapter<InvoiceDetailsAdapter.ViewHolder> {

    private static final String LOG_TAG = InvoiceDetailsAdapter.class.getSimpleName();

    Context mContext;
    LayoutInflater mInflater;
    List<InvoiceDetail> mDetails;

    String[] mExpenseTypes;
    String[] mExpenseTypesDescriptions;

    InvoiceDetailsAdapterCallbacks mListener;

    public InvoiceDetailsAdapter(Context context, InvoiceDetailsAdapterCallbacks listener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        mListener = listener;

        mExpenseTypes = mContext.getResources().getStringArray(R.array.expense_types);
        mExpenseTypesDescriptions = mContext.getResources().getStringArray(R.array.expense_types_descriptions);
    }

    public InvoiceDetail invoiceDetailAtPosition(int position) {
        return mDetails.get(position);
    }

    public void swapDetails(List<InvoiceDetail> details) {
        mDetails = details;
        notifyDataSetChanged();
    }

    public void onExpenseTypeChange(int position, String newExpenseType) {
        if(mDetails.get(position).expenseType.equalsIgnoreCase(newExpenseType))
            return;

        mListener.onExpenseTypeChanged(position, newExpenseType);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_invoice_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        InvoiceDetail invoiceDetail = mDetails.get(position);

        //Bind data
        holder.productName.setText(invoiceDetail.productName);
        holder.quantity.setText(String.format("%.0f unit(s)", invoiceDetail.quantity));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                mContext,
                android.R.layout.simple_spinner_item,
                mExpenseTypesDescriptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.expenseType.setAdapter(adapter);
        holder.expenseType.setSelection(expenseTypeIndex(invoiceDetail.expenseType));

        //Listeners
        RxAdapterView.itemSelections(holder.expenseType)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if(holder.getAdapterPosition() == RecyclerView.NO_POSITION)
                            return;

                        onExpenseTypeChange(holder.getAdapterPosition(), mExpenseTypes[integer]);
                    }
                });
    }

    public int expenseTypeIndex(String expenseType) {
        for(int i = 0; i < mExpenseTypes.length; i++) {
            if(mExpenseTypes[i].equalsIgnoreCase(expenseType))
                return i;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDetails != null ? mDetails.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.product_name) TextView productName;
        @Bind(R.id.quantity) TextView quantity;
        @Bind(R.id.progress_bar) ProgressBar progressBar;
        @Bind(R.id.expense_type) Spinner expenseType;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface InvoiceDetailsAdapterCallbacks {
        void onExpenseTypeChanged(int position, String newExpenseType);
    }

}
