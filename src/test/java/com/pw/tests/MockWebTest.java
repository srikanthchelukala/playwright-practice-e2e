package com.pw.tests;
import org.testng.annotations.Test;
import com.microsoft.playwright.options.AriaRole;

import org.testng.Assert;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.nio.file.Paths;
public class MockWebTest {

    @Test(description="Verify that the event cards are correctly displayed when the API is mocked")
    public void  routeFulfillTest(){
        Playwright playwright=Playwright.create();
        Browser browser=playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page=browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        page.getByLabel("Email").fill("reddy.csri@gmail.com");
        page.getByLabel("Password").fill("Shivaya9@");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
        // page.getByTestId("nav-bookings").click();
        page.route("**/api/events**",route -> route.fulfill(new Route.FulfillOptions().setPath(Paths.get("src\\test\\resources\\testdata\\events_4.json"))));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        page.waitForTimeout(7000);
        Locator eventCards=page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        Assert.assertEquals(eventCards.count(), 4);
        assertThat(page.locator(".mx-1").first()).isHidden();
    }

    @Test(description="Verify that access is denied when trying to view booking details without proper authorization")
    public void routeResumeTest(){
        Playwright playwright=Playwright.create();
        Browser browser=playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page=browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        page.getByLabel("Email").fill("reddy.csri@gmail.com");
        page.getByLabel("Password").fill("Shivaya9@");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
        page.getByTestId("nav-bookings").click();
        page.route("**/api/bookings**",route->route.resume(new Route.ResumeOptions().setUrl("https://eventhub.rahulshettyacademy.com/bookings/22307")));
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("View Details")).first().click();
        assertThat(page.getByText("Access Denied")).isVisible();
    }
    
}