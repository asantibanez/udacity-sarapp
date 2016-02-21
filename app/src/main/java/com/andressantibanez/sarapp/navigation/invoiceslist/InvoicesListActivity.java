package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.database.invoices.InvoicesCursor;
import com.andressantibanez.sarapp.database.invoices.InvoicesSelection;

public class InvoicesListActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = InvoicesListActivity.class.getSimpleName();

    public static Intent launchIntent(Context context) {
        return new Intent(context, InvoicesListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices_list);

        if(savedInstanceState == null) {
            InvoicesSyncService.execute(this);
        }

        getSupportLoaderManager().initLoader(1000, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new InvoicesSelection().getAsCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "Has records: " + new InvoicesCursor(data).hasRecords());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
