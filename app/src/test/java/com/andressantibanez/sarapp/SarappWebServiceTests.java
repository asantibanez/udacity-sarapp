package com.andressantibanez.sarapp;

import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.LoginRequest;
import com.andressantibanez.sarapp.endpoints.dtos.LoginResponse;

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

    @Test
    public void validLogin() {
        LoginRequest loginRequest = new LoginRequest("demo@gmail.com", "demodemo");
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

}