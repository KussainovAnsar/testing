package org.example;
import io.restassured.RestAssured;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.Map;

import io.restassured.response.Response;

public class Main {
    public static final String USERNAME = "chocolatewithtea_WLEVOt";
    public static final String AUTOMATE_KEY = "paTe2VB2MSJB3MhMpzFf";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @Test
    public void testGetRequest() {
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        Response response =  RestAssured.get(url);
        // status code is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        String title = response.jsonPath().getString("title");
        Assert.assertNotNull(title, "Title should not be null");

        System.out.println("API Test Passed. Title: " + title);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.openTelemetry.enabled", "false");
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserName", "chrome");
        options.setCapability("browserVersion", "latest");
        options.setCapability("platformName", "Windows 10");

        options.setCapability("bstack:options", Map.of(
                "os", "Windows",
                "osVersion", "10",
                "sessionName", "MyTestSession"
        ));

        WebDriver driver = new RemoteWebDriver(new URL(URL), options);
        driver.get("https://www.google.com");
        System.out.println("Title: " + driver.getTitle());
        driver.quit();
    }
}