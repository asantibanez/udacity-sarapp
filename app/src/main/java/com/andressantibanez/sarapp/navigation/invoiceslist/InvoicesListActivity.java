package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
    public static final String BROADCAST_CHANNEL = "com.andressantibanez.sarapp.ACTION_SYNC";

    @Bind(R.id.fab) FloatingActionButton mFab;
    @Bind(R.id.invoices_list_container) SwipeRefreshLayout mInvoicesListContainerView;
    @Bind(R.id.invoices_list) RecyclerView mInvoicesListView;

    InvoicesListAdapter mAdapter;
    BroadcastReceiver mBroadcastReceiver;

    public static Intent launchIntent(Context context) {
        return new Intent(context, InvoicesListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices_list);
        ButterKnife.bind(this);

        registerForUpdates();

        //Setup toolbar
        setupToolbar(getString(R.string.invoices_list));
        setupNavigationView(R.id.drawer_item_invoices);
        setupDrawerLayout();

        //Setup invoices list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mInvoicesListView.setLayoutManager(layoutManager);

        mAdapter = new InvoicesListAdapter(this);
        mInvoicesListView.setAdapter(mAdapter);

        //Execute sync on first run
        if(savedInstanceState == null) {
            InvoicesSyncService.execute(this);

            //Run refresh animation a third of second later
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mInvoicesListContainerView.setRefreshing(true);
                }
            }, 300);
        }

        //Setup swipe refresh listener
        mInvoicesListContainerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InvoicesSyncService.execute(InvoicesListActivity.this);
            }
        });

        //Init loader to get invoices from database
        getSupportLoaderManager().initLoader(1000, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterForUpdates();
    }

    public void registerForUpdates() {
        IntentFilter filter = new IntentFilter(BROADCAST_CHANNEL);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mInvoicesListContainerView.setRefreshing(false);
            }
        };

        registerReceiver(mBroadcastReceiver, filter);
    }

    public void unregisterForUpdates() {
        if(mBroadcastReceiver != null)
            unregisterReceiver(mBroadcastReceiver);
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
