package com.andressantibanez.sarapp.navigation.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.navigation.invoiceslist.InvoicesListActivity;
import com.andressantibanez.sarapp.navigation.invoiceview.InvoiceViewActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String LOG_TAG = AuthenticationActivity.class.getSimpleName();

    @Bind(R.id.fragment_container) FrameLayout mFragmentContainer;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    Tracker mTracker;

    public static Intent launchIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        //Get tracker and send activity hit
        Sarapp application = (Sarapp) getApplication();
        mTracker = application.getDefaultTracker();

        String name = "Authentication";
        Log.i(LOG_TAG, "Setting screen name: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        //Set action bar
        setSupportActionBar(mToolbar);

        //Check if user already logged in
        String token = Sarapp.instance().getToken();
        if(token.length() > 0) {
            startActivity(InvoicesListActivity.launchIntent(this));
            finish();
        }

        //Add login fragment
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(mFragmentContainer.getId(), LoginFragment.newInstance())
                    .commit();
        }
    }

    public void showRegistrationView() {
        getSupportFragmentManager().beginTransaction()
                .replace(mFragmentContainer.getId(), RegistrationFragment.newInstance())
                .commit();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Registration")
                .build());
    }

    public void showLoginView() {
        getSupportFragmentManager().beginTransaction()
                .replace(mFragmentContainer.getId(), LoginFragment.newInstance())
                .commit();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Login")
                .build());
    }

}
