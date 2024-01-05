package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.Expense;
import core.ListAndValueContainer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ApiClientTest {

  @InjectMocks private ApiClient apiClient;

  @BeforeEach
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    apiClient = new ApiClient();
    apiClient.setFilePath("test.json");
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
   * Validates user creation behavior in {@code ApiClient}. Ensures non-null response for new user
   * creation, throws {@code IllegalArgumentException} for duplicate or invalid usernames.
   * Specifically, it checks:
   *
   * <ol>
   *   <li>The non-existence of a null user object post-creation attempt with a unique username.
   *   <li>The presence of an exception for a repeated username, flagged as "Username already
   *       exists".
   *   <li>The triggering of an exception for invalid username criteria, marked as "Illegal input".
   * </ol>
   *
   * @throws IllegalArgumentException if username is already taken or fails validation
   */
  @Order(1)
  @Test
  public void testGenerateUser() {
    String username = "testuser";
    assertNotNull(apiClient.createUser(username, username), "User should not be null");
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              apiClient.createUser(username, username);
            },
            "User should exist");

    assertTrue(exception.getMessage().contains("Username already exists."));
    IllegalArgumentException badException =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              apiClient.createUser("usernameWithNumbers99", "BadPassword");
            },
            "This is not a valid username");

    assertTrue(badException.getMessage().contains("Illegal input."));
  }

  /**
   * Tests retrieval of a user by username from the {@code ApiClient}. Asserts that the username of
   * the retrieved user matches the expected username provided for the lookup.
   *
   * @order 2 Specifies the order in which this test method is executed.
   * @see ApiClient#getUserByUsername(String)
   */
  @Order(2)
  @Test
  public void testGetUserByUsername() {
    String username = "testuser";
    assertEquals(
        username,
        apiClient.getUserByUsername(username).getUsername(),
        "Username should be the same");
  }

  /**
   * Tests successful and failed login attempts in {@code ApiClient}. Asserts successful login
   * matches the provided username and failed login throws an exception with an appropriate message.
   *
   * @order 3 Execution order of this test method.
   */
  @Order(3)
  @Test
  public void testLogIn() {
    String username = "testuser";
    assertEquals(
        username,
        apiClient.logInUser(username, username).getUsername(),
        "This should be the same username");
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              apiClient.logInUser("usernameWithNumbers99", "BadPassword");
            },
            "This should not be a valid username and password");

    assertTrue(exception.getMessage().contains("Invalid username or password."));
  }

  /**
   * Validates the addition of an expense for a user using {@code ApiClient}. It checks that the
   * correct number of expenses is recorded after the operation, and verifies that input errors are
   * properly handled with exceptions.
   *
   * @order 4 Defines the execution order of this test.
   * @throws IllegalArgumentException If the expense addition is invalid.
   * @throws IOException If an I/O error occurs during the addition.
   * @throws RuntimeException If an unexpected error occurs.
   */
  @Order(4)
  @Test
  public void testAddExpenseForUser()
      throws IllegalArgumentException, IOException, RuntimeException {
    HashMap<String, String> expenseCreditentials = new HashMap<>();
    expenseCreditentials.put("date", "01.11.2023");
    expenseCreditentials.put("newCategory", "refreshments");
    expenseCreditentials.put("dropDownCategory", null);
    expenseCreditentials.put("price", "29");
    expenseCreditentials.put("description", "One Coke");
    assertEquals(
        1,
        apiClient
            .addExpenseForUser("testuser", expenseCreditentials)
            .getExpenseHandler()
            .getAllExpenses()
            .size(),
        "The user should have 1 expense");

    expenseCreditentials.replace("price", "number");

    NumberFormatException exception =
        assertThrows(
            NumberFormatException.class,
            () -> {
              apiClient.addExpenseForUser("testuser", expenseCreditentials);
            },
            "This should result in error because price is not a number");
    assertTrue(exception.getMessage().contains("Please provide a valid price."));
    expenseCreditentials.replace("price", "");

    IllegalArgumentException exception2 =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              apiClient.addExpenseForUser("testuser", expenseCreditentials);
            },
            "This should result in error because price is null");
    assertTrue(exception2.getMessage().contains("Illegal input."));
  }

  /**
   * Tests the deletion of an expense for a user through {@code ApiClient}. Asserts that the expense
   * count is zero after deletion.
   *
   * @order 5 Indicates the order in which this test is run.
   */
  @Order(5)
  @Test
  public void testDeleteExpenseForUser() {
    LocalDate date = LocalDate.parse("01.11.2023", DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    Expense deleteExpense = new Expense(date, "refreshments", 29.00, "One Coke");
    assertEquals(
        0,
        apiClient
            .deleteExpenseForUser("testuser", deleteExpense)
            .getExpenseHandler()
            .getAllExpenses()
            .size(),
        "This should 0 expenses");
  }

  /**
   * Tests adding and retrieving expenses for a user through {@code ApiClient}. Confirms that the
   * added expense is correctly retrieved afterward.
   *
   * @order 6 Specifies the execution sequence for this test method.
   * @throws IllegalArgumentException If the argument passed is not appropriate.
   * @throws IOException If an I/O exception occurs during the process.
   * @throws RuntimeException If an unexpected runtime exception occurs.
   */
  @Order(6)
  @Test
  public void testExpensesForUser() throws IllegalArgumentException, IOException, RuntimeException {
    HashMap<String, String> expenseCreditentials = new HashMap<>();
    expenseCreditentials.put("date", "01.11.2023");
    expenseCreditentials.put("newCategory", "Presents");
    expenseCreditentials.put("dropDownCategory", null);
    expenseCreditentials.put("price", "200");
    expenseCreditentials.put("description", "Presents to my brother");
    Expense expense =
        apiClient
            .addExpenseForUser("testuser", expenseCreditentials)
            .getExpenseHandler()
            .getAllExpenses()
            .get(0);
    assertEquals(
        expense,
        apiClient.getExpensesFromUser("testuser").get(0),
        "This should be the same expense");
  }

  /**
   * Tests retrieval of expense categories for a specific user via {@code ApiClient}. Asserts that
   * the expected category is present and the total number of categories is correct.
   *
   * @order 7 Specifies this test's execution order.
   */
  @Order(7)
  @Test
  public void testGetCategoriesForUser() {
    HashMap<String, String> expenseCreditentials = new HashMap<>();
    expenseCreditentials.put("date", "01.12.2023");
    expenseCreditentials.put("newCategory", "Food");
    expenseCreditentials.put("dropDownCategory", null);
    expenseCreditentials.put("price", "200");
    expenseCreditentials.put("description", "Apple");
    apiClient.addExpenseForUser("testuser", expenseCreditentials);
    Set<String> categories = apiClient.getCategoriesForUser("testuser");
    assertTrue(categories.contains("Food"));
    assertEquals(2, categories.size(), "This should be 2");
  }

  /**
   * Verifies the functionality to filter a user's expenses by category and date range in {@code
   * ApiClient}. Checks that one expense is returned when filtering by a specific category and date
   * range.
   *
   * @order 8 Indicates the order of this test method during execution.
   */
  @Order(8)
  @Test
  public void testFilterExpensesForUser() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate startDate = LocalDate.parse("10.10.2023", formatter);
    LocalDate endDate = LocalDate.parse("01.12.2023", formatter);

    assertEquals(
        1,
        apiClient.filterExpensesForUser("testuser", "Presents", startDate, endDate).size(),
        "This should be 1");
    assertEquals(
        1,
        apiClient.filterExpensesForUser("testuser", "Presents", startDate, null).size(),
        "This should be 1");
  }

  /**
   * Tests the retrieval of filtered expenses and their total value for a user through {@code
   * ApiClient}. Asserts the correct operation of filtering by category and date, ensuring that the
   * number of expenses and total value are as expected. It also checks that an exception is thrown
   * if the date range is invalid.
   *
   * @order 9 Specifies the test's execution order.
   * @throws IllegalArgumentException If date range is invalid or input is otherwise incorrect.
   * @throws IOException If an I/O error occurs in the process.
   * @throws RuntimeException If any other runtime exception occurs during the method execution.
   */
  @Order(9)
  @Test
  public void testGetFilteredExpensesAndValue()
      throws IllegalArgumentException, IOException, RuntimeException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate startDate = LocalDate.parse("10.10.2023", formatter);
    LocalDate endDate = LocalDate.parse("01.12.2023", formatter);

    ListAndValueContainer response =
        apiClient.getFilteredExpensesAndValue("testuser", "Food", startDate, endDate);

    assertEquals(1, response.getExpenses().size(), "This should be 1");
    assertThrows(
        IllegalArgumentException.class,
        () -> apiClient.getFilteredExpensesAndValue("testuser", "Presents", endDate, startDate),
        "This should result in error because start date is after end date");
    assertEquals(
        1,
        apiClient
            .getFilteredExpensesAndValue("testuser", "Presents", startDate, null)
            .getExpenses()
            .size(),
        "This shuld be 1");
  }
}
