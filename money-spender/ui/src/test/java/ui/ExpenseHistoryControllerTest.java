package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import core.Expense;
import core.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.framework.junit5.ApplicationTest;
import persistence.JsonController;

/** JUnit test class for {@link ExpenseHistoryController} class. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ExpenseHistoryControllerTest extends ApplicationTest implements FxHelper {

  private ExpenseHistoryController controller;
  private User user;

  /**
   * Helper method to find a UI component by its query string.
   *
   * @param query The query string used to locate the UI component.
   * @return The UI component node.
   */
  @Override
  public <T extends Node> T find(String query) {
    return lookup(query).query();
  }

  /**
   * Initializes a default set of expenses and assigns them to a user.
   *
   * <p>This method creates two default expense entries for specific dates, one for books and
   * another for food, and assigns them to a newly created user with test as username.
   *
   * @throws IOException
   */
  private void setDefaultValues() throws IOException {
    List<Expense> expenses = new ArrayList<Expense>();
    Expense expense1 = new Expense(LocalDate.of(2020, 1, 1), "Books", 800.0, "Big Java");
    Expense expense2 = new Expense(LocalDate.of(2020, 1, 2), "Food", 189.0, "Sushi");
    expenses.add(expense1);
    expenses.add(expense2);
    this.user = new User("test", "test", expenses);
    JsonController jsonController = new JsonController("test.json");
    jsonController.saveNewUserToJson(user);
  }

  /**
   * Initializes and displays the primary stage with the login interface.
   *
   * @param stage The primary stage to be initialized and displayed.
   * @throws Exception If there's an issue loading the FXML or any other related exception.
   */
  @Override
  public void start(final Stage stage) throws Exception {
    setDefaultValues();
    FXMLLoader loader =
        new FXMLLoader(ExpenseHistoryController.class.getResource("/ui/ExpenseHistory.fxml"));
    Parent parent = loader.load();
    this.controller = loader.getController();
    controller.setFilePath("test.json");
    this.controller.loadUserAndData(user);
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

  /** Test to assert that the controller instance is correctly initialized. */
  @Order(1)
  @Test
  public void testControllerNotNull() {
    assertNotNull(controller);
  }

  /**
   * Tests the initial state of expenses. Ensures that the expenses table view is not null when the
   * application starts.
   */
  @Order(2)
  @Test
  public void testInitialExpensesState() {
    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    FxHelper.waitTwoDecisecond();
    assertNotNull(lastExpenses);
  }

  /**
   * Test to verify the behavior of the "Back to login" button. Ensures that after clicking the
   * button, the user is redirected to the home page where the "new expense" button should be
   * present.
   */
  @Order(3)
  @Test
  public void testBackToLoginButton() {
    Button backToHome = find("#back");
    clickOn(backToHome);
    FxHelper.waitTwoDecisecond();
    Button newExpense = find("#newExpense");
    assertNotNull(newExpense);
  }

  /**
   * Tests the functionality of filtering expenses by a specific start date. This test sets a future
   * date as the start date and ensures that the expenses table view reflects the expected state
   * after filtering.
   */
  @Order(4)
  @Test
  public void testFilterByStartDate() {
    DatePicker startDate = find("#startDate");
    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    Button show = find("#show");
    Button clearFilter = find("#clearFilter");
    FxHelper.waitTwoDecisecond();

    startDate.setValue(LocalDate.of(2023, 1, 2));
    clickOn(show);
    FxHelper.waitTwoDecisecond();

    assertEquals(
        0,
        lastExpenses.getItems().size(),
        "The start date is after the created expenses, therefore should be zero");
    clickOn(clearFilter);
    assertNull(startDate.getValue(), "Start date should be null");
  }

  /**
   * Tests the functionality of filtering expenses by an earlier start date. This test sets a past
   * date as the start date and ensures that the expenses table view reflects the expected state
   * after filtering.
   */
  @Order(5)
  @Test
  public void testFilterByEarlierStartDate() {
    DatePicker startDate = find("#startDate");
    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    Button show = find("#show");
    Button clearFilter = find("#clearFilter");
    FxHelper.waitTwoDecisecond();

    startDate.setValue(LocalDate.of(2020, 1, 2));
    clickOn(show);
    FxHelper.waitTwoDecisecond();
    assertEquals(1, lastExpenses.getItems().size(), "There should be one expense");

    LocalDate date1 = LocalDate.of(2020, 1, 2);
    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String date1String = date1.format(formatter1);
    Expense row = lastExpenses.getItems().get(0);

    assertEquals(date1String, row.getDate(), "Wrong date on index 0");
    assertEquals("Food", row.getCategory(), "Wrong category on index 0, should be Food");
    assertEquals("Sushi", row.getDescription(), "Wrong description, should be Sushi");
    assertEquals(189.00, row.getPrice(), "Wrong price, should be 189.00");

    clickOn(clearFilter);
    assertNull(startDate.getValue(), "Start date should be null");
  }

  /**
   * Tests the functionality of filtering expenses by a specific category. This test selects a
   * category from the ComboBox and ensures that the expenses table view reflects the expected state
   * after filtering by the chosen category.
   */
  @Order(6)
  @Test
  public void testFilterByCategory() {
    ComboBox<String> category = find("#category");
    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    Button show = find("#show");
    Button clearFilter = find("#clearFilter");
    FxHelper.waitTwoDecisecond();

    clickOn(category);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    clickOn(show);

    FxHelper.waitTwoDecisecond();
    LocalDate date1 = LocalDate.of(2020, 1, 1);
    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String date1String = date1.format(formatter1);
    Expense row = lastExpenses.getItems().get(0);

    assertEquals(1, lastExpenses.getItems().size(), "Should contain one expense");
    assertEquals(date1String, row.getDate(), "Wrong date on index 0");
    assertEquals("Books", row.getCategory(), "Wrong category on index 0, should be books");
    assertEquals("Big Java", row.getDescription(), "Wrong description, should be Big Java");
    assertEquals(800.00, row.getPrice(), "Wrong price, should be 800.00");

    clickOn(clearFilter);
    assertNull(category.getValue(), "category date should be null");
  }

  /**
   * Tests the functionality of filtering expenses by a specific end date. This test sets a future
   * date as the end date and ensures that the expenses table view reflects the expected state after
   * filtering.
   */
  @Order(7)
  @Test
  public void testFilterByEndDate() {
    DatePicker endDate = find("#endDate");
    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    Button show = find("#show");
    Button clearFilter = find("#clearFilter");

    endDate.setValue(LocalDate.of(2023, 1, 2));
    clickOn(show);
    FxHelper.waitTwoDecisecond();

    assertEquals(
        2,
        lastExpenses.getItems().size(),
        "The date is after the created expenses, therefore should be two");

    LocalDate date1 = LocalDate.of(2020, 1, 1);
    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String date1String = date1.format(formatter1);

    Expense row1 = lastExpenses.getItems().get(0);
    assertEquals(date1String, row1.getDate(), "Wrong date on index 0");
    assertEquals("Books", row1.getCategory(), "Wrong category on index 0, should be books");
    assertEquals("Big Java", row1.getDescription(), "Wrong description, should be Big Java");
    assertEquals(800.00, row1.getPrice(), "Wrong price, should be 800.00");

    LocalDate date2 = LocalDate.of(2020, 1, 2);
    String date2String = date2.format(formatter1);
    Expense row2 = lastExpenses.getItems().get(1);
    assertEquals(date2String, row2.getDate(), "Wrong date on index 0");
    assertEquals("Food", row2.getCategory(), "Wrong category on index 0, should be Food");
    assertEquals("Sushi", row2.getDescription(), "Wrong description, should be Sushi");
    assertEquals(189.00, row2.getPrice(), "Wrong price, should be 189.00");

    clickOn(clearFilter);
    assertEquals(
        2,
        lastExpenses.getItems().size(),
        "The date is after the created expenses, therefore should be two");
  }

  /**
   * Tests the functionality of deleting an expense from the TableView.
   *
   * <p>This method simulates the deletion of an expense by clicking on its corresponding delete
   * button and confirming the action. It then validates that the expense was removed correctly by
   * checking the remaining expenses in the TableView and ensuring their properties are as expected.
   */
  @Order(9)
  @Test
  public void testDeleteExpense() {
    Set<Button> buttonSet = lookup(".expense-delete-button").queryAllAs(Button.class);
    ArrayList<Button> deleteButtons = new ArrayList<>(buttonSet);

    FxHelper.waitTwoDecisecond();
    clickOn(deleteButtons.get(0));
    clickOn("OK");
    FxHelper.waitTwoDecisecond();

    TableView<Expense> lastExpenses = find("#grdExpenseHistory");
    assertEquals(
        1.0,
        lastExpenses.getItems().size(),
        "after deleting the first expense, there should be one left");
    Expense row = lastExpenses.getItems().get(0);
    LocalDate date = LocalDate.of(2020, 1, 2);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String dateString = date.format(formatter);
    assertEquals(dateString, row.getDate(), "Wrong date on index 0");
    assertEquals("Food", row.getCategory(), "Wrong category on index 0, should be Food");
    assertEquals("Sushi", row.getDescription(), "Wrong description, should be Sushi");
    assertEquals(189.00, row.getPrice(), "Wrong price, should be 189.00");
  }

  /**
   * Tests the accuracy of the total price displayed in the UI based on selected start dates.
   *
   * <p>This method verifies that the total price displayed on the UI matches the expected value
   * when filtering expenses by different start dates. It checks the behavior with two specific
   * dates, ensuring that the UI reflects the correct total price for expenses on and after those
   * dates.
   */
  @Order(8)
  @Test
  public void testTotalPrice() {
    Label price = find("#totalPriceLabel");
    DatePicker startDate = find("#startDate");
    Button show = find("#show");
    Button clearFilter = find("#clearFilter");
    FxHelper.waitTwoDecisecond();

    startDate.setValue(LocalDate.of(2020, 1, 2));
    clickOn(show);
    FxHelper.waitTwoDecisecond();
    assertEquals("Total price: 189.00 kr", price.getText(), "Wrong price, should be 189.00");
    clickOn(clearFilter);

    startDate.setValue(LocalDate.of(2020, 1, 1));
    clickOn(show);
    FxHelper.waitTwoDecisecond();
    assertEquals("Total price: 989.00 kr", price.getText(), "Wrong price, should be 898.00");
  }

  /**
   * Tests the application's behavior when an invalid date range is provided. This test sets a start
   * date that is after the end date and ensures that an alert is displayed to the user indicating
   * the invalid date range.
   */
  @Test
  public void testInvalidDateRangeAlert() {
    DatePicker startDate = find("#startDate");
    DatePicker endDate = find("#endDate");
    Button show = find("#show");
    FxHelper.waitTwoDecisecond();

    startDate.setValue(LocalDate.of(2023, 1, 2));
    endDate.setValue(LocalDate.of(2020, 1, 2));
    clickOn(show);
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
  }
}
