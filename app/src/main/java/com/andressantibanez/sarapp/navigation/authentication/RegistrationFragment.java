package com.andressantibanez.sarapp.navigation.authentication;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.RegistrationRequest;
import com.andressantibanez.sarapp.endpoints.dtos.RegistrationResponse;
import com.andressantibanez.sarapp.navigation.invoiceslist.InvoicesListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment extends Fragment {

    @Bind(R.id.root) View mRootView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBarView;
    @Bind(R.id.email) TextView mEmailView;
    @Bind(R.id.password) TextView mPasswordView;
    @Bind(R.id.first_name) TextView mFirstNameView;
    @Bind(R.id.last_name) TextView mLastNameView;
    @Bind(R.id.help) TextView mHelpView;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    public RegistrationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.registration_button)
    public void register() {
        mProgressBarView.setVisibility(View.VISIBLE);

        RegistrationRequest registrationRequest = new RegistrationRequest(
                mEmailView.getText().toString(),
                mPasswordView.getText().toString(),
                mFirstNameView.getText().toString(),
                mLastNameView.getText().toString()
        );
        new RegistrationTask().execute(registrationRequest);
    }

    @OnClick(R.id.help)
    public void goToLogin() {
        ((AuthenticationActivity) getActivity()).showLoginView();
    }


    public class RegistrationTask extends AsyncTask<RegistrationRequest, Void, RegistrationResponse> {

        @Override
        protected RegistrationResponse doInBackground(RegistrationRequest... registrationRequests) {
            return SarappWebService.create().register(registrationRequests[0]);
        }

        @Override
        protected void onPostExecute(RegistrationResponse registrationResponse) {
            mProgressBarView.setVisibility(View.GONE);

            if(registrationResponse.hasErrors()) {
                String errors = TextUtils.join("\n", registrationResponse.errors);
                Snackbar.make(mRootView, errors, Snackbar.LENGTH_LONG).show();
            } else{
                Sarapp.instance().setToken(registrationResponse.token);
                startActivity(InvoicesListActivity.launchIntent(getContext()));
                getActivity().finish();
            }
        }
    }
}
