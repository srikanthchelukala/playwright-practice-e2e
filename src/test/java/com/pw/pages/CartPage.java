package com.pw.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public Locator items() {
        return page.locator(".cart_item");
    }

    @Step("Proceed to checkout")
    public CheckoutPage checkout() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Checkout")).click();
        return new CheckoutPage(page);
    }
}