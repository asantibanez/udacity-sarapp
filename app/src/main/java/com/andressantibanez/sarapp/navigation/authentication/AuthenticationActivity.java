package com.andressantibanez.sarapp.navigation.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.navigation.invoiceslist.InvoicesListActivity;
import com.andressantibanez.sarapp.navigation.invoiceview.InvoiceViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity {

    @Bind(R.id.fragment_container) FrameLayout mFragmentContainer;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    public static Intent launchIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

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
    }

    public void showLoginView() {
        getSupportFragmentManager().beginTransaction()
                .replace(mFragmentContainer.getId(), LoginFragment.newInstance())
                .commit();
    }

}
