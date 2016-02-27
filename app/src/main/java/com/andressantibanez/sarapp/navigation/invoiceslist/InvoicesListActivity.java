package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.database.invoices.InvoicesContract;
import com.andressantibanez.sarapp.database.invoices.InvoicesSelection;
import com.andressantibanez.sarapp.navigation.addinvoice.AddInvoiceActivity;
import com.andressantibanez.sarapp.navigation.authentication.AuthenticationActivity;
import com.andressantibanez.sarapp.navigation.common.NavDrawerActivity;
import com.andressantibanez.sarapp.navigation.expensesummary.ExpenseSummaryActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoicesListActivity extends NavDrawerActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = InvoicesListActivity.class.getSimpleName();

    @Bind(R.id.fab) FloatingActionButton mFab;
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

        setupToolbar(getString(R.string.invoices_list));
        setupNavigationView(R.id.drawer_item_invoices);
        setupDrawerLayout();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mInvoicesListView.setLayoutManager(layoutManager);

        mAdapter = new InvoicesListAdapter(this);
        mInvoicesListView.setAdapter(mAdapter);

        if(savedInstanceState == null) {
            InvoicesSyncService.execute(this);
        }

        getSupportLoaderManager().initLoader(1000, null, this);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        startActivity(AddInvoiceActivity.launchIntent(this));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new InvoicesSelection()
                .orderBy(InvoicesContract.OrderBy.ISSUING_DATE_DESC)
                .getAsCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "Records: " + data.getCount());
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
