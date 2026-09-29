package com.saucedemo.ci;

import com.saucedemo.factory.DriverFactory;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.*;

public class LoginCiSteps {
    private WebDriver driver() {
        return DriverFactory.getDriver();
    }

    private LoginPage loginPage() {
        return new LoginPage(driver());
    }

    @Given("I am on the SauceDemo login page")
    public void iAmOnTheSauceDemoLoginPage() {
        driver().get(ConfigReader.get("base.url"));
    }

    @When("I enter username {string}")
    public void iEnterUsername(String username) {
        loginPage().enterUsername(username);
    }

    @And("I enter password {string}")
    public void iEnterPassword(String password) {
        loginPage().enterPassword(password);
    }

    @And("I click the login button")
    public void iClickTheLoginButton() {
        loginPage().clickLogin();
    }

    @Then("I should be redirected to the products page")
    public void iShouldBeRedirectedToTheProductsPage() {
        assertTrue("User should be on inventory page", driver().getCurrentUrl().contains("inventory"));
    }

    @And("I should see {string} as the page title")
    public void iShouldSeeAsThePageTitle(String title) {
        assertEquals("Page title should match", title, driver().findElement(By.className("title")).getText());
    }

    @Then("I should see an error message")
    public void iShouldSeeAnErrorMessage() {
        assertTrue("Error message should be displayed", loginPage().isErrorDisplayed());
    }

    @And("the error should contain {string}")
    public void theErrorShouldContain(String errorText) {
        assertTrue("Error should contain: " + errorText, loginPage().getErrorMessage().contains(errorText));
    }

    @Given("I try to access the inventory page directly without login")
    public void iTryToAccessInventoryPageDirectlyWithoutLogin() {
        driver().get(ConfigReader.get("base.url") + "/inventory.html");
    }

    @Then("I should be redirected to the login page")
    public void iShouldBeRedirectedToTheLoginPage() {
        assertTrue("User should stay on login page", driver().getCurrentUrl().contains("saucedemo.com"));
    }

    @And("I should see an error message about authorization")
    public void iShouldSeeAnErrorMessageAboutAuthorization() {
        assertTrue("Authorization error should be displayed", loginPage().isErrorDisplayed());
    }
}

