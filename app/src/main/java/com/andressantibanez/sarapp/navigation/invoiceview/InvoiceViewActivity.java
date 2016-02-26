package com.andressantibanez.sarapp.navigation.invoiceview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetail;
import com.andressantibanez.sarapp.database.invoices.Invoice;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;
import com.andressantibanez.sarapp.database.invoices.InvoicesSelection;
import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.UpdateDetailExpenseTypeResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoiceViewActivity extends AppCompatActivity {

    private static final String LOG_TAG = InvoiceViewActivity.class.getSimpleName();
    public static final String INVOICE_ID = "invoice_id";

    String mInvoiceId;
    InvoiceDetailsAdapter mAdapter;

    @Bind(R.id.root) View mRootView;
    @Bind(R.id.toolbar) Toolbar mToolbarView;
    @Bind(R.id.supplier_name) TextView mSupplierNameView;
    @Bind(R.id.issuing_date) TextView mIssuingDateView;
    @Bind(R.id.subtotal) TextView mSubtotalView;
    @Bind(R.id.details_list) RecyclerView mDetailsListView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;

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

        setupToolbar();

        //Setup adapter
        mAdapter = new InvoiceDetailsAdapter(this, new InvoiceDetailsAdapter.InvoiceDetailsAdapterCallbacks() {
            @Override
            public void onExpenseTypeChanged(final int position, final String newExpenseType) {
                final InvoiceDetailsAdapter.ViewHolder holder = (InvoiceDetailsAdapter.ViewHolder)
                        mDetailsListView.findViewHolderForAdapterPosition(position);

                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        UpdateDetailExpenseTypeResponse updateDetailExpenseTypeResponse;
                        updateDetailExpenseTypeResponse = SarappWebService.create().updateDetailExpenseType(
                                Sarapp.instance().getToken(),
                                mAdapter.invoiceDetailAtPosition(position).invoiceId,
                                mAdapter.invoiceDetailAtPosition(position).id,
                                newExpenseType
                        );

                        if(!updateDetailExpenseTypeResponse.hasErrors()) {
                            mAdapter.invoiceDetailAtPosition(position).expenseType = newExpenseType;
                            mAdapter.invoiceDetailAtPosition(position).update();

                            return true;
                        }

                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        holder.progressBar.setVisibility(View.GONE);
                        mAdapter.notifyItemChanged(position);

                        String message;
                        if (result)
                            message = getString(R.string.expense_type_changed_successfully);
                        else
                            message = getString(R.string.expense_type_change_error);

                        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
                    }

                }.execute();
            }
        });

        //Setup details list
        mDetailsListView.setLayoutManager(new LinearLayoutManager(this));
        mDetailsListView.setAdapter(mAdapter);

        getParameters();

        //Get invoice data
        getSupportLoaderManager().initLoader(1000, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new InvoicesSelection().whereIdEquals(mInvoiceId).getAsCursorLoader();
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Invoice invoice = new InvoicesCursor(data).first();
                displayInvoiceInfo(invoice);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

        //Get invoice details data
        getSupportLoaderManager().initLoader(2000, null, new LoaderManager.LoaderCallbacks<List<InvoiceDetail>>() {

            @Override
            public Loader<List<InvoiceDetail>> onCreateLoader(int id, Bundle args) {
                return new InvoiceViewDetailsLoader(InvoiceViewActivity.this, mInvoiceId);
            }

            @Override
            public void onLoadFinished(Loader<List<InvoiceDetail>> loader, List<InvoiceDetail> data) {
                if(data == null) {
                    //TODO: handle no data gracefully
                    return;
                }

                mAdapter.swapDetails(data);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoaderReset(Loader<List<InvoiceDetail>> loader) {
                mAdapter.swapDetails(null);
            }
        });


    }

    public void setupToolbar() {
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getParameters() {
        mInvoiceId = getIntent().getStringExtra(INVOICE_ID);
    }

    public void displayInvoiceInfo(Invoice invoice) {
        mSupplierNameView.setText(invoice.supplierName);
        mIssuingDateView.setText(invoice.readableIssuingDate());
        mSubtotalView.setText(invoice.readableSubtotal());
    }
}
