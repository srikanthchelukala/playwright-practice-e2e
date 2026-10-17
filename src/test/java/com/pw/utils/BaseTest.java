package com.pw.utils;

import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.pw.pages.LoginPage;

import io.qameta.allure.Allure;

public abstract class BaseTest{
    private static final DateTimeFormatter SCREENSHOT_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    protected Page page;
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected LoginPage loginPage;
    protected String sauceUrl;

    // @BeforeMethod
    /*public void setUp() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        loginPage = new LoginPage(page);
        loginPage.open();
    }*/

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws FileNotFoundException,IOException{
        Properties prop=new Properties();
        FileInputStream fis=new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
        // String browserName=prop.getProperty("browser");
        //mvn test -PRegression -Dbrowser=chrome
        String browserName=System.getProperty("browser")!=null ? System.getProperty("browser") :prop.getProperty("browser");
                ///if condition exec then after ? mark consition will run and if no then : condition will run
        String envName=System.getProperty("env")!=null ?System.getProperty("env") : prop.getProperty("env");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless") != null ? System.getProperty("headless") : prop.getProperty("headless"));
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        if(browserName.equalsIgnoreCase("firefox")) {
            // browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
              browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        } else if(browserName.equalsIgnoreCase("chrome")) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        } else if(browserName.equalsIgnoreCase("webkit")) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        }
        sauceUrl=prop.getProperty(envName+"."+"sauceUrl");
        System.out.println("Browser : " + browserName);
        System.out.println("Environment : " + envName);
        System.out.println("URL : " + sauceUrl);
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));
        page = context.newPage();
        loginPage = new LoginPage(page, sauceUrl);
        loginPage.open();
        // LoginPage loginPage = new LoginPage(page,sauceUrl);
        //  loginPage.open();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        context.tracing().stop(new Tracing.StopOptions()
        .setPath(Paths.get("trace.zip")));
        if (!result.isSuccess() && page != null) {
            captureFailureScreenshot(result);
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private void captureFailureScreenshot(ITestResult result) {
        String fileName = result.getTestClass().getRealClass().getSimpleName()
                + "-" + result.getMethod().getMethodName()
                + "-" + LocalDateTime.now().format(SCREENSHOT_TIMESTAMP)
                + ".png";
        Path screenshotPath = Paths.get("target", "reports", "screenshots", fileName);

        try {
            Files.createDirectories(screenshotPath.getParent());
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));
            Allure.addAttachment("Failure screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
            System.out.println("Failure screenshot: " + screenshotPath.toAbsolutePath());
        } catch (IOException | RuntimeException exception) {
            System.err.println("Unable to capture failure screenshot: " + exception.getMessage());
        }
    }
}