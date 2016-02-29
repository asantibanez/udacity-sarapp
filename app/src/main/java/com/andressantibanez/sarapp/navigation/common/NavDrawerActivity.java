package com.andressantibanez.sarapp.navigation.common;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.database.invoicedetails.InvoiceDetailsContract;
import com.andressantibanez.sarapp.database.invoices.InvoicesContract;
import com.andressantibanez.sarapp.navigation.authentication.AuthenticationActivity;
import com.andressantibanez.sarapp.navigation.expensesummary.ExpenseSummaryActivity;
import com.andressantibanez.sarapp.navigation.invoiceslist.InvoicesListActivity;

import butterknife.Bind;

/**
 * Created by Andrés on 2/26/16.
 */
public class NavDrawerActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.navigation_view) NavigationView mNavigationView;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;

    public @Bind(R.id.error_container) View mErrorContainerView;
    public @Bind(R.id.loading_indicator) ProgressBar mLoadingIndicator;
    public @Bind(R.id.error_message) TextView mErrorMessageView;
    public @Bind(R.id.error_action_button) Button mErrorActionButton;

    public void setupNavigationView(int checkedItem) {
        final Handler handler = new Handler();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Invoices List
                        if(item.getItemId() == R.id.drawer_item_invoices) {
                            startActivity(InvoicesListActivity.launchIntent(NavDrawerActivity.this));
                            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                            finish();
                        }

                        //Expense Summary
                        if(item.getItemId() == R.id.drawer_item_expense_summary) {
                            startActivity(ExpenseSummaryActivity.launchIntent(NavDrawerActivity.this));
                            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                            finish();
                        }

                        //Logout
                        if(item.getItemId() == R.id.drawer_item_logout) {

                            //Logout and delete all device data
                            Sarapp.instance().setToken("");
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    getContentResolver().delete(InvoiceDetailsContract.CONTENT_URI, null, null);
                                    getContentResolver().delete(InvoicesContract.CONTENT_URI, null, null);
                                    return null;
                                }
                            }.execute();

                            startActivity(AuthenticationActivity.launchIntent(NavDrawerActivity.this));
                            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                            finish();
                        }
                    }
                }, 300);

                return true;
            }
        });

        mNavigationView.setCheckedItem(checkedItem);
    }

    public void showLoadingIndicator(boolean show) {
        mLoadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showErrorContainer(boolean show, boolean withAction) {
        mErrorContainerView.setVisibility(show ? View.VISIBLE : View.GONE);
        mErrorMessageView.setVisibility(show ? View.VISIBLE : View.GONE);
        mErrorActionButton.setVisibility(show && withAction ? View.VISIBLE : View.GONE);
    }


    public void setupToolbar(String title) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title);
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
}
