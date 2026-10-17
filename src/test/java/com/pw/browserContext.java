package com.pw;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
public class browserContext {
    Playwright playwright;
    Browser browser;
    Page page;
    Page pageA;
    Page pageB;
    
    @Test(description = "Test browser context functionality")
    public void testBrowserContext() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(); 
        // page = browser.newPage();
        BrowserContext contextA=browser.newContext();
        BrowserContext contextB=browser.newContext();
        pageA=contextA.newPage();
        pageB=contextB.newPage();
    }
    
}
