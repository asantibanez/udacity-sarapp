package com.andressantibanez.sarapp.navigation.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.andressantibanez.sarapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity {

    @Bind(R.id.fragment_container) FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

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
