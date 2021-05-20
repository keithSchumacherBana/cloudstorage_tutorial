package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	@LocalServerPort
	private int port;

	private static WebDriver driver;
	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;


	private static String username;
	private static String password;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterAll
	public static void afterAll() {
		driver.quit();
	}

	@BeforeEach
	public void beforeEach() {
		loginPage = new LoginPage(driver);
		homePage = new HomePage(driver);
		signupPage = new SignupPage(driver);
		int upperbound = 999999;
		Random rand = new Random();
		username = String.valueOf(rand.nextInt(upperbound));
		password = String.valueOf(rand.nextInt(upperbound));
	}

	@Test
	public void unauthorizedAccessRestrictions() {
//		Test that verifies that an unauthorized user can only access the login and signup pages.
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void signupLogin() {
//		Test that signs up a new user, logs in, verifies that the home page is accessible,
//		logs out, and verifies that the home page is no longer accessible.
		homePage.signupAndLogin(driver, port, signupPage, loginPage, username, password);
		Assertions.assertEquals("Home", driver.getTitle());
		driver.get("http://localhost:" + port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());
		homePage.logout();
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void createAndVerifyNote() {
//		Test that creates a note, and verifies it is displayed.
		String title = "title1";
		String description = "description1";
		homePage.signupAndLogin(driver, port, signupPage, loginPage, username, password);
		homePage.addNewNote(driver, title, description);
		String[] noteData = homePage.getNotes(driver);
		Assertions.assertEquals(title, noteData[0]);
		Assertions.assertEquals(description, noteData[1]);
		homePage.logout();
	}

	@Test
	public void editNote() {
//		Test that edits an existing note and verifies that the changes are displayed.
		String title = "title1";
		String description = "description1";
		homePage.signupAndLogin(driver, port, signupPage, loginPage, username, password);
		homePage.addNewNote(driver, title, description);
		String newTitle ="title2";
		String newDescription = "description2";
		homePage.editNote(driver, newTitle, newDescription);
		String[] noteData = homePage.getNotes(driver);
		Assertions.assertEquals(newTitle, noteData[0]);
		Assertions.assertEquals(newDescription, noteData[1]);
		homePage.logout();
	}

	@Test
	public void deleteNote() {
//		Test that deletes a note and verifies that the note is no longer displayed.
		String title = "title1";
		String description = "description1";
		homePage.signupAndLogin(driver, port, signupPage, loginPage, username, password);
		homePage.addNewNote(driver, title, description);
		homePage.deleteNote(driver);
		String[] noteData = homePage.getNotes(driver);
		Assertions.assertNull(noteData[0]);
		Assertions.assertNull(noteData[1]);
		homePage.logout();
	}

	@Test
	public void createAndVerifyCredential() {
//	Test that creates a set of credentials, verifies that they are displayed,
//	and verifies that the displayed password is encrypted.
	String url = "test.com";
	String credential_user = "credential_user";
	String credential_password = "credential_password";
	homePage.signupAndLogin(driver, port, signupPage, loginPage, username, password);
	homePage.addNewCredential(driver, url, credential_user, credential_password);
	String[] credentialData = homePage.getCredentials(driver);
	Assertions.assertEquals(url, credentialData[0]);
	Assertions.assertEquals(credential_user, credentialData[1]);
	Assertions.assertNotEquals(credential_password, credentialData[2]);
	homePage.logout();
	}

	@Test
	public void editCredential() {
//	Test that views an existing set of credentials, verifies that the viewable password is unencrypted,
//	edits the credentials, and verifies that the changes are displayed.
		String url = "url.com";
		String credUser = "user1";
		String credPassword = "password1";
		homePage.signupAndLogin(driver, port, signupPage, loginPage, username, password);
		homePage.addNewCredential(driver, url, credUser, credPassword);
		String newUrl = "url2.com";
		String newCredUser = "user2";
		String newCredPassword = "password2";
		homePage.editCredential(driver, newUrl, newCredUser, newCredPassword);
		String[] credentialData = homePage.getCredentials(driver);
		Assertions.assertEquals(newUrl, credentialData[0]);
		Assertions.assertEquals(newCredUser, credentialData[1]);
		Assertions.assertNotEquals(newCredPassword, credentialData[2]);
		homePage.logout();
	}

	@Test
	public void deleteCredential() {
//		Test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
		String url = "url.com";
		String credUser = "user1";
		String credPassword = "password1";
		homePage.signupAndLogin(driver, port, signupPage, loginPage, username, password);
		homePage.addNewCredential(driver, url, credUser, credPassword);
		homePage.deleteCredential(driver);
		String[] credentialData = homePage.getCredentials(driver);
		Assertions.assertNull(credentialData[0]);
		Assertions.assertNull(credentialData[1]);
		Assertions.assertNull(credentialData[2]);
		homePage.logout();
	}
}
