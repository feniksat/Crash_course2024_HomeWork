package com.softserve.edu.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShoppingCartEmptyPage extends TopPart {

    private WebElement continueButton;
    private WebElement shoppingCartLabel;
    private WebElement shoppingCartEmpty;

    public ShoppingCartEmptyPage(WebDriver driver) {
        super(driver);
        initElements();
    }

    public void initElements() {
        shoppingCartEmpty = driver.findElement(By.cssSelector("div#content.col p"));
        shoppingCartLabel = driver.findElement(By.cssSelector(".list-inline-item i.fas.fa-shopping-cart"));
        continueButton = driver.findElement(By.cssSelector("div.float-end a.btn"));
    }

    public String getShoppingCartEmptyText() {
        return shoppingCartEmpty.getText();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

}
