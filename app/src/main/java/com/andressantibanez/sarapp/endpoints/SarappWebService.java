package com.andressantibanez.sarapp.endpoints;

import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.endpoints.dtos.LoginRequest;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asantibanez on 2/20/16.
 */
public class SarappWebService {

    private static final String API_BASE_URL = "http://52.2.92.14/";

    private SarappWebApi service;

    public SarappWebService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(SarappWebApi.class);
    }

    public static SarappWebService create() {
        return new SarappWebService();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse;
        Response<LoginResponse> response;

        String email = loginRequest.email;
        String password = loginRequest.password;

        try {
            response = service.login(email, password).execute();

            if(response.code() == 200)
                loginResponse = response.body();
            else
                loginResponse = new Gson().fromJson(response.errorBody().string(), LoginResponse.class);

        } catch (IOException e) {
            loginResponse = new LoginResponse();
            loginResponse.errors.add("Error while contacting server. Please try again");
        }

        return loginResponse;
    }

    public GetInvoicesResponse getInvoices(String token) {
        GetInvoicesResponse getInvoicesResponse;
        Response<GetInvoicesResponse> response;

        try {
            response = service.getInvoices(token).execute();

            if(response.code() == 200)
                getInvoicesResponse = response.body();
            else
                getInvoicesResponse = new Gson().fromJson(response.errorBody().string(), GetInvoicesResponse.class);

        } catch (IOException e) {
            getInvoicesResponse = new GetInvoicesResponse();
            getInvoicesResponse.errors.add("Error while contacting server. Please try again");
        }

        return getInvoicesResponse;
    }

}
