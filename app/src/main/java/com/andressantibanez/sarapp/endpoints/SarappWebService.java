package com.andressantibanez.sarapp.endpoints;

import com.andressantibanez.sarapp.endpoints.dtos.ExpenseSummaryResponse;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoiceInfoResponse;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.endpoints.dtos.RegistrationRequest;
import com.andressantibanez.sarapp.endpoints.dtos.RegistrationResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UploadInvoiceFileResponse;
import com.andressantibanez.sarapp.endpoints.dtos.LoginRequest;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UpdateDetailExpenseTypeRequest;
import com.andressantibanez.sarapp.endpoints.dtos.UpdateDetailExpenseTypeResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UploadedInvoiceFileToInvoiceResponse;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asantibanez on 2/20/16.
 */
public class SarappWebService {

    private static final String API_BASE_URL = "http://54.164.87.92/";

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

    public RegistrationResponse register(RegistrationRequest registrationRequest) {
        RegistrationResponse registrationResponse;
        Response<RegistrationResponse> response;

        String email = registrationRequest.email;
        String password = registrationRequest.password;
        String firstName = registrationRequest.firstName;
        String lastName = registrationRequest.lastName;

        try {
            response = service.register(email, password, firstName, lastName).execute();

            if(response.code() == 200)
                registrationResponse = response.body();
            else
                registrationResponse = new Gson().fromJson(response.errorBody().string(), RegistrationResponse.class);

        } catch (IOException e) {
            registrationResponse = new RegistrationResponse();
            registrationResponse.errors.add("Error while contacting server. Please try again");
        }

        return registrationResponse;
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

        int year = Calendar.getInstance().get(Calendar.YEAR);

        try {
            response = service.getInvoices(token, year).execute();

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

    public GetInvoiceInfoResponse getInvoiceInfo(String token, String invoiceId) {
        GetInvoiceInfoResponse getInvoiceInfoResponse;
        Response<GetInvoiceInfoResponse> response;

        try {
            response = service.getInvoiceInfo(invoiceId, token).execute();

            if(response.code() == 200)
                getInvoiceInfoResponse = response.body();
            else
                getInvoiceInfoResponse = new Gson().fromJson(response.errorBody().string(), GetInvoiceInfoResponse.class);

        } catch (IOException e) {
            getInvoiceInfoResponse = new GetInvoiceInfoResponse();
            getInvoiceInfoResponse.errors.add("Error while contacting server. Please try again");
        }

        return getInvoiceInfoResponse;
    }

    public UpdateDetailExpenseTypeResponse updateDetailExpenseType(String token, String invoiceId, String invoiceDetailId, String expenseType) {
        UpdateDetailExpenseTypeResponse updateDetailExpenseTypeResponse;
        Response<UpdateDetailExpenseTypeResponse> response;

        try {
            response = service.updateInvoiceDetailExpenseType(
                    invoiceId,
                    invoiceDetailId,
                    new UpdateDetailExpenseTypeRequest(expenseType),
                    token
            ).execute();

            if(response.code() == 200)
                updateDetailExpenseTypeResponse = response.body();
            else
                updateDetailExpenseTypeResponse = new Gson().fromJson(
                        response.errorBody().string(),
                        UpdateDetailExpenseTypeResponse.class
                );

        } catch (IOException e) {
            updateDetailExpenseTypeResponse = new UpdateDetailExpenseTypeResponse();
            updateDetailExpenseTypeResponse.errors.add("Error while contacting server. Please try again");
        }

        return updateDetailExpenseTypeResponse;
    }

    public ExpenseSummaryResponse getExpenseSummary(String token, int year) {
        ExpenseSummaryResponse expenseSummaryResponse;
        Response<ExpenseSummaryResponse> response;

        try {
            response = service.getExpenseSummary(year, token).execute();

            if(response.code() == 200)
                expenseSummaryResponse = response.body();
            else
                expenseSummaryResponse = new Gson().fromJson(
                        response.errorBody().string(),
                        ExpenseSummaryResponse.class
                );

        } catch (IOException e) {
            expenseSummaryResponse = new ExpenseSummaryResponse();
            expenseSummaryResponse.errors.add("Error while contacting server. Please try again");
        }

        return expenseSummaryResponse;
    }

    public UploadInvoiceFileResponse uploadInvoceFile(String token, String filePath) {
        System.out.println("File path to upload: " + filePath);

        RequestBody xmlFileRequestBody = RequestBody.create(MediaType.parse("text/xml"), new File(filePath));
        Map<String, RequestBody> files = new HashMap<>();
        files.put("files[0]\"; filename=\"invoice.xml\" ", xmlFileRequestBody);

        UploadInvoiceFileResponse uploadInvoiceFileResponse;
        Response<UploadInvoiceFileResponse> response;
        try {
            response = service.uploadInvoiceFile(files, token).execute();

            if(response.code() == 200)
                uploadInvoiceFileResponse = response.body();
            else
                uploadInvoiceFileResponse = new Gson().fromJson(
                        response.errorBody().string(),
                        UploadInvoiceFileResponse.class
                );
        } catch (IOException e) {
            e.printStackTrace();

            uploadInvoiceFileResponse = new UploadInvoiceFileResponse();
            uploadInvoiceFileResponse.errors.add("Error while contacting server. Please try again");
        }

        return uploadInvoiceFileResponse;
    }

    public UploadedInvoiceFileToInvoiceResponse uploadedInvoiceFileToInvoice(String token, String electronicInvoiceId) {
        UploadedInvoiceFileToInvoiceResponse uploadedInvoiceFileToInvoiceResponse;
        Response<UploadedInvoiceFileToInvoiceResponse> response;

        try {
            response = service.uploadedInvoiceFileToInvoice(electronicInvoiceId, token).execute();

            if(response.code() == 200)
                uploadedInvoiceFileToInvoiceResponse = response.body();
            else
                uploadedInvoiceFileToInvoiceResponse = new Gson().fromJson(
                        response.errorBody().string(),
                        UploadedInvoiceFileToInvoiceResponse.class
                );

        } catch (IOException e) {
            uploadedInvoiceFileToInvoiceResponse = new UploadedInvoiceFileToInvoiceResponse();
            uploadedInvoiceFileToInvoiceResponse.errors.add("Error while contacting server. Please try again");
        }

        return uploadedInvoiceFileToInvoiceResponse;
    }

}
