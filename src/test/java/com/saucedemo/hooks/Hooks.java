package com.saucedemo.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.saucedemo.factory.DriverFactory;
import java.io.ByteArrayInputStream;

public class Hooks {

    @Before
    public void setUp() {
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && DriverFactory.getDriver() != null) {
            byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            // Attach to Cucumber report
            scenario.attach(screenshot, "image/png", "Failed Screenshot");
            // Attach to Allure report
            Allure.addAttachment("Screenshot on Failure",
                    new ByteArrayInputStream(screenshot));
        }
        DriverFactory.quitDriver();
    }
}
