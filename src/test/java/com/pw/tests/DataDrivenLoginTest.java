package com.pw.tests;
import io.qameta.allure.Allure;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import com.pw.pages.InventoryPage;
import com.pw.pages.LoginPage;
import com.pw.testdata.LoginTestData;
import com.pw.utils.BaseTest;
import com.pw.utils.DataProviderUtil;

public class DataDrivenLoginTest extends BaseTest {


    
    @Test(groups = "regression",dataProvider = "loginTestDataExpErrMsg", dataProviderClass = DataProviderUtil.class,   description = "Validate login behavior with external JSON data")
    public void verifyLogin(HashMap<String,String> data) {
        //  LoginPage loginPage = new LoginPage(page,sauceUrl);
        //  loginPage.open();
        assertThat(page).hasTitle("Swag Labs");
         InventoryPage inventoryPage=loginPage.login(data.get("username"), data.get("password"));
        if (data.get("expectedError").isEmpty()) {
            Allure.step("Verify inventory page heading is visible", () -> {
                assertThat(inventoryPage.heading()).isVisible();            });
        } else {
            Allure.step("Verify login error message is displayed", () -> {
                assertThat(loginPage.errorMessage()).hasText(data.get("expectedError"));            });
            String actualError = loginPage.errorMessage().textContent();
            System.out.println("Login error for user '" + data.get("username") + "': " + actualError);
            Allure.step("Displayed error message: " + actualError);
        }
    }

   @Test(groups = "regression",dataProvider = "userlogin", dataProviderClass = DataProviderUtil.class,description = "Validate login behavior with dataset")
    public void verifyLoginWithMultipleData(Map<String, String> testData) {
        assertThat(page).hasTitle("Swag Labs");
        loginPage.login(testData.get("username"), testData.get("password"));
    }

}