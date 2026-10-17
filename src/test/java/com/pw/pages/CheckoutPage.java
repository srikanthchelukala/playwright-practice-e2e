package com.pw.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class CheckoutPage {
    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    @Step("Enter customer details: {firstName} {lastName}, {postalCode}")
    public void enterCustomerDetails(String firstName, String lastName, String postalCode) {
        page.getByPlaceholder("First Name").fill(firstName);
        page.getByPlaceholder("Last Name").fill(lastName);
        page.getByPlaceholder("Zip/Postal Code").fill(postalCode);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
    }

    @Step("Read order total")
    public String totalAmount() {
        return page.getByTestId("total-label").textContent().replace("Total: $", "");
    }

    @Step("Finish order")
    public void finish() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Finish")).click();
    }

    public Locator completionHeader() {
        return page.getByTestId("complete-header");
    }
}