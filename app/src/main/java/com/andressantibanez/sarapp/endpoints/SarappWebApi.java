package com.andressantibanez.sarapp.endpoints;

import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by asantibanez on 2/20/16.
 */
public interface SarappWebApi {

    @FormUrlEncoded
    @POST("api/users/authenticate")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

}
