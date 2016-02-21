package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.database.invoices.InvoicesContract;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;
import com.andressantibanez.sarapp.database.invoices.InvoicesSelection;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoicesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = InvoicesListActivity.class.getSimpleName();

    @Bind(R.id.invoices_list) RecyclerView mInvoicesListView;

    InvoicesListAdapter mAdapter;

    public static Intent launchIntent(Context context) {
        return new Intent(context, InvoicesListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices_list);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mInvoicesListView.setLayoutManager(layoutManager);

        mAdapter = new InvoicesListAdapter();
        mInvoicesListView.setAdapter(mAdapter);

        if(savedInstanceState == null) {
            InvoicesSyncService.execute(this);
        }

        getSupportLoaderManager().initLoader(1000, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new InvoicesSelection()
                .orderBy(InvoicesContract.OrderBy.ISSUING_DATE_DESC)
                .getAsCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
