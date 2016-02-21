package com.andressantibanez.sarapp;

import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.endpoints.dtos.LoginRequest;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class SarappWebServiceTests {

    String validEmail = "demo@gmail.com";
    String validPassword = "demodemo";

    public String getValidLoginToken() {
        LoginRequest loginRequest = new LoginRequest(validEmail, validPassword);
        return SarappWebService.create().login(loginRequest).token;
    }

    @Test
    public void validLoginToken() {
        assertThat(getValidLoginToken().length() > 0, is(true));
    }

    @Test
    public void validLogin() {
        LoginRequest loginRequest = new LoginRequest(validEmail, validPassword);
        LoginResponse loginResponse = SarappWebService.create().login(loginRequest);
        assertThat(loginResponse.token.length() > 0, is(true));
        assertThat(loginResponse.errors.size() == 0, is(true));
    }

    @Test
    public void invalidLogin() {
        LoginRequest loginRequest = new LoginRequest("invalid@gmail.com", "helloworld");
        LoginResponse loginResponse = SarappWebService.create().login(loginRequest);
        assertThat(loginResponse.token.length(), is(0));
        assertThat(loginResponse.errors.size() > 0, is(true));
    }

    @Test
    public void getInvoices() {
        GetInvoicesResponse getInvoicesResponse = SarappWebService.create().getInvoices(getValidLoginToken());
        assertThat(getInvoicesResponse.invoices.size() >= 0, is(true));
        assertThat(getInvoicesResponse.errors.size() == 0, is(true));

        for (GetInvoicesResponse.Invoice invoice : getInvoicesResponse.invoices) {
            Assert.assertThat(invoice.id.length() > 0, is(true));
            Assert.assertThat(invoice.supplierName.length() > 0, is(true));
            Assert.assertThat(invoice.supplierTaxpayerId.length() > 0, is(true));
        }
    }

}