package com.pw.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.Locator;

import io.qameta.allure.Step;

public class LoginPage {
    // private static final String URL = "ht/tps://www.saucedemo.com/";
    private static final String placeholderUsername = "Username";
    private static final String placeholderPassword = "Password";
    private final Page page;
    String sauceUrl;

    public LoginPage(Page page,String sauceUrl) {
        this.page = page;   //this param constructor now accepts sauceUrl as well
        this.sauceUrl = sauceUrl;
    }

    @Step("Open SauceDemo login page {sauceUrl}")
    public void open() {
        page.navigate(sauceUrl);
    }

    /*@Step("Login with username '{username}'") 
    public InventoryPage login(String username, String password) {
        page.getByPlaceholder("Username").fill(username);
        page.getByPlaceholder("Password").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        return new InventoryPage(page);
    }*/
   
   /*@Step("Login with username '{username}' and password '{password}' and click Login button")  ------WITHOUT RETURNING NEXT PAGE 
    public void login(String username, String password) {
        page.getByPlaceholder("Username").fill(username);
        page.getByPlaceholder("Password").fill(password);
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Login")
    ).click();
}*/
   @Step("Login with username '{username}' and password '{password}' and click Login button")
    public InventoryPage login(String username, String password) {
        page.getByPlaceholder(placeholderUsername).fill(username);
        page.getByPlaceholder(placeholderPassword).fill(password);
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Login")
    ).click();
    InventoryPage inventoryPage = new InventoryPage(page);
    return inventoryPage;//Returning the object of next page what user will interact with after login
}


    public Locator errorMessage() {
        return page.getByTestId("error");
    }
}