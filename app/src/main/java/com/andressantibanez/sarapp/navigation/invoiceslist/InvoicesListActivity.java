package com.andressantibanez.sarapp.navigation.invoiceslist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.andressantibanez.sarapp.navigation.authentication.AuthenticationActivity;
import com.andressantibanez.sarapp.navigation.expensesummary.ExpenseSummaryActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoicesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = InvoicesListActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.navigation_view) NavigationView mNavigationView;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
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

        setupToolbar();
        setupNavigationView();
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

    public void setupNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                //Expense Summary
                if(item.getItemId() == R.id.drawer_item_expense_summary) {
                    startActivity(ExpenseSummaryActivity.launchIntent(InvoicesListActivity.this));
                }

                //Logout
                if(item.getItemId() == R.id.drawer_item_logout) {
                    finish();
                    Sarapp.instance().setToken("");
                    startActivity(AuthenticationActivity.launchIntent(InvoicesListActivity.this));
                }

                return true;
            }
        });
    }

    public void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    public void setupDrawerLayout() {
        ActionBarDrawerToggle actionBarDrawerToggle;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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
