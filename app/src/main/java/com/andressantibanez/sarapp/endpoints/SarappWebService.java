package com.andressantibanez.sarapp.endpoints;

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

    public LoginResponse login(String email, String password) {
        LoginResponse data = null;
        Response<LoginResponse> response = null;
        try {
            response = service.login(email, password).execute();

            if(response.code() == 200)
                data = response.body();
            else
                data = new Gson().fromJson(response.errorBody().string(), LoginResponse.class);

        } catch (IOException e) {
            data = new LoginResponse();
            data.errors.add("Error al contactar servidor.");
        }

        return data;
    }

}
