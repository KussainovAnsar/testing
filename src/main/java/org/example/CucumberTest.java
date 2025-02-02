package org.example;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "org.example.stepdefinitions",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class CucumberTest extends AbstractTestNGCucumberTests {
}
