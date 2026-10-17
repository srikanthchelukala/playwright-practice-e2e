package com.pw.tests;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import org.testng.annotations.Test;
import org.testng.Assert;
import com.jayway.jsonpath.JsonPath;
import java.util.HashMap;

public class APITest {
    @Test
    public void e2eApiTest(){
        HashMap<Object,Object> loginPayload=new HashMap<>();
        loginPayload.put("email", "eve.holt@reqres.in");
        loginPayload.put("password","pistol");
        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext(new APIRequest.NewContextOptions().setIgnoreHTTPSErrors(true));
        try {
            APIResponse apiResponse = apiRequest.post("https://reqres.in/api/register",
                    RequestOptions.create().setData(loginPayload));
                Assert.assertTrue(apiResponse.ok(),"Registration failed with status " + apiResponse.status() + ": " + apiResponse.text());
                String token=JsonPath.read(apiResponse.text(),"$.token");
                System.out.println("User created: "+ token);

        } finally {
            apiRequest.dispose();
            playwright.close();
        }

    }
    
}
