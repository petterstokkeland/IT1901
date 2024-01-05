package ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

import core.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import persistence.JsonController;

/** JUnit test class for {@link LogInController} class. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogInControllerTest extends ApplicationTest implements FxHelper {

  private LogInController controller;
  private JsonController jsonController;
  private PasswordField password;
  private TextField username;
  private User user;

  /**
   * Finds and returns a node within the current scene graph using the provided query.
   *
   * @param <T> The type of the node to be returned.
   * @param query The query string used to search for the node.
   * @return The node that matches the provided query.
   * @see Node
   */
  @Override
  public <T extends Node> T find(final String query) {
    return lookup(query).query();
  }

  /**
   * Initializes and displays the primary stage with the login interface.
   *
   * @param stage The primary stage to be initialized and displayed.
   * @throws Exception If there's an issue loading the FXML or any other related exception.
   */
  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(LogInController.class.getResource("/ui/LogIn.fxml"));
    final Parent parent = loader.load();
    this.controller = loader.getController();
    controller.setFilePath("test.json");
    controller.setPrimaryStage(stage);
    controller.getPrimaryStage().setScene(new Scene(parent));
    controller.getPrimaryStage().setMinHeight(750);
    controller.getPrimaryStage().setMinWidth(500);
    controller.getPrimaryStage().show();
  }

  /**
   * Initializes the test environment before each test. This includes setting up the JSON controller
   * and creating a mock user.
   *
   * @throws Exception If there's an issue initializing the JSON controller or saving the user.
   */
  @BeforeEach
  public void setUp() throws Exception {
    this.jsonController = new JsonController("test.json");
    this.user = new User("test", "test");
    this.jsonController.saveNewUserToJson(user);
  }

  /**
   * This method delete the test.json file after each test.
   *
   * @throws IOException If an I/O error occurs while attempting to delete the test JSON file.
   */
  @AfterAll
  public void tearDownAfterAll() throws IOException {
    Path testFilePath = Paths.get(System.getProperty("user.home"), "money_spender", "test.json");
    Files.deleteIfExists(testFilePath);
  }

  /** Tests the behavior of the "Register New User" button. */
  @Test
  public void testRegisterNewUserButton() {
    Button registerNewUserFromLogIn = find("#registerNewUser");
    clickOn(registerNewUserFromLogIn);
    FxHelper.waitTwoDecisecond();
  }

  /** Asserts that the controller instance is not null. */
  @Test
  public void assertnotnull() {
    assertNotNull(controller);
  }

  /**
   * Tests the behavior and functionality of the login button. Ensures that after a successful
   * login, the logout button is visible.
   */
  @Test
  public void testLogInButton() {
    username = find("#username");
    clickOn(username).write("test");
    FxHelper.waitTwoDecisecond();
    password = find("#password");
    clickOn(password).write("test");
    FxHelper.waitTwoDecisecond();
    Button logInButton = find("#loginButton");
    clickOn(logInButton);
    FxHelper.waitTwoDecisecond();
    Node button = find("#newExpense");
    assertNotNull(button);
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /**
   * Tests the behavior of the login functionality when provided with incorrect credentials. Ensures
   * that an appropriate error message or dialog is displayed to the user.
   */
  @Test
  public void testLoginException() {
    FxHelper.waitTwoDecisecond();
    username = find("#username");
    clickOn(username).write("wrongUsername");
    password = find("#password");
    clickOn(password).write("wrongPassword");
    Button logIn = find("#loginButton");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    verifyLogInButtonExist();
  }

  /** Tests logging in with an incorrect password, but correct username. */
  @Test
  public void testLogInWithWrongPassword() {
    username = find("#username");
    clickOn(username).write("test");
    password = find("#password");
    clickOn(password).write("wrongPassword");
    FxHelper.waitTwoDecisecond();
    Button logIn = find("#loginButton");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    verifyLogInButtonExist();
  }

  /** Tests logging in with an incorrect username, but correct password. */
  @Test
  public void testLogInWithWrongUsername() {
    username = find("#username");
    clickOn(username).write("wrongUsername");
    password = find("#password");
    clickOn(password).write("test");
    FxHelper.waitTwoDecisecond();
    Button logIn = find("#loginButton");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    verifyLogInButtonExist();
  }

  /**
   * Tests the behavior of the "Switch to Register New User" functionality. Ensures that after
   * clicking on the register button, the back to login button is visible.
   */
  @Test
  public void testSwitchToRegisterNewUser() {
    Node registerNewUserFromLogIn = find("#registerNewUser");
    clickOn(registerNewUserFromLogIn);
    FxHelper.waitTwoDecisecond();
    Node button = find("#backToLogIn");
    assertNotNull(button);
  }

  /**
   * Tests the visibility of the title on the login page. Ensures that the title "Please log in" is
   * present and visible to the user.
   */
  @Test
  public void testFindTitle() {
    Node title = find("User Login");
    assertNotNull(title);
  }

  /**
   * Tests the visibility of the username input field on the login page. Ensures that the username
   * input field is present and visible to the user.
   */
  @Test
  public void testFindUsername() {
    Node username = find("#username");
    assertNotNull(username);
  }

  /**
   * Tests the visibility of the password input field on the login page. Ensures that the password
   * input field is present and visible to the user.
   */
  @Test
  public void testFindPassword() {
    Node password = find("#password");
    assertNotNull(password);
  }

  /** Tests the login functionality with an empty username. */
  @Test
  public void testLoginWithEmptyUsername() {
    password = find("#password");
    clickOn(password).write("testPassword");
    FxHelper.waitTwoDecisecond();
    Button logIn = find("#loginButton");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /** Tests the login functionality with an empty password. */
  @Test
  public void testLoginWithEmptyPassword() {
    username = find("#username");
    clickOn(username).write("testUsername");
    FxHelper.waitTwoDecisecond();
    Button logIn = find("#loginButton");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /** Tests the login functionality with special characters in the username and password. */
  @Test
  public void testLoginWithSpecialCharacters() {
    username = find("#username");
    clickOn(username).write("!@#$%^&*()");
    password = find("#password");
    clickOn(password).write("!@#$%^&*()");
    Button logIn = find("#loginButton");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /** Tests the login functionality with a long input for both username and password. */
  @Test
  public void testLoginWithLongInput() {
    username = find("#username");
    clickOn(username).write("longUsernameInput".repeat(3));
    password = find("#password");
    clickOn(password).write("longPasswordInput".repeat(3));
    Button logIn = find("#loginButton");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    Button button = find("#registerNewUser");
    FxHelper.waitTwoDecisecond();
    assertNotNull(button);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /** Verifies that the "Log in" button exists in the UI. */
  private void verifyLogInButtonExist() {
    FxHelper.waitForFX();
    Node logInButton = find("#loginButton");
    assertNotNull(logInButton);
  }
}
