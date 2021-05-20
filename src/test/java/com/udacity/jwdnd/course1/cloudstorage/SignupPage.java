package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    WebElement inputLastName;

    @FindBy(id = "inputUsername")
    WebElement inputUsername;

    @FindBy(id = "inputPassword")
    WebElement inputPassword;

    @FindBy(id = "submit-button")
    WebElement signupButton;

    @FindBy(linkText = "back to login")
    WebElement loginLink;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void credentialSignup(String firstName, String lastName, String username, String password) {
        inputFirstName.sendKeys(firstName);
        inputLastName.sendKeys(lastName);
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        signupButton.click();
    }

    public void clickLogin() {
        loginLink.click();
    }
}
