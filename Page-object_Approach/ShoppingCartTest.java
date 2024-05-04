package com.softserve.edu.opencart.tests;

import com.softserve.edu.opencart.pages.HomePage;
import com.softserve.edu.opencart.pages.ShoppingCartEmptyPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShoppingCartTest extends TestRunnerFirst {

    @Test
    public void verifyShoppingCartFunctionality() {

        HomePage homePage = loadApplication();
        homePage.clickShoppingCartLabel();

        ShoppingCartEmptyPage shoppingCartEmptyPage = new ShoppingCartEmptyPage(driver);
        String emptyCartText = shoppingCartEmptyPage.getShoppingCartEmptyText();
        Assertions.assertTrue(emptyCartText.contains("Your shopping cart is empty!"), "Shopping cart is not empty");


        shoppingCartEmptyPage.clickContinueButton();

        String expectedUrl = "https://demo.opencart.com/index.php?route=common/home&language=en-gb";

        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl());
    }
}
