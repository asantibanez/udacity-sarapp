package com.andressantibanez.sarapp;

import com.andressantibanez.sarapp.endpoints.SarappWebService;
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
        LoginResponse loginResponse;
        loginResponse = SarappWebService.create().login("santibanez.andres@gmail.com", "orellana");
        assertThat(loginResponse.token.length() > 0, is(true));
        assertThat(loginResponse.errors.size() == 0, is(true));
    }

    @Test
    public void invalidLogin() {
        LoginResponse loginResponse;
        loginResponse = SarappWebService.create().login("invalid@email.com", "hello");
        assertThat(loginResponse.token.length(), is(0));
        assertThat(loginResponse.errors.size() > 0, is(true));
    }

}