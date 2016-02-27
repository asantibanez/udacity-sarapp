package com.andressantibanez.sarapp;

import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.ExpenseSummaryResponse;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoiceInfoResponse;
import com.andressantibanez.sarapp.endpoints.dtos.GetInvoicesResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UploadInvoiceFileResponse;
import com.andressantibanez.sarapp.endpoints.dtos.LoginRequest;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;
import com.andressantibanez.sarapp.endpoints.dtos.UploadedInvoiceFileToInvoiceResponse;
import com.andressantibanez.sarapp.testing.TestHelper;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class SarappWebServiceTests {

    @Test
    public void validLoginToken() {
        assertThat(TestHelper.getValidLoginToken().length() > 0, is(true));
    }

    @Test
    public void validLogin() {
        LoginRequest loginRequest = new LoginRequest(TestHelper.VALID_EMAIL, TestHelper.VALID_PASSWORD);
        LoginResponse loginResponse = SarappWebService.create().login(loginRequest);
        assertThat(loginResponse.token.length() > 0, is(true));
        assertThat(loginResponse.errors.size() == 0, is(true));
    }

    @Test
    public void invalidLogin() {
        LoginRequest loginRequest = new LoginRequest(TestHelper.INVALID_EMAIL, TestHelper.INVALID_PASSWORD);
        LoginResponse loginResponse = SarappWebService.create().login(loginRequest);
        assertThat(loginResponse.token.length(), is(0));
        assertThat(loginResponse.errors.size() > 0, is(true));
    }

    @Test
    public void getInvoices() {
        GetInvoicesResponse getInvoicesResponse =
                SarappWebService.create().getInvoices(TestHelper.getValidLoginToken());
        assertThat(getInvoicesResponse.invoices.size() >= 0, is(true));
        assertThat(getInvoicesResponse.errors.size() == 0, is(true));

        for (GetInvoicesResponse.Invoice invoice : getInvoicesResponse.invoices) {
            Assert.assertThat(invoice.id.length() > 0, is(true));
        }
    }

    @Test
    public void getInvoiceInfo() {
        GetInvoicesResponse getInvoicesResponse =
                SarappWebService.create().getInvoices(TestHelper.getValidLoginToken());

        GetInvoiceInfoResponse getInvoiceInfoResponse;
        getInvoiceInfoResponse = SarappWebService.create().getInvoiceInfo(
                TestHelper.getValidLoginToken(),
                getInvoicesResponse.invoices.get(0).id
        );

        Assert.assertThat(getInvoiceInfoResponse.id, notNullValue());
        Assert.assertThat(getInvoiceInfoResponse.hasDetails(), is(true));
        Assert.assertThat(getInvoiceInfoResponse.hasErrors(), is(false));

        for (GetInvoiceInfoResponse.InvoiceDetail detail :
                getInvoiceInfoResponse.details) {
            Assert.assertThat(detail.id, notNullValue());
        }
    }

    @Test
    public void getExpenseSummary() {
        ExpenseSummaryResponse expenseSummary = SarappWebService.create().getExpenseSummary(TestHelper.getValidLoginToken(), 2015);
        assertThat(expenseSummary, notNullValue());
        assertThat(expenseSummary.hasErrors(), is(false));
        assertThat(expenseSummary.expenseTypes, notNullValue());
        assertThat(expenseSummary.expenseTypes.feeding, notNullValue());
        assertThat(expenseSummary.expenseTypes.health, notNullValue());
        assertThat(expenseSummary.expenseTypes.clothing, notNullValue());
        assertThat(expenseSummary.expenseTypes.dwelling, notNullValue());
        assertThat(expenseSummary.expenseTypes.education, notNullValue());
    }

    @Test
    public void uploadInvoiceFile() {
        String filePath = TestHelper.getAbsouluteFilePathForTestResource(
                this, "res/test_invoice_file.xml"
        );

        UploadInvoiceFileResponse uploadInvoiceFileResponse;
        uploadInvoiceFileResponse = SarappWebService.create().uploadInvoceFile(
                TestHelper.getValidLoginToken(), filePath
        );

        Assert.assertThat(uploadInvoiceFileResponse, notNullValue());
        Assert.assertThat(uploadInvoiceFileResponse.hasErrors(), is(false));
        Assert.assertThat(uploadInvoiceFileResponse.electronicInvoiceId.length() > 0, is(true));

    }

    @Test
    public void uploadedInvoiceFileToInvoice() {
        String electronicInvoiceId = TestHelper.uploadInvoiceFileAndGetId(this);
        Assert.assertThat(electronicInvoiceId, notNullValue());
        Assert.assertThat(electronicInvoiceId.length() > 0, is(true));

        UploadedInvoiceFileToInvoiceResponse uploadedInvoiceFileToInvoiceResponse =
                SarappWebService.create().uploadedInvoiceFileToInvoice(
                        TestHelper.getValidLoginToken(), electronicInvoiceId
                );

        Assert.assertThat(uploadedInvoiceFileToInvoiceResponse, notNullValue());
        Assert.assertThat(uploadedInvoiceFileToInvoiceResponse.hasErrors(), is(true));
    }

}