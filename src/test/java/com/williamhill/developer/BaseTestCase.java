package com.williamhill.developer;

import com.williamhill.developer.api.client.ApiContext;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

public class BaseTestCase {

    final ApiContext apiContext = new ApiContext();

    @BeforeMethod
    public void setUp() throws Exception {
        RestAssured.baseURI = apiContext.BASE_URL;
        RestAssured.basePath = apiContext.BASE_PATH;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
