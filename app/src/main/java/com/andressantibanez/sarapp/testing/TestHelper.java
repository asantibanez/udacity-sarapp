package com.andressantibanez.sarapp.testing;

import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.endpoints.dtos.LoginRequest;
import com.andressantibanez.sarapp.endpoints.dtos.UploadInvoiceFileResponse;

import java.net.URL;

/**
 * Created by asantibanez on 2/24/16.
 */
public class TestHelper {

    public static String VALID_EMAIL = "demo@sarapp.com";
    public static String VALID_PASSWORD = "demodemo";

    public static String INVALID_EMAIL = "invalid@sarapp.com";
    public static String INVALID_PASSWORD = "invalid";

    public static String getValidLoginToken() {
        LoginRequest loginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        return SarappWebService.create().login(loginRequest).token;
    }

    public static GetInvoicesResponse.Invoice getValidInvoice() {
        return SarappWebService.create()
                .getInvoices(TestHelper.getValidLoginToken())
                .invoices.get(0)
        ;
    }

    public static String uploadInvoiceFileAndGetId(Object object, String fileName) {
        //String fileName = "res/test_invoice_file.xml";
//        String fileName = "res/test_invalid_file.xml";
        String filePath = TestHelper.getAbsouluteFilePathForTestResource(
                object, fileName
        );

        UploadInvoiceFileResponse uploadInvoiceFileResponse;
        uploadInvoiceFileResponse = SarappWebService.create().uploadInvoceFile(
                TestHelper.getValidLoginToken(), filePath
        );

        return uploadInvoiceFileResponse.electronicInvoiceId;
    }

    public static String getAbsouluteFilePathForTestResource(Object object, String fileName) {
        ClassLoader classLoader = object.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

}
