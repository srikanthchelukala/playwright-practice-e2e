package com.pw.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.HashMap;

import com.pw.pages.LoginPage;

import org.testng.annotations.Test;

import com.pw.pages.CartPage;
import com.pw.pages.CheckoutPage;
import com.pw.pages.InventoryPage;
import com.pw.utils.BaseTest;
import com.pw.utils.DataProviderUtil;

public class AutomationFundamentals extends BaseTest {
    
    @Test(dataProvider = "checkoutData", dataProviderClass = DataProviderUtil.class,description = "Verify successful login")
    public void verifySuccessfulLogin(HashMap<String, String> checkoutData) {
        // assertThat(page).hasTitle("Swag Labs");
        // InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        // LoginPage loginPage = new LoginPage(page,sauceUrl);
        // loginPage.open();
        assertThat(page).hasTitle("Swag Labs");
        // loginPage.login("standard_user", "secret_sauce");
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");
        // InventoryPage inventoryPage = new InventoryPage(page); ---retun this in loginPage class

        
        assertThat(inventoryPage.heading()).isVisible();
        inventoryPage.addProduct("add-to-cart-sauce-labs-bike-light");
        inventoryPage.addProduct("add-to-cart-sauce-labs-bolt-t-shirt");
        inventoryPage.addFirstAvailableProduct();

        CartPage cartPage = inventoryPage.openCart();
        assertThat(cartPage.items()).hasCount(3);

        CheckoutPage checkoutPage = cartPage.checkout();
        checkoutPage.enterCustomerDetails(checkoutData.get("firstname"), checkoutData.get("lastname"), checkoutData.get("postalcode"));
        System.out.println(checkoutPage.totalAmount());
        checkoutPage.finish();

        assertThat(checkoutPage.completionHeader()).hasText("Thank you for your order!");
    }
}