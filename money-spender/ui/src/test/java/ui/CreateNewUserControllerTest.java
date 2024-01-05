package ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

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
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

/** JUnit test class for {@link CreateNewUserController} class. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class CreateNewUserControllerTest extends ApplicationTest implements FxHelper {

  private CreateNewUserController controller;
  private PasswordField password;
  private TextField username;

  @Override
  public <T extends Node> T find(String query) {
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
    final FXMLLoader loader =
        new FXMLLoader(CreateNewUserController.class.getResource("/ui/CreateNewUser.fxml"));
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
   * This method delete the test.json file after each test.
   *
   * @throws IOException If an I/O error occurs while attempting to delete the test JSON file.
   */
  @AfterAll
  public void tearDownAfterAll() throws IOException {
    Path testFilePath = Paths.get(System.getProperty("user.home"), "money_spender", "test.json");
    Files.deleteIfExists(testFilePath);
  }

  /** Verifies that the "Register New User" button exists in the UI. */
  private void verifyNewUserButtonExists() {
    FxHelper.waitTwoDecisecond();
    Button registerNewUserFromLogIn = find("#registerNewUser");
    assertNotNull(registerNewUserFromLogIn);
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
  public void testControllerInitialization() {
    assertNotNull(controller);
  }

  /** Tests the behavior of the "Back to login" button. */
  @Test
  public void testBackToRegisterNewUser() {
    Button registerNewUserFromLogIn = find("#backToLogIn");
    clickOn(registerNewUserFromLogIn);
    verifyNewUserButtonExists();
  }

  /**
   * Try register new user with username and password If the user is created the new expense button
   * should not be null
   */
  @Test
  @Order(1)
  public void testCreateNewUser() {
    username = find("#username");
    clickOn(username).write("test");
    FxHelper.waitForFX();
    password = find("#password");
    clickOn(password).write("test");
    FxHelper.waitForFX();
    Button registerNewUserFromLogIn = find("#registerNewUser");
    clickOn(registerNewUserFromLogIn);
    FxHelper.waitForFX();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitForFX();
    Button newExpense = find("#newExpense");
    assertNotNull(newExpense);
  }

  /**
   * Test that the user with username test and password test exsist. Then the user should should not
   * be in the home page and therefore the registerNewUser button should not be null This user will
   * exist default
   */
  @Test
  @Order(2)
  public void testWithExistingUser() {
    username = find("#username");
    password = find("#password");
    clickOn(username).write("test");
    clickOn(password).write("test");
    FxHelper.waitTwoDecisecond();
    Button registerNewUserFromLogIn = find("#registerNewUser");
    clickOn(registerNewUserFromLogIn);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());

    verifyNewUserButtonExists();
  }

  /** Tests the create new user functionality with an empty username. */
  @Test
  public void testCreateUserWithEmptyUsername() {
    password = find("#password");
    clickOn(password).write("testPassword");
    Button logIn = find("#registerNewUser");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();

    verifyNewUserButtonExists();
  }

  /** Tests the create new user functionality with an empty password. */
  @Test
  public void testCreateUserWithEmptyPassword() {
    username = find("#username");
    clickOn(username).write("testUsername");
    Button logIn = find("#registerNewUser");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();

    verifyNewUserButtonExists();
  }

  /**
   * Tests the create new user functionality with special characters in the username and password.
   */
  @Test
  public void testCreateUserWithSpecialCharacters() {
    username = find("#username");
    clickOn(username).write("!@#$%^&*()");
    password = find("#password");
    clickOn(password).write("!@#$%^&*()");
    Button logIn = find("#registerNewUser");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());

    verifyNewUserButtonExists();
  }

  /** Tests the create new user functionality with a long input for both username and password. */
  @Test
  public void testCreateUserWithLongInput() {
    username = find("#username");
    clickOn(username).write("longUsernameInput");
    password = find("#password");
    clickOn(password).write("longPasswordInput");
    Button logIn = find("#registerNewUser");
    FxHelper.waitTwoDecisecond();
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();
    Button button = find("#registerNewUser");
    assertNotNull(button);
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    verifyNewUserButtonExists();
  }
}
