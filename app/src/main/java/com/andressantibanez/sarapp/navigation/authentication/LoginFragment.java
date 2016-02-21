package com.andressantibanez.sarapp.navigation.authentication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.LoginRequest;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;
import com.andressantibanez.sarapp.navigation.invoiceslist.InvoicesListActivity;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @Bind(R.id.main_content) CoordinatorLayout mMainContentView;
    @Bind(R.id.email) EditText mEmailView;
    @Bind(R.id.password) EditText mPasswordView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.login_button) Button mLoginButtonView;
    @Bind(R.id.help) TextView mHelpView;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_login, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.valid_credentials) {
            mEmailView.setText(R.string.valid_email);
            mPasswordView.setText(R.string.valid_password);
            return true;
        }

        if(item.getItemId() == R.id.invalid_credentials) {
            mEmailView.setText(R.string.invalid_email);
            mPasswordView.setText(R.string.invalid_password);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.help)
    public void goToRegistration() {
        ((AuthenticationActivity)getActivity()).showRegistrationView();
    }

    @OnClick(R.id.login_button)
    public void login() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mProgressBar.setVisibility(View.VISIBLE);
        new LoginTask().execute(new LoginRequest(email, password));
    }


    public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

        @Override
        protected LoginResponse doInBackground(LoginRequest... loginRequests) {
            return SarappWebService.create().login(loginRequests[0]);
        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);

            mProgressBar.setVisibility(View.GONE);

            if(loginResponse.hasErrors()) {
                String errors = TextUtils.join("\n", loginResponse.errors);
                Snackbar.make(mMainContentView, errors, Snackbar.LENGTH_LONG).show();
            } else{
                Sarapp.instance().setToken(loginResponse.token);
                startActivity(InvoicesListActivity.launchIntent(getContext()));
                getActivity().finish();
            }
        }

    }
}
