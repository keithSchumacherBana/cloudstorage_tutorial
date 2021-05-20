package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "display-note-description")
    private WebElement noteDescription;

    @FindBy(id = "display-note-title")
    private WebElement noteTitle;

    @FindBy(id = "display-credential-url")
    private WebElement url;

    @FindBy(id="display-credential-user")
    private WebElement user;

    @FindBy(id="display-credential-password")
    private WebElement password;

    @FindBy(id = "edit-note")
    private WebElement editNoteButton;

    @FindBy(id = "edit-credential")
    private WebElement editCredentialButton;

    @FindBy(id = "delete-note")
    private WebElement deleteNoteButton;

    @FindBy(id = "delete-credential")
    private WebElement deleteCredentialButton;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void signupAndLogin(WebDriver driver, int port, SignupPage signupPage, LoginPage loginPage, String username,
                      String password) {
        driver.get("http://localhost:" + port + "/signup");
        signupPage.credentialSignup("John", "Doe", username, password);
        driver.get("http://localhost:" + port + "/login");
        loginPage.credentialLogin(username, password);
    }

    public void logout() {
        logoutButton.click();
    }

    public void clickNoteTab(WebDriver driver) {
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement noteTab = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        noteTab.click();
    }

    public void clickAddNote(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement newNote = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));
        newNote.click();
    }

    public void submitTitleAndDescription(WebDriver driver, String title, String description){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement newTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement newDescription = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        newTitle.sendKeys(Keys.chord(Keys.CONTROL, "a"), title);
        newDescription.sendKeys(Keys.chord(Keys.CONTROL, "a"), description);
        driver.findElement(By.id("save-changes")).click();
    }

    public void addNewNote(WebDriver driver, String title, String description) {
        this.clickNoteTab(driver);
        this.clickAddNote(driver);
        this.submitTitleAndDescription(driver, title, description);
        this.clickNoteTab(driver);
    }

    public void editNote(WebDriver driver, String newTitle, String newDescription) {
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        editNoteButton.click();
        this.submitTitleAndDescription(driver, newTitle, newDescription);
        this.clickNoteTab(driver);
    }

    public void deleteNote(WebDriver driver) {
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        deleteNoteButton.click();
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        this.clickNoteTab(driver);
    }

    public String[] getNotes(WebDriver driver) {
        String[] noteData = new String[2];
        try {
            noteData[0] = noteTitle.getAttribute("innerHTML");
            noteData[1] = noteDescription.getAttribute("innerHTML");
        } catch (NoSuchElementException e) { return noteData; }
        return noteData;
    }

    public void clickCredentialTab(WebDriver driver) {
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement credentialTab = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credentialTab.click();
    }

    public void clickAddCredential(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement newNote = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("add-new-credential")));
        newNote.click();
    }

    public void submitCredential(WebDriver driver, String url, String credential_user, String credential_password){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement newURL = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement newUser = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement newPassword = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        newURL.sendKeys(Keys.chord(Keys.CONTROL, "a"), url);
        newUser.sendKeys(Keys.chord(Keys.CONTROL, "a"), credential_user);
        newPassword.sendKeys(Keys.chord(Keys.CONTROL, "a"), credential_password);
        driver.findElement(By.id("save-credentials")).click();
    }

    public void addNewCredential(WebDriver driver, String url, String credential_user, String credential_password){
        this.clickCredentialTab(driver);
        this.clickAddCredential(driver);
        this.submitCredential(driver, url, credential_user, credential_password);
        this.clickCredentialTab(driver);
    }

    public void editCredential(WebDriver driver,String url ,String username,String password){
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        editCredentialButton.click();
        this.submitCredential(driver, url, username, password);
        this.clickCredentialTab(driver);
    }

    public String[] getCredentials(WebDriver driver) {
        String[] credentialData = new String[3];
        try {
            credentialData[0] = url.getAttribute("innerHTML");
            credentialData[1] = user.getAttribute("innerHTML");
            credentialData[2] = password.getAttribute("innerHTML");
        } catch (NoSuchElementException e) { return credentialData; }
        return credentialData;
    }

    public void deleteCredential(WebDriver driver) {
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        deleteCredentialButton.click();
        try { Thread.sleep(500); }
        catch (Exception e) { System.out.println(e);}
        this.clickCredentialTab(driver);
    }



}
