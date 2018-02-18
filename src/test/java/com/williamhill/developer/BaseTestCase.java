package com.williamhill.developer;

import com.williamhill.developer.apiclients.ApiContext;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

import static com.williamhill.developer.configuration.Configuration.CONFIGURATION;

public class BaseTestCase {

    public static final String BASE_URL = CONFIGURATION.getValue("baseUrl");
    public static final String BASE_PATH = CONFIGURATION.getValue("basePath");
    private ApiContext apiContext = new ApiContext();

    public ApiContext apiContext() {
        return apiContext;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = BASE_PATH;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
