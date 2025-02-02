package org.example;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class OlxTesting {
    private static final Logger log = LogManager.getLogger(OlxTesting.class);
    private WebDriver webDriver;
    private ExtentReports report;
    private ExtentTest testCase;

    @BeforeSuite
    public void initializeSuite() {
        log.info("Initializing the test suite.");

        ExtentSparkReporter reporter = new ExtentSparkReporter("generated-report.html");
        report = new ExtentReports();
        report.attachReporter(reporter);
        log.info("Report setup complete.");
    }

    @BeforeClass
    public void initializeDriver() {
        log.info("Initializing WebDriver.");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\.cache\\selenium\\chromedriver\\win64\\131.0.6778.204\\chromedriver.exe");
        webDriver = new ChromeDriver();
        log.info("WebDriver launched.");
    }

    @Test
    public void verifyOlxHomePageTitle() {
        log.info("Starting the test for assignment 5.");
        testCase = report.createTest("testOlxHomePage", "Validates OLX.kz homepage title.");
        webDriver.get("https://www.olx.kz/");
        log.info("Navigated to OLX.kz.");

        String pageTitle = webDriver.getTitle();
        log.info("Page Title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("Сервис объявлений OLX: сайт частных объявлений в Казахстане - купля/продажа б/у товаров на OLX.kz"), "Title mismatch.");
        log.info("Test completed successfully.");
    }

    @AfterMethod
    public void afterTestMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test failed: " + result.getName());
            log.error("Exception: " + result.getThrowable());
            testCase.fail("Test failure: " + result.getThrowable());

            // Capturing screenshot if test fails
            File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            String screenshotLocation = "failure-screenshots/" + result.getName() + ".png";
            try {
                FileUtils.copyFile(screenshot, new File(screenshotLocation));
                testCase.addScreenCaptureFromPath(screenshotLocation);
                log.info("Screenshot saved: " + screenshotLocation);
            } catch (IOException e) {
                log.error("Error saving screenshot: " + e.getMessage());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("Test passed: " + result.getName());
            testCase.pass("Test passed successfully.");
        }
    }

    @AfterClass
    public void cleanupAfterClass() {
        log.info("Shutting down WebDriver.");
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @AfterSuite
    public void finalizeReport() {
        log.info("Flushing final report.");
        if (report != null) {
            report.flush();
        }
    }
}
