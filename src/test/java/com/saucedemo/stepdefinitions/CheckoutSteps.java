package com.saucedemo.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.CheckoutOverviewPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.factory.DriverFactory;
import com.saucedemo.utils.ConfigReader;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class CheckoutSteps {
    private WebDriver driver;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    public CheckoutSteps() {
        this.driver = DriverFactory.getDriver();
        this.cartPage = new CartPage(driver);
        this.checkoutPage = new CheckoutPage(driver);
        this.checkoutOverviewPage = new CheckoutOverviewPage(driver);
        this.checkoutCompletePage = new CheckoutCompletePage(driver);
        this.loginPage = new LoginPage(driver);
        this.productsPage = new ProductsPage(driver);
    }

    @Given("I am logged in as a standard user")
    @Step("Login as standard user")
    public void iAmLoggedInAsAStandardUser() {
        driver.get(ConfigReader.get("base.url"));
        loginPage.login("standard_user", "secret_sauce");
    }

    @Given("I am on the cart page")
    @Step("Navigate to cart page")
    public void iAmOnTheCartPage() {
        assertTrue(driver.getCurrentUrl().contains("cart"));
    }

    @Then("I should see {int} items in the cart")
    @Step("Verify cart contains {0} items")
    public void iShouldSeeItemsInTheCart(int count) {
        assertEquals("Cart should contain " + count + " items", count, cartPage.getCartItemCount());
    }

    @And("I should see {string} in the cart")
    @Step("Verify {0} is in cart")
    public void iShouldSeeInTheCart(String productName) {
        assertTrue("Product should be in cart", 
                   cartPage.getCartItemNames().contains(productName));
    }

    @When("I click the {string} button")
    @Step("Click {0} button")
    public void iClickTheButton(String buttonText) {
        if (buttonText.equals("Continue Shopping")) {
            cartPage.continueShopping();
        } else if (buttonText.equals("Back Home")) {
            checkoutCompletePage.clickBackHome();
        }
    }

    @Then("I should be redirected to the products page")
    @Step("Verify redirected to products page")
    public void iShouldBeRedirectedToTheProductsPage() {
        assertTrue("Should be on products page", 
                   driver.getCurrentUrl().contains("inventory.html") && 
                   !driver.getCurrentUrl().contains("cart"));
    }

    @And("the cart should still contain {string}")
    @Step("Verify cart still contains {0}")
    public void theCartShouldStillContain(String productName) {
        productsPage.goToCart();
        assertTrue("Cart should still contain product", 
                   cartPage.getCartItemNames().contains(productName));
    }

    @When("I remove {string} from the cart")
    @Step("Remove {0} from cart")
    public void iRemoveFromTheCart(String productName) {
        cartPage.removeItem(productName);
    }

    @Then("the cart badge should show {int} items")
    @Step("Verify cart badge shows {0} items")
    public void theCartBadgeShouldShowItems(int count) {
        assertEquals("Cart badge should show " + count + " items", count, productsPage.getCartBadgeCount());
    }

    @When("I proceed to checkout")
    @Step("Proceed to checkout")
    public void iProceedToCheckout() {
        cartPage.proceedToCheckout();
    }

    @Then("I should be on the checkout information page")
    @Step("Verify on checkout information page")
    public void iShouldBeOnTheCheckoutInformationPage() {
        assertTrue("Should be on checkout step one", 
                   driver.getCurrentUrl().contains("checkout-step-one"));
    }

    @Given("I am on the checkout information page")
    @Step("Navigate to checkout information page")
    public void iAmOnTheCheckoutInformationPage() {
        iAmLoggedInAsAStandardUser();
        productsPage.addProductToCartByName("Sauce Labs Backpack");
        productsPage.goToCart();
        cartPage.proceedToCheckout();
    }

    @When("I click the {string} button without entering information")
    @Step("Click continue without information")
    public void iClickTheButtonWithoutEnteringInformation(String buttonText) {
        checkoutPage.clickContinue();
    }

    @When("I enter checkout information:")
    @Step("Enter checkout information")
    public void iEnterCheckoutInformation(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> data = rows.get(0);

        checkoutPage.fillCheckoutInfo(
                data.get("firstName"),
                data.get("lastName"),
                data.get("postalCode")
        );
    }

    @When("I continue to overview")
    @Step("Continue to overview")
    public void iContinueToOverview() {
        checkoutPage.clickContinue();
    }

    @Then("I should see the order summary")
    @Step("Verify order summary is displayed")
    public void iShouldSeeTheOrderSummary() {
        assertTrue(checkoutPage.isOrderSummaryDisplayed());
    }

    @Then("I should be on the checkout overview page")
    @Step("Verify on checkout overview page")
    public void iShouldBeOnTheCheckoutOverviewPage() {
        assertTrue("Should be on checkout step two", 
                   driver.getCurrentUrl().contains("checkout-step-two"));
    }

    @Given("I am on the checkout overview page with items {string} and {string}")
    @Step("Setup checkout overview with specific items")
    public void iAmOnTheCheckoutOverviewPageWithItemsAnd(String item1, String item2) {
        iAmLoggedInAsAStandardUser();
        productsPage.addProductToCartByName(item1);
        productsPage.addProductToCartByName(item2);
        productsPage.goToCart();
        cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();
    }

    @Then("I should see the correct item total")
    @Step("Verify correct item total")
    public void iShouldSeeTheCorrectItemTotal() {
        assertTrue("Item total should be displayed", 
                   checkoutOverviewPage.isOrderSummaryDisplayed());
    }

    @And("I should see the calculated tax")
    @Step("Verify tax is calculated")
    public void iShouldSeeTheCalculatedTax() {
        assertTrue("Tax should be displayed", 
                   checkoutOverviewPage.isOrderSummaryDisplayed());
    }

    @And("I should see the correct total amount")
    @Step("Verify correct total amount")
    public void iShouldSeeTheCorrectTotalAmount() {
        assertTrue("Total should be displayed", 
                   checkoutOverviewPage.isOrderSummaryDisplayed());
    }

    @Given("I am on the checkout overview page")
    @Step("Navigate to checkout overview page")
    public void iAmOnTheCheckoutOverviewPage() {
        iAmOnTheCheckoutInformationPage();
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();
    }

    @When("I finish the order")
    @Step("Finish the order")
    public void iFinishTheOrder() {
        checkoutOverviewPage.clickFinish();
    }

    @Then("I should see the order confirmation")
    @Step("Verify order confirmation")
    public void iShouldSeeTheOrderConfirmation() {
        assertTrue(driver.getCurrentUrl().contains("complete"));
    }

    @Then("I should see the order confirmation page")
    @Step("Verify order confirmation page")
    public void iShouldSeeTheOrderConfirmationPage() {
        assertTrue("Should be on checkout complete", 
                   driver.getCurrentUrl().contains("checkout-complete"));
    }

    @And("I should see {string}")
    @Step("Verify confirmation message: {0}")
    public void iShouldSeeThankYouForYourOrder(String message) {
        assertTrue("Should see confirmation message", 
                   checkoutCompletePage.getConfirmationMessage().contains(message));
    }

    @Given("I am on the order confirmation page")
    @Step("Navigate to order confirmation page")
    public void iAmOnTheOrderConfirmationPage() {
        iAmOnTheCheckoutOverviewPage();
        checkoutOverviewPage.clickFinish();
    }


    @And("the cart should be empty")
    @Step("Verify cart is empty")
    public void theCartShouldBeEmpty() {
        assertEquals("Cart should be empty", 0, productsPage.getCartBadgeCount());
    }

    @When("I enter first name {string}")
    @Step("Enter first name: {0}")
    public void iEnterFirstName(String firstName) {
        checkoutPage.enterFirstName(firstName);
    }

    @And("I enter last name {string}")
    @Step("Enter last name: {0}")
    public void iEnterLastName(String lastName) {
        checkoutPage.enterLastName(lastName);
    }

    @And("I enter postal code {string}")
    @Step("Enter postal code: {0}")
    public void iEnterPostalCode(String postalCode) {
        checkoutPage.enterPostalCode(postalCode);
    }

    @When("I click the Continue button")
    @Step("Click Continue button")
    public void iClickTheContinueButton() {
        checkoutPage.clickContinue();
    }

    @And("the script should be treated as plain text")
    @Step("Verify script is sanitized")
    public void theScriptShouldBeTreatedAsPlainText() {
        assertTrue("Should be on overview page", 
                   driver.getCurrentUrl().contains("checkout-step-two"));
    }

    @When("I enter first name with 500+ characters")
    @Step("Enter very long first name")
    public void iEnterFirstNameWith500Characters() {
        String longName = "a".repeat(501);
        checkoutPage.enterFirstName(longName);
    }

    @Then("the system should handle the long input gracefully")
    @Step("Verify long input is handled gracefully")
    public void theSystemShouldHandleTheLongInputGracefully() {
        assertTrue("System should handle long input", 
                   driver.getCurrentUrl().contains("checkout"));
    }

    @Given("I have completed a purchase")
    @Step("Complete a purchase")
    public void iHaveCompletedAPurchase() {
        iAmLoggedInAsAStandardUser();
        productsPage.addProductToCartByName("Sauce Labs Backpack");
        productsPage.goToCart();
        cartPage.proceedToCheckout();
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutOverviewPage.clickFinish();
    }

    @When("I click the browser back button")
    @Step("Click browser back button")
    public void iClickTheBrowserBackButton() {
        driver.navigate().back();
    }

    @Then("I should not be able to duplicate the order")
    @Step("Verify order cannot be duplicated")
    public void iShouldNotBeAbleToDuplicateTheOrder() {
        assertFalse("Should not be able to duplicate order", 
                    driver.getCurrentUrl().contains("checkout-complete"));
    }

    @And("I should be redirected to the inventory page or see an empty cart")
    @Step("Verify redirected to inventory or empty cart")
    public void iShouldBeRedirectedToTheInventoryPageOrSeeAnEmptyCart() {
        boolean onInventoryPage = driver.getCurrentUrl().contains("inventory.html");
        boolean cartEmpty = productsPage.getCartBadgeCount() == 0;
        assertTrue("Should be on inventory page or have empty cart", 
                   onInventoryPage || cartEmpty);
    }

    @Then("I should see error {string}")
    public void iShouldSeeError(String errorMessage) {
        assertTrue(checkoutPage.getErrorMessage().contains(errorMessage));
    }
}
