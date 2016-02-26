package com.andressantibanez.sarapp.endpoints;

import com.andressantibanez.sarapp.endpoints.dtos.GetInvoiceInfoResponse;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UpdateInvoiceDetailExpenseTypeRequest;
import com.andressantibanez.sarapp.endpoints.dtos.UpdateInvoiceDetailExpenseTypeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by asantibanez on 2/20/16.
 */
public interface SarappWebApi {

    @FormUrlEncoded
    @POST("api/users/authenticate")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @GET("api/invoices?page=1&records=2000&all_records=1")
    Call<GetInvoicesResponse> getInvoices(@Query("token") String token);

    @GET("api/invoices/{invoice_id}")
    Call<GetInvoiceInfoResponse> getInvoiceInfo(@Path("invoice_id") String invoiceId, @Query("token") String token);

    @POST("api/invoices/{invoice_id}/details/{invoice_detail_id}/expense_type")
    Call<UpdateInvoiceDetailExpenseTypeResponse> updateInvoiceDetailExpenseType(
            @Path("invoice_id") String invoiceId,
            @Path("invoice_detail_id") String invoiceDetailId,
            @Body UpdateInvoiceDetailExpenseTypeRequest body,
            @Query("token") String token);
}
