package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import core.Expense;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

/**
 * Test class for validating various user stories related to the application's UI.
 *
 * <p>This class contains test methods that simulate and validate different user interactions with
 * the application's UI, such as logging in, creating expenses, viewing expense history, and
 * applying filters. The tests are designed to ensure that the application's UI behaves as expected
 * and provides accurate feedback to the user.
 *
 * <p>The tests are ordered using the {@link OrderAnnotation} to ensure they are executed in a
 * specific sequence. The class also implements the {@link FxHelper} interface to provide utility
 * methods for interacting with JavaFX components.
 *
 * <p>The test methods make use of the TestFX framework to simulate user interactions and validate
 * UI components' states. Assertions are made against expected outcomes to ensure the correctness of
 * the application's behavior.
 *
 * @see ApplicationTest
 * @see FxHelper
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UserStoriesTest extends ApplicationTest implements FxHelper {

  private LogInController controller;
  private static final String testUsername = "NTNUTestUser";
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  /**
   * Retrieves a JavaFX Node element from the current UI scene based on the provided query string.
   *
   * <p>This method is a utility function to simplify the process of querying UI elements in a
   * JavaFX scene. It uses the lookup-string method to search for the node and then queries the
   *
   * @param <T> The type of the JavaFX Node element to be retrieved.
   * @param query The query string used to search for the node in the UI scene.
   * @return The queried JavaFX Node element of type T.
   */
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
    final FXMLLoader loader = new FXMLLoader(LogInController.class.getResource("/ui/LogIn.fxml"));
    final Parent parent = loader.load();
    this.controller = loader.getController();
    this.controller.setFilePath("test.json");
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

  /**
   * Tests the login functionality using a test user and verifies the user registration process.
   *
   * <p>The steps executed in this test method are:
   *
   * <ul>
   *   <li>Attempt to log in without registering, expecting a failure.
   *   <li>Proceed to the user registration section.
   *   <li>Register a new test user with a generated username and a predefined password.
   *   <li>Verify successful registration through a confirmation message.
   *   <li>Log out after successful registration.
   *   <li>Log in again using the newly registered test user's credentials.
   *   <li>Verify successful login through a confirmation message.
   * </ul>
   *
   * This test ensures that the user registration process works as expected and that users can log
   * in using the credentials they registered with.
   *
   * @throws IOException If there's an issue related to input/output operations during the test.
   */
  @Test
  @Order(1)
  public void testLogInWithTestUser() throws IOException {
    Button logIn = find("#loginButton");
    clickOn(logIn);
    FxHelper.waitTwoDecisecond();

    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");

    Button createNewUser = find("#registerNewUser");
    clickOn(createNewUser);
    FxHelper.waitTwoDecisecond();

    Button backToLogIn = find("#backToLogIn");
    clickOn(backToLogIn);
    FxHelper.waitTwoDecisecond();

    createNewUser = find("#registerNewUser");
    clickOn(createNewUser);
    FxHelper.waitTwoDecisecond();

    TextField username = find("#username");
    clickOn(username).write(testUsername);
    TextField password = find("#password");
    clickOn(password).write("password");
    createNewUser = find("#registerNewUser");
    clickOn(createNewUser);
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    Button logOut = find("#logOut");
    clickOn(logOut);
    FxHelper.waitTwoDecisecond();

    Button login = find("#loginButton");
    TextField Username = find("#username");
    TextField Password = find("#password");

    clickOn(Username).write(testUsername);
    clickOn(Password).write("password");
    clickOn(login);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
  }

  /**
   * Tests the functionality of logging in and creating a new expense with a non-existing category.
   *
   * <p>This test method performs the following steps:
   *
   * <ul>
   *   <li>Login with a test user.
   *   <li>Access the "New Expense" section.
   *   <li>Provide details for the new expense, including a category that doesn't exist yet, a date,
   *       price, and description.
   *   <li>Add the new expense and verify the success message.
   *   <li>Validate the displayed expense history against the expected string to ensure the new
   *       expense is correctly added.
   * </ul>
   *
   * The test ensures that users can successfully create expenses with new categories that are not
   * already present in the system.
   */
  @Test
  @Order(2)
  public void testLogInAndCreateNewExpense() {
    setLogInCredentials();
    Button newExpense = find("#newExpense");
    clickOn(newExpense);
    FxHelper.waitTwoDecisecond();

    TextField notExistingCategory = find("#notExistingCategory");
    DatePicker datePicker = find("#datePicker");
    TextField price = find("#price");
    TextField description = find("#description");
    Button add = find("#add");
    FxHelper.waitTwoDecisecond();

    datePicker.setValue(LocalDate.of(2023, 1, 1));
    FxHelper.waitTwoDecisecond();

    clickOn(notExistingCategory).write("Books");
    clickOn(price).write("200");
    clickOn(description).write("Artificial Intelligence");
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    clickOn("OK");
    FxHelper.waitTwoDecisecond();

    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    FxHelper.waitTwoDecisecond();

    assertEquals(1.0, lastExpenses.getItems().size(), "There should be one expense");
    Expense row = lastExpenses.getItems().get(0);
    LocalDate date = LocalDate.of(2023, 1, 1);
    String dateString = date.format(formatter);
    Label totalPrice = find("#totalPriceLabel");
    assertEquals(dateString, row.getDate(), "Wrong date on index 0");
    assertEquals("Books", row.getCategory(), "Wrong category on index 0, should be Books");
    assertEquals(
        "Artificial Intelligence",
        row.getDescription(),
        "Wrong description, should be Artificial Intelligence");
    assertEquals(200.00, row.getPrice(), "Wrong price, should be 200.00");
    assertEquals("Total price: 200.00 kr", totalPrice.getText());
  }

  /**
   * Tests the functionality of logging in and creating a new expense using an existing category.
   *
   * <p>This test method performs the following steps:
   *
   * <ul>
   *   <li>Login with a test user.
   *   <li>Access the "New Expense" section.
   *   <li>Select an existing category from the dropdown menu.
   *   <li>Set the date, price, and description for the new expense.
   *   <li>Add the new expense and verify the success message.
   *   <li>Validate the displayed expense history against the expected string to ensure the new
   *       expense is correctly added.
   * </ul>
   *
   * The test ensures that users can successfully create expenses using categories that already
   * exist in the system.
   */
  @Test
  @Order(3)
  public void testLogInAndCreateNewExpenseWithExistingCategory() {
    FxHelper.waitTwoDecisecond();
    setLogInCredentials();
    FxHelper.waitTwoDecisecond();

    Button newExpense = find("#newExpense");
    clickOn(newExpense);
    FxHelper.waitTwoDecisecond();

    ComboBox<String> dragDownCategory = find("#dragDownCategory");
    DatePicker datePicker = find("#datePicker");
    TextField price = find("#price");
    TextField description = find("#description");
    Button add = find("#add");

    FxHelper.waitTwoDecisecond();
    datePicker.setValue(LocalDate.of(2022, 1, 1));
    FxHelper.waitTwoDecisecond();

    clickOn(dragDownCategory);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    clickOn(price).write("2000");
    clickOn(description).write("10 Books");
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    clickOn("OK");
    FxHelper.waitTwoDecisecond();

    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    FxHelper.waitTwoDecisecond();
    assertEquals(2.0, lastExpenses.getItems().size(), "There should be two expense");
    Expense row = lastExpenses.getItems().get(0);
    LocalDate date = LocalDate.of(2022, 1, 1);
    String dateString = date.format(formatter);
    FxHelper.waitTwoDecisecond();
    Label totalPrice = find("#totalPriceLabel");
    FxHelper.waitTwoDecisecond();
    assertEquals(dateString, row.getDate(), "Wrong date on index 0");
    assertEquals("Books", row.getCategory(), "Wrong category on index 0, should be Books");
    assertEquals("10 Books", row.getDescription(), "Wrong description, should be 10 Books");
    assertEquals(2000.00, row.getPrice(), "Wrong price, should be 2000.00");
    assertEquals("Total price: 2200.00 kr", totalPrice.getText());
  }

  /**
   * Tests the functionality of viewing expense history with various filters applied.
   *
   * <p>This test method performs the following steps:
   *
   * <ul>
   *   <li>Login with a test user.
   *   <li>Access the "See History" view.
   *   <li>Apply different date filters and verify the displayed expense history.
   *   <li>Clear filters and verify the displayed history.
   *   <li>Apply category filters and verify the displayed history.
   *   <li>Test invalid date range-filters
   *   <li>Finally, navigate back and log out.
   * </ul>
   *
   * Throughout the test, the displayed expense history is validated against expected strings to
   * ensure the correctness of the filtering functionality.
   */
  @Test
  @Order(4)
  public void testSeeHistoryWithFilters() {
    FxHelper.waitTwoDecisecond();
    setLogInCredentials();
    FxHelper.waitTwoDecisecond();

    Button seeHistory = find("#seeHistory");
    clickOn(seeHistory);
    FxHelper.waitTwoDecisecond();

    DatePicker startDate = find("#startDate");
    DatePicker endDate = find("#endDate");
    ComboBox<String> category = find("#category");
    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    Button clearFilter = find("#clearFilter");
    FxHelper.waitTwoDecisecond();
    Button back = find("#back");
    FxHelper.waitTwoDecisecond();
    Button show = find("#show");
    FxHelper.waitTwoDecisecond();

    Expense row1 = lastExpenses.getItems().get(0);
    Expense row2 = lastExpenses.getItems().get(1);
    LocalDate date1 = LocalDate.of(2022, 1, 1);
    LocalDate date2 = LocalDate.of(2023, 1, 1);
    String date1String = date1.format(formatter);
    String date2String = date2.format(formatter);
    Label totalPrice = find("#totalPriceLabel");
    FxHelper.waitTwoDecisecond();

    startDate.setValue(LocalDate.of(2020, 1, 1));
    clickOn(show);
    assertEquals(2.0, lastExpenses.getItems().size(), "There should be two expense");
    assertEquals(
        date1String,
        row1.getDate(),
        "Wrong date on index 0, should be the must expensive expense first");
    assertEquals("Books", row1.getCategory(), "Wrong category on index 0, should be Books");
    assertEquals("10 Books", row1.getDescription(), "Wrong description, should be 10 Books");
    assertEquals(2000.00, row1.getPrice(), "Wrong price, should be 2000,00");

    assertEquals(
        date2String,
        row2.getDate(),
        "Wrong date on index 1, this should come after the must expense expense");
    assertEquals("Books", row2.getCategory(), "Wrong category on index , should be Books");
    assertEquals(
        "Artificial Intelligence",
        row2.getDescription(),
        "Wrong description, Artificial Intelligence");
    assertEquals(200.00, row2.getPrice(), "Wrong price, should be 200,00");
    assertEquals("Total price: 2200.00 kr", totalPrice.getText());

    // should be the AI Book because it is the only one in 2023
    startDate.setValue(LocalDate.of(2023, 1, 1));
    clickOn(show);
    assertEquals(1.0, lastExpenses.getItems().size(), "There should be one expense");
    assertEquals(
        date2String,
        lastExpenses.getItems().get(0).getDate(),
        "Wrong date on index 0 should be 01.01.2023");
    assertEquals(
        "Books",
        lastExpenses.getItems().get(0).getCategory(),
        "Wrong category on index , should be Books");
    assertEquals(
        "Artificial Intelligence",
        lastExpenses.getItems().get(0).getDescription(),
        "Wrong description, Artificial Intelligence");
    assertEquals(
        200.00, lastExpenses.getItems().get(0).getPrice(), "Wrong price, should be 200,00");
    assertEquals("Total price: 200.00 kr", totalPrice.getText());

    startDate.setValue(LocalDate.of(2022, 1, 1));
    endDate.setValue(LocalDate.of(2022, 2, 2));
    clickOn(show);
    assertEquals(1.0, lastExpenses.getItems().size(), "There should be one expense");
    assertEquals(
        date1String,
        lastExpenses.getItems().get(0).getDate(),
        "Wrong date on index 0, should be the must expensive expense first");
    assertEquals(
        "Books",
        lastExpenses.getItems().get(0).getCategory(),
        "Wrong category on index 0, should be Books");
    assertEquals(
        "10 Books",
        lastExpenses.getItems().get(0).getDescription(),
        "Wrong description, should be 10 Books");
    assertEquals(
        2000.00, lastExpenses.getItems().get(0).getPrice(), "Wrong price, should be 2000,00");
    assertEquals("Total price: 2000.00 kr", totalPrice.getText());

    clickOn(clearFilter);
    assertEquals(2.0, lastExpenses.getItems().size(), "There should be two expense");
    assertEquals(
        date1String,
        row1.getDate(),
        "Wrong date on index 0, should be the must expensive expense first");
    assertEquals("Books", row1.getCategory(), "Wrong category on index 0, should be Books");
    assertEquals("10 Books", row1.getDescription(), "Wrong description, should be 10 Books");
    assertEquals(2000.00, row1.getPrice(), "Wrong price, should be 2000,00");

    assertEquals(
        date2String,
        row2.getDate(),
        "Wrong date on index 1, this should come after the must expense expense");
    assertEquals("Books", row2.getCategory(), "Wrong category on index , should be Books");
    assertEquals(
        "Artificial Intelligence",
        row2.getDescription(),
        "Wrong description, Artificial Intelligence");
    assertEquals(200.00, row2.getPrice(), "Wrong price, should be 200,00");
    assertEquals("Total price: 2200.00 kr", totalPrice.getText());

    clickOn(category);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    clickOn(show);
    assertEquals(2.0, lastExpenses.getItems().size(), "There should be two expense");
    assertEquals(
        date1String,
        row1.getDate(),
        "Wrong date on index 0, should be the must expensive expense first");
    assertEquals("Books", row1.getCategory(), "Wrong category on index 0, should be Books");
    assertEquals("10 Books", row1.getDescription(), "Wrong description, should be 10 Books");
    assertEquals(2000.00, row1.getPrice(), "Wrong price, should be 2000,00");

    assertEquals(
        date2String,
        row2.getDate(),
        "Wrong date on index 1, this should come after the must expense expense");
    assertEquals("Books", row2.getCategory(), "Wrong category on index , should be Books");
    assertEquals(
        "Artificial Intelligence",
        row2.getDescription(),
        "Wrong description, Artificial Intelligence");
    assertEquals(200.00, row2.getPrice(), "Wrong price, should be 200,00");
    assertEquals("Total price: 2200.00 kr", totalPrice.getText());

    startDate.setValue(LocalDate.of(2023, 1, 1));
    clickOn(show);
    assertEquals(1.0, lastExpenses.getItems().size(), "There should be one expense");
    assertEquals(
        date2String,
        lastExpenses.getItems().get(0).getDate(),
        "Wrong date on index 0 should be 01.01.2023");
    assertEquals(
        "Books",
        lastExpenses.getItems().get(0).getCategory(),
        "Wrong category on index , should be Books");
    assertEquals(
        "Artificial Intelligence",
        lastExpenses.getItems().get(0).getDescription(),
        "Wrong description, Artificial Intelligence");
    assertEquals(
        200.00, lastExpenses.getItems().get(0).getPrice(), "Wrong price, should be 200,00");
    assertEquals("Total price: 200.00 kr", totalPrice.getText());

    startDate.setValue(LocalDate.of(2023, 1, 1));
    endDate.setValue(LocalDate.of(2022, 2, 2));
    clickOn(show);
    verifyThat("OK", NodeMatchers.isVisible());
    clickOn("OK");
    FxHelper.waitTwoDecisecond();

    clickOn(back);
    FxHelper.waitTwoDecisecond();
    Button logOut = find("#logOut");
    clickOn(logOut);
  }

  /**
   * Validates the behavior of deleting an expense in the application.
   *
   * <p>This test performs the following steps: 1. Logs into the application using test credentials.
   * 2. Verifies successful login by checking for an 'OK' confirmation. 3. Navigates to the history
   * section. 4. Checks the initial number of expenses listed. 5. Initiates a delete action on the
   * first expense. 6. Confirms the deletion action. 7. Verifies that only one expense remains after
   * deletion.
   */
  @Test
  @Order(5)
  public void testDeleteExpense() {
    setLogInCredentials();

    Button seeHistory = find("#seeHistory");
    clickOn(seeHistory);
    FxHelper.waitTwoDecisecond();

    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    assertEquals(
        2.0,
        lastExpenses.getItems().size(),
        "after deleting the first expense, there should be one left");
    Set<Button> buttonSet = lookup(".expense-delete-button").queryAllAs(Button.class);
    FxHelper.waitTwoDecisecond();
    ArrayList<Button> deleteButtons = new ArrayList<>(buttonSet);

    clickOn(deleteButtons.get(0));
    clickOn("OK");
    assertEquals(
        1.0, lastExpenses.getItems().size(), "after deleting this should be one expense left");
  }

  /**
   * Sets and submits login credentials in the application.
   *
   * <p>The method performs the following steps: 1. Inputs a test username and password into the
   * respective fields. 2. Clicks on the login button. 3. Waits for UI elements to process. 4.
   * Verifies that a login confirmation ("OK") is visible. 5. Closes the confirmation prompt.
   */
  private void setLogInCredentials() {
    TextField Username = find("#username");
    TextField Password = find("#password");
    Button login = find("#loginButton");

    clickOn(Username).write(testUsername);
    clickOn(Password).write("password");
    clickOn(login);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
  }
}
