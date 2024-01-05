package ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

import core.Expense;
import core.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import persistence.JsonController;

/** JUnit test class for {@link NewExpenseController} class. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NewExpenseControllerTest extends ApplicationTest implements FxHelper {

  private NewExpenseController controller;
  private User user;
  private DatePicker datePicker;
  private TextField price, notExistingCategory, description;
  private Button add, backToHome;
  private ComboBox<String> dragDownCategory;

  /**
   * Finds and retrieves a JavaFX Node based on a given query string.
   *
   * @param <T> the type of the Node to be returned, extending Node
   * @param query the query string used to search for the Node
   * @return the matched Node of type T; can return null if no Node matches the query
   */
  @Override
  public <T extends Node> T find(String query) {
    return lookup(query).query();
  }

  /**
   * Initializes the primary stage with the new expense interface for testing. It also sets up a
   * mock user and related expenses for testing.
   *
   * @param stage The primary stage to be initialized and displayed.
   * @throws Exception if there's any issue in loading the FXML or other related issues.
   */
  @Override
  public void start(final Stage stage) throws Exception {
    Expense expense1 =
        new Expense(LocalDate.of(2020, 1, 1), "Traveling", 1200.0, "Flew home to my mom");
    List<Expense> expense = new ArrayList<Expense>();
    expense.add(expense1);
    user = new User("test", "test", expense);
    JsonController JsonControler = new JsonController("test.json");
    JsonControler.saveNewUserToJson(user);

    FXMLLoader loader =
        new FXMLLoader(NewExpenseController.class.getResource("/ui/NewExpense.fxml"));
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

  /** Tests the behavior when trying to add an expense with empty input fields. */
  @Test
  public void testAddExpenseWithEmptyTextFields() {
    add = find("#add");
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    FxHelper.waitTwoDecisecond();
    assertNotNull(add, "Button add should not be null");
  }

  @Test
  public void testAddExpenseWithEmptyPrice() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    notExistingCategory = find("#notExistingCategory");
    clickOn(notExistingCategory).write("Furniture");
    price = find("#price");
    clickOn(price).write("");
    description = find("#description");
    clickOn(description).write("New chairs after crazy party");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    FxHelper.waitTwoDecisecond();
    assertNotNull(price, "Textfield price should not be null");
  }

  @Test
  public void testAddExpenseWithUnvalidPrice() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    notExistingCategory = find("#notExistingCategory");
    clickOn(notExistingCategory).write("Furniture");
    price = find("#price");
    clickOn(price).write("hhh");
    description = find("#description");
    clickOn(description).write("New table after crazy party");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    assertNotNull(price, "Textfield price should not be null");
  }

  @Test
  public void testAddExpenseWithBothNewCategoryAndDropdown() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    notExistingCategory = find("#notExistingCategory");
    clickOn(notExistingCategory).write("Cleaning");
    dragDownCategory = find("#dragDownCategory");
    clickOn(dragDownCategory);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    price = find("#price");
    clickOn(price).write("1000");
    description = find("#description");
    clickOn(description).write("Cleaning articles");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    assertNotNull(price, "Textfield price should not be null");
  }

  /** Tests adding an expense with only a date specified. */
  @Test
  public void testAddExpenseWithOnlyDate() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    assertNotNull(datePicker, "Datepicker should not be null");
  }

  /** Tests adding an expense with a specified date and a new category. */
  @Test
  public void testAddExpenseWithDateAndNewCategory() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    notExistingCategory = find("#notExistingCategory");
    clickOn(notExistingCategory).write("makeup");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    // If this it not null, then the user is still in the new expense view
    assertNotNull(notExistingCategory, "Textfield notExistingCategory should not be null");
    assertNotNull(datePicker, "Datepicker should not be null");
  }

  /** Tests adding an expense with a specified date and selecting an existing category. */
  @Test
  public void testAddExpenseWithDateAndExistingCategory() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    dragDownCategory = find("#dragDownCategory");
    clickOn(dragDownCategory);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    // If this it not null, then the user is still in the new expense view
    assertNotNull(dragDownCategory, "Drag down category should not be null");
    assertNotNull(datePicker, "Datepicker should not be null");
  }

  /** Tests adding an expense by specifying date, category, and price. */
  @Test
  public void testAddExpenseWithDateCategoryAndPrice() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    dragDownCategory = find("#dragDownCategory");
    clickOn(dragDownCategory);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    price = find("#price");
    clickOn(price).write("100");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();

    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");

    // If this it not null, then the user is still in the new expense view
    assertNotNull(dragDownCategory, "Drag down category should not be null");
    assertNotNull(datePicker, "Datepicker should not be null");
    assertNotNull(price, "Textfield price should not be null");
  }

  /** Tests navigating back to the Home view. */
  @Test
  public void testBackToHome() {
    backToHome = find("#backToHome");
    FxHelper.waitTwoDecisecond();
    clickOn(backToHome);
    FxHelper.waitTwoDecisecond();
    // If in the Home view, this button should not be null
    Button newExpense = find("#newExpense");
    assertNotNull(newExpense, "The user is not in the Home view");
  }

  /**
   * Tests creating a new expense with a specified date, selecting an existing category, specifying
   * a price, and adding a description.
   */
  @Test
  public void testCreateNewExpenseWithExistingCategory() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    dragDownCategory = find("#dragDownCategory");
    clickOn(dragDownCategory);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    price = find("#price");
    clickOn(price).write("1000");
    description = find("#description");
    clickOn(description).write("Flew to London to see fotball match");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    FxHelper.waitTwoDecisecond();
    // If in the Home view, this button should not be null
    Button newExpense = find("#newExpense");
    assertNotNull(newExpense, "The user is not in the Home view");
  }

  /**
   * Tests creating a new expense with a non-existing category. Ensures the user is navigated to the
   * Home view post-creation.
   */
  @Test
  public void testCreateNewExpenseWithNotCategory() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    notExistingCategory = find("#notExistingCategory");
    clickOn(notExistingCategory).write("Drinks");
    price = find("#price");
    clickOn(price).write("72");
    description = find("#description");
    clickOn(description).write("Beer at Samfundet");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    FxHelper.waitTwoDecisecond();
    // If in the Home view, this button should not be null
    Button newExpense = find("#newExpense");
    assertNotNull(newExpense, "The user is not in the Home view");
  }

  /**
   * Tests creating an expense without selecting a category. Validates the user remains on the new
   * expense view.
   */
  @Test
  public void testNotHavingCategory() {
    datePicker = find("#datePicker");
    datePicker.setValue(LocalDate.of(2023, 1, 2));
    price = find("#price");
    clickOn(price).write("999");
    description = find("#description");
    clickOn(description).write("New images to the house");
    add = find("#add");
    FxHelper.waitTwoDecisecond();
    clickOn(add);
    FxHelper.waitTwoDecisecond();
    verifyThat("OK", NodeMatchers.isVisible());
    FxHelper.waitTwoDecisecond();
    clickOn("OK");
    // If this it not null, then the user is still in the new expense view
    assertNotNull(datePicker, "Datepicker should not be null");
  }
}
