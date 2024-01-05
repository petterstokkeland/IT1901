package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import persistence.JsonController;

/** JUnit test class for {@link HomeController} class. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HomeControllerTest extends ApplicationTest implements FxHelper {

  private HomeController controller;
  private Button logOut;
  private Button newExpense;
  private Button seeHistory;
  private TableView<Expense> lastExpenses;
  private User user;

  /**
   * Finds a UI component by its query string.
   *
   * @param query The query string used to locate the UI component.
   * @return The located UI component.
   */
  @Override
  public <T extends Node> T find(String query) {
    return lookup(query).query();
  }

  /**
   * Initializes a default set of expenses and assigns them to a test user.
   *
   * <p>This method creates a default list of expenses with pre-defined attributes. It initializes
   * two expense objects with specific date, category, amount, and description. These expenses are
   * then added to a list which is set to a user object with test as username.
   *
   * @throws IOException
   * @see Expense
   * @see User
   */
  private void setDefaultValue() throws IOException {
    List<Expense> expenses = new ArrayList<Expense>();
    expenses.add(new Expense(LocalDate.of(2020, 1, 1), "Sports", 1200.0, "Running shoes"));
    expenses.add(new Expense(LocalDate.of(2020, 1, 2), "School", 400.0, "books"));
    user = new User("test", "test", expenses);
    JsonController jsonController = new JsonController("test.json");
    jsonController.saveNewUserToJson(user);
  }

  /**
   * Initializes the test environment. This method sets up the primary stage, loads test data, and
   * initializes the home controller.
   *
   * @param stage The primary stage for the test.
   * @throws Exception if there's an error during initialization.
   */
  @Override
  public void start(final Stage stage) throws Exception {
    setDefaultValue();
    FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/ui/Home.fxml"));
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

  /**
   * Tests the functionality of the logout button. After clicking the logout button, the user should
   * be redirected to the login screen.
   */
  @Test
  public void testLogOutButton() {
    logOut = find("#logOut");
    FxHelper.waitTwoDecisecond();
    FxAssert.verifyThat(logOut, button -> button.isVisible());

    clickOn(logOut);
    FxHelper.waitTwoDecisecond();
    // The user should now been in the login screen
    Button registerNewUser = find("#registerNewUser");
    assertNotNull(registerNewUser);
  }

  /** Tests the presence of the application logo on the home screen. */
  @Test
  public void testFindLogo() {
    FxHelper.waitTwoDecisecond();
    Node logo = find("MoneySpender");
    assertNotNull(logo);
  }

  /**
   * Tests the functionality of the new expense button. After clicking this button, the user should
   * be redirected to the new expense screen.
   */
  @Test
  public void testNewExpenseButton() {
    newExpense = find("#newExpense");
    clickOn(newExpense);
    FxHelper.waitTwoDecisecond();
    Button backToHomeFromNewExpense = find("#backToHome");
    assertNotNull(backToHomeFromNewExpense);
  }

  /**
   * Tests the functionality of the see history button. After clicking this button, the user should
   * be redirected to the history screen.
   */
  @Test
  public void testSeeHistoryButton() {
    seeHistory = find("#seeHistory");
    FxHelper.waitTwoDecisecond();
    clickOn(seeHistory);
    FxHelper.waitTwoDecisecond();
    Button clearFilter = find("#clearFilter");
    FxHelper.waitTwoDecisecond();
    assertNotNull(clearFilter);
  }

  /** Tests that the last expenses table view is not null and is properly initialized. */
  @Test
  public void testLastExpensesNotNull() {
    lastExpenses = find("#grdExpenseHistory");
    assertNotNull(lastExpenses);
  }

  /**
   * Tests the content of the "lastExpenses" table view, verifying the correctness of date,
   * category, description, and price for the first row.
   */
  @Test
  public void testLastExpenses() {
    lastExpenses = find("#grdExpenseHistory");
    assertEquals(
        2, lastExpenses.getItems().size(), "The last expense table should contain two items");

    LocalDate date1 = LocalDate.of(2020, 1, 1);
    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String date1String = date1.format(formatter1);

    ObservableList<TableColumn<Expense, ?>> columns = lastExpenses.getColumns();
    for (TableColumn<Expense, ?> column : columns) {
      Expense row = lastExpenses.getItems().get(0);
      if ("Date".equals(column.getText())) {
        assertEquals("Date", column.getText());
        assertEquals(date1String, row.getDate(), "Wrong date on index 0");
      } else if ("Category".equals(column.getText())) {
        assertEquals("Category", column.getText(), "Cannot find the category column");
        assertEquals("Sports", row.getCategory(), "Wrong category on index 0");
      } else if ("Description".equals(column.getText())) {
        assertEquals("Description", column.getText(), "Cannot find the description column");
        assertEquals("Running shoes", row.getDescription(), "Wrong description on index 0");
      } else if ("Price".equals(column.getText())) {
        assertEquals("Price", column.getText(), "Cannot find the price column");
        assertEquals(1200.00, row.getPrice(), "Wrong price on index 0");
      }
    }
  }

  /** Tests if the displayed total price in the UI matches "Total price: 1600,00 kr". */
  @Test
  public void testTotalPrice() {
    Labeled totalPrice = find("#totalPriceLabel");
    assertEquals(
        "Total price: 1600.00 kr", totalPrice.getText(), "The total price should be 1600.00 kr");
  }
}
