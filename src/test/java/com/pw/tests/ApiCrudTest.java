package com.pw.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class ApiCrudTest {
    private static final String BASE_URL = "https://reqres.in/api";
    private static final String KNOWN_USER_ID = "2";
    private   String JOB;
    private   String userID;

    private Playwright playwright;
    private APIRequestContext apiRequest;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        apiRequest = playwright.request().newContext(
                new APIRequest.NewContextOptions().setIgnoreHTTPSErrors(true));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (apiRequest != null) {
            apiRequest.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test(description = "Create a user and validate the response")
    public void createUser() {
        Map<String, String> payload = new HashMap<>();
        payload.put("name", "morpheus");
        payload.put("job", "leader");

        APIResponse response = post("/users", payload);
        JOB = JsonPath.read(response.text(), "$.job");
        userID=JsonPath.read(response.text(), "$.id");
        assertEquals(response.status(), 201, response.text());
        assertEquals(JsonPath.read(response.text(), "$.name"), "morpheus");
        assertEquals(JsonPath.read(response.text(), "$.job"), "leader");
        assertNotNull(JsonPath.read(response.text(), "$.id"));
        assertNotNull(JsonPath.read(response.text(), "$.createdAt"));
    }


     @Test(description = "Get a user and verify user details",dependsOnMethods = "deleteUser")
    public void getUserAfterDeleteUser() {
        APIResponse response = get("/users/" + userID);
        assertEquals(response.status(), 404, response.text()); // Expecting 404 after deletion
    }

    @Test(description = "Update a user and verify the updated data",dependsOnMethods = "createUser")
    public void updateUser() {
        Map<String, String> payload = new HashMap<>();
        payload.put("name", "morpheus");
        payload.put("job", JOB);

        APIResponse response = put("/users/" + userID, payload);
        Allure.step("Updating user with ID " + userID + " Payload " + payload);
        Allure.step("Response " +response.body());
        assertEquals(response.status(), 200, response.text());
        assertEquals(JsonPath.read(response.text(), "$.name"), "morpheus");
        assertEquals(JsonPath.read(response.text(), "$.job"), JOB);
        assertNotNull(JsonPath.read(response.text(), "$.updatedAt"));
    }

    @Test(description = "Delete a user and verify successful deletion",dependsOnMethods = "updateUser")
    public void deleteUser() {
        APIResponse response = delete("/users/" + userID);
        Allure.step("Deleting user with ID " + userID);
        assertEquals(response.status(), 204, response.text());
        assertTrue(response.text().isEmpty(), "Expected an empty response body");
    }

    @Step("Baseurl {BASE_URL} | POST {path} | Payload {payload} | Response {response.text()}")
    private APIResponse post(String path, Object payload) {
        return send("POST", path, apiRequest.post(BASE_URL + path,
                RequestOptions.create().setData(payload)));
    }

    @Step("Baseurl {BASE_URL} | GET {path} | Response {response.text()}")
    private APIResponse get(String path) {
        return send("GET", path, apiRequest.get(BASE_URL + path));
    }

    @Step("Baseurl {BASE_URL} | PUT {path} | Payload {payload} | Response {response.text()}")
    private APIResponse put(String path, Object payload) {
        return send("PUT", path, apiRequest.put(BASE_URL + path,
                RequestOptions.create().setData(payload)));
    }

    @Step("Baseurl {BASE_URL} | DELETE {path} | Response {response.text()}")
    private APIResponse delete(String path) {
        return send("DELETE", path, apiRequest.delete(BASE_URL + path));
    }

    private APIResponse send(String method, String path, APIResponse response) {
        String responseText = response.text();
        String log = method + " " + BASE_URL + path
                + "\nStatus: " + response.status()
                + "\nResponse:\n" + responseText;
        System.out.println(log);
        Allure.addAttachment(method + " " + path, "text/plain", log, ".txt");
        return response;
    }
}