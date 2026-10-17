package com.pw.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import io.qameta.allure.Step;

public class InventoryPage {
    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    public Locator heading() {
        return page.getByText("Products", new Page.GetByTextOptions().setExact(true));
    }

    @Step("Add product '{productTestId}' to cart")
    public void addProduct(String productTestId) {
        page.getByTestId(productTestId).click();
    }

    @Step("Add first available product to cart")
    public void addFirstAvailableProduct() {
        page.locator(".btn.btn_primary.btn_small.btn_inventory").first().click();
    }

    @Step("Open shopping cart")
    public CartPage openCart() {
        page.getByTestId("shopping-cart-link").click();
        return new CartPage(page);
    }
}