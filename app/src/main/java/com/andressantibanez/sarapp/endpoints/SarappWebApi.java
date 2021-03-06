package com.andressantibanez.sarapp.endpoints;

import com.andressantibanez.sarapp.endpoints.dtos.ExpenseSummaryResponse;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoiceInfoResponse;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.endpoints.dtos.RegistrationResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UploadedInvoiceFileToInvoiceResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UploadInvoiceFileResponse;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UpdateDetailExpenseTypeRequest;
import com.andressantibanez.sarapp.endpoints.dtos.UpdateDetailExpenseTypeResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by asantibanez on 2/20/16.
 */
public interface SarappWebApi {

    @FormUrlEncoded
    @POST("api/users/authenticate")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/users/register")
    Call<RegistrationResponse> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName
    );

    @GET("api/invoices?page=1&records=2000&all_records=1")
    Call<GetInvoicesResponse> getInvoices(@Query("token") String token, @Query("year") int year);

    @GET("api/invoices/{invoice_id}")
    Call<GetInvoiceInfoResponse> getInvoiceInfo(@Path("invoice_id") String invoiceId, @Query("token") String token);

    @POST("api/invoices/{invoice_id}/details/{invoice_detail_id}/expense_type")
    Call<UpdateDetailExpenseTypeResponse> updateInvoiceDetailExpenseType(
            @Path("invoice_id") String invoiceId,
            @Path("invoice_detail_id") String invoiceDetailId,
            @Body UpdateDetailExpenseTypeRequest body,
            @Query("token") String token);

    @GET("api/summary")
    Call<ExpenseSummaryResponse> getExpenseSummary(@Query("year") int year, @Query("token") String token);

    @Multipart
    @POST("api/electronic-invoices/upload")
    Call<UploadInvoiceFileResponse> uploadInvoiceFile(@PartMap() Map<String, RequestBody> files, @Query("token") String token);

    @POST("api/electronic-invoices/{electronic_invoice_id}/to-invoice")
    Call<UploadedInvoiceFileToInvoiceResponse> uploadedInvoiceFileToInvoice(
            @Path("electronic_invoice_id") String electronicInvoiceId,
            @Query("token") String token
    );
}
