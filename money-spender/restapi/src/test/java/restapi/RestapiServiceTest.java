package restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import core.Expense;
import core.ExpenseService;
import core.User;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.JsonController;

/** JUnit test class for {@link RestapiService} class. */
@ExtendWith(MockitoExtension.class)
public class RestapiServiceTest {

  @Mock private JsonController jsonController;
  @Mock private ExpenseService expenseService;
  @InjectMocks private RestapiService restapiService;

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String TEST_FILE_PATH = "testFilePath.json";
  private User testUser;
  private Expense testExpense;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    testUser = new User("username", "password");
    testExpense = new Expense(LocalDate.now(), "category", 10.0, "description");
  }

  private Map<String, String> createExpenseCredentials(
      String category, String dropDownCategory, String price) {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("date", "01.01.2021");
    credentials.put("newCategory", category);
    credentials.put("dropDownCategory", dropDownCategory);
    credentials.put("price", price);
    credentials.put("description", "Grocery shopping");
    return credentials;
  }

  private void mockUserAndExpenseServiceBehavior() throws IOException {
    when(jsonController.getUser(USERNAME)).thenReturn(testUser);
    doNothing().when(expenseService).addExpenseForUser(any(User.class), any(Expense.class));
    doNothing().when(jsonController).saveNewUserToJson(any(User.class));
  }

  /**
   * Test the creation of a new user if the username doesn't exist.
   *
   * @throws IOException if there's an issue with creating the user.
   */
  @Test
  void testCreateUserIfExistsAndNonExistence() throws IOException {
    when(jsonController.checkIfUserNameExists(USERNAME)).thenReturn(false);
    User nonExistingUserResult = restapiService.createUserIfNotExists(USERNAME, PASSWORD);
    assertNotNull(nonExistingUserResult, "Newly created user should not be null");
    assertEquals(USERNAME, nonExistingUserResult.getUsername(), "Username should match");

    when(jsonController.checkIfUserNameExists("existingUser")).thenReturn(true);
    User existingUserResult = restapiService.createUserIfNotExists("existingUser", "password");
    assertNull(existingUserResult, "Should return null for existing user");
  }

  /**
   * Test user authentication scenarios, including correct authentication, non-existing user, and
   * incorrect password.
   *
   * @throws IOException if there's an issue with user authentication.
   */
  @Test
  void testAuthenticateUserScenarios() throws IOException {
    when(jsonController.checkIfUserNameExists(USERNAME)).thenReturn(true);
    when(jsonController.checkIfPasswordIsCorrect(USERNAME, PASSWORD)).thenReturn(true);
    when(jsonController.getUser(USERNAME)).thenReturn(new User(USERNAME, PASSWORD));

    User resultWithCorrectPassword = restapiService.authenticateUser(USERNAME, PASSWORD);
    assertNotNull(resultWithCorrectPassword, "User should be authenticated with correct password");
    assertEquals(
        USERNAME, resultWithCorrectPassword.getUsername(), "Authenticated username should match");

    reset(jsonController);

    when(jsonController.checkIfUserNameExists(USERNAME)).thenReturn(false);

    User resultWithUserNotExist = restapiService.authenticateUser(USERNAME, PASSWORD);
    assertNull(resultWithUserNotExist, "Non-existing user should not be authenticated");

    when(jsonController.checkIfUserNameExists(USERNAME)).thenReturn(true);
    when(jsonController.checkIfPasswordIsCorrect(USERNAME, PASSWORD)).thenReturn(false);

    User resultWithIncorrectPassword = restapiService.authenticateUser(USERNAME, PASSWORD);
    assertNull(
        resultWithIncorrectPassword, "User should not be authenticated with incorrect password");
  }

  /**
   * Test the creation of a new expense with various scenarios, including valid input, invalid
   * category combinations, and invalid price format.
   *
   * @throws IOException if there's an issue with expense creation or validation.
   */
  @Test
  void createNewExpense_AllBranches() throws IOException {
    String username = "testUser";
    Map<String, String> credentials = new HashMap<>();
    credentials.put("date", "01.01.2021");
    credentials.put("description", "Grocery shopping");

    credentials.put("newCategory", "Groceries");
    credentials.put("dropDownCategory", null);
    credentials.put("price", "100.00");

    User user = new User(username, "password");
    when(jsonController.getUser(username)).thenReturn(user);
    doNothing().when(expenseService).addExpenseForUser(any(User.class), any(Expense.class));
    doNothing().when(jsonController).saveNewUserToJson(any(User.class));

    User result = restapiService.createNewExpense(username, credentials);
    assertNotNull(result);

    credentials.put("newCategory", "Groceries");
    credentials.put("dropDownCategory", "Food");

    assertThrows(
        IllegalArgumentException.class,
        () -> restapiService.createNewExpense(username, credentials));

    credentials.put("newCategory", "Groceries");
    credentials.put("dropDownCategory", null);
    credentials.put("price", "invalid_price");

    assertThrows(
        NumberFormatException.class, () -> restapiService.createNewExpense(username, credentials));
  }

  /**
   * Test the retrieval of all users from JSON storage.
   *
   * @throws IOException if there's an issue with reading users from JSON.
   */
  @Test
  void testGetAllUsers() throws IOException {
    List<User> expectedUsers =
        Arrays.asList(new User("user", "password1"), new User("user", "password2"));

    when(jsonController.readUsersFromJson()).thenReturn(expectedUsers);

    List<User> result = restapiService.getAllUsers();

    assertNotNull(result, "The result should not be null.");
    assertEquals(
        expectedUsers.size(), result.size(), "The number of users should match the expected size.");
    assertEquals(expectedUsers, result, "The returned user list should match the expected list.");

    verify(jsonController).readUsersFromJson();
  }

  /**
   * Test setting the file path for JSON storage.
   *
   * @throws IOException if there's an issue with setting the file path.
   */
  @Test
  void testSetFilePath() throws IOException {
    restapiService.setFilePath(TEST_FILE_PATH);
    verify(jsonController).setFilePath(TEST_FILE_PATH);
  }

  /**
   * Test retrieving all expenses for a user from JSON storage.
   *
   * @throws IOException if there's an issue with reading data from JSON.
   */
  @Test
  void testGetAllExpensesForUser() throws IOException {
    when(jsonController.getUser(anyString())).thenReturn(testUser);
    when(expenseService.getAllExpensesForUser(any(User.class)))
        .thenReturn(Collections.singletonList(testExpense));

    List<Expense> result = restapiService.getAllExpensesForUser("username");
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(testExpense, result.get(0));
  }

  /**
   * Test deleting an expense for a user from JSON storage.
   *
   * @throws IOException if there's an issue with reading or writing data to JSON.
   */
  @Test
  void testDeleteExpenseForUser() throws IOException {
    when(jsonController.getUser("username")).thenReturn(testUser);
    when(expenseService.findExpense(testUser, testExpense)).thenReturn(testExpense);
    when(expenseService.removeExpenseForUser(any(User.class), any(Expense.class)))
        .thenReturn(Boolean.TRUE);
    doNothing().when(jsonController).saveNewUserToJson(testUser);
    User result = restapiService.deleteExpenseForUser("username", testExpense);
    assertNotNull(result);
    assertEquals("username", result.getUsername());
    verify(jsonController).getUser("username");
    verify(expenseService).findExpense(testUser, testExpense);
    verify(expenseService).removeExpenseForUser(testUser, testExpense);
    verify(jsonController).saveNewUserToJson(testUser);
  }

  /**
   * Test retrieving categories for a user from JSON storage.
   *
   * @throws IOException if there's an issue with reading data from JSON.
   */
  @Test
  void getCategoriesForUser_Successful() throws IOException {
    String username = testUser.getUsername();
    Set<String> expectedCategories = new HashSet<>(Arrays.asList("Food", "Travel", "Utilities"));

    when(jsonController.getUser(username)).thenReturn(testUser);
    when(expenseService.getCategoriesForUser(testUser)).thenReturn(expectedCategories);

    Set<String> result = restapiService.getCategoriesForUser(username);

    assertNotNull(result);
    assertEquals(expectedCategories, result);
    verify(jsonController).getUser(username);
    verify(expenseService).getCategoriesForUser(testUser);
  }

  /** Test calculating the total expenses value for a list of expenses. */
  @Test
  void getTotalExpensesValueOfList_Successful() {
    List<Expense> expenses =
        Arrays.asList(
            new Expense(LocalDate.now(), "Groceries", 50.00, "Grocery shopping"),
            new Expense(LocalDate.now(), "Utilities", 75.00, "Electricity bill"),
            new Expense(LocalDate.now(), "Entertainment", 40.00, "Movie tickets"));

    double expectedTotal = 165.00;
    when(expenseService.calculateTotalExpenseValueForList(expenses)).thenReturn(expectedTotal);

    double result = restapiService.getTotalExpensesValueOfList(expenses);

    assertEquals(expectedTotal, result, 0.01);
    verify(expenseService).calculateTotalExpenseValueForList(expenses);
  }

  /**
   * Test filtering expenses for a user based on selected category and date range.
   *
   * @throws IOException if there's an issue with reading data from JSON.
   */
  @Test
  void filterExpensesForUser_Successful() throws IOException {
    String username = "testUser";
    String selectedCategory = "Utilities";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = LocalDate.of(2022, 1, 31);

    List<Expense> filteredExpenses =
        Arrays.asList(
            new Expense(start.format(formatter), selectedCategory, 120.00, "Electricity Bill"),
            new Expense(
                start.plusDays(19).format(formatter), selectedCategory, 60.00, "Water Bill"));

    User user = new User(username, "password");
    when(jsonController.getUser(username)).thenReturn(user);
    when(expenseService.filterExpensesForUser(user, selectedCategory, start, end))
        .thenReturn(filteredExpenses);

    List<Expense> result =
        restapiService.filterExpensesForUser(username, selectedCategory, start, end);

    assertNotNull(result);
    assertEquals(filteredExpenses.size(), result.size());
    assertEquals(filteredExpenses, result);
    verify(jsonController).getUser(username);
    verify(expenseService).filterExpensesForUser(user, selectedCategory, start, end);
  }

  /**
   * Test creating a new expense with an empty category and dropdown, which should throw an
   * IllegalArgumentException.
   */
  @Test
  void createNewExpense_EmptyCategoryAndDropDown_ThrowsIllegalArgumentException() {
    Map<String, String> credentials = createExpenseCredentials("", null, "100.00");

    assertThrows(
        IllegalArgumentException.class,
        () -> restapiService.createNewExpense(testUser.getUsername(), credentials),
        "Expected IllegalArgumentException for empty category and dropdown.");
  }

  /**
   * Test creating a new expense with an empty category and a valid dropdown value, which should
   * return a User object.
   *
   * @throws IOException if there's an issue with reading data from JSON.
   */
  @Test
  void createNewExpense_EmptyCategory_ValidDropDown_ReturnsUser() throws IOException {
    Map<String, String> credentials = createExpenseCredentials("", "Food", "100.00");

    mockUserAndExpenseServiceBehavior();

    User result = restapiService.createNewExpense(testUser.getUsername(), credentials);
    assertNotNull(result);
  }

  /**
   * Test creating a new expense with a valid category and an empty dropdown, which should return a
   * User object.
   *
   * @throws IOException if there's an issue with reading data from JSON.
   */
  @Test
  void createNewExpense_ValidCategory_EmptyDropDown_ReturnsUser() throws IOException {
    Map<String, String> credentials = createExpenseCredentials("Food", null, "100.00");

    mockUserAndExpenseServiceBehavior();

    User result = restapiService.createNewExpense(testUser.getUsername(), credentials);
    assertNotNull(result);
  }

  /**
   * Test creating a new expense with both a category and a dropdown, which should throw an
   * IllegalArgumentException.
   */
  @Test
  void createNewExpense_BothCategoryAndDropDown_ThrowsIllegalArgumentException() {
    Map<String, String> credentials = createExpenseCredentials("Groceries", "Food", "100.00");

    assertThrows(
        IllegalArgumentException.class,
        () -> restapiService.createNewExpense(testUser.getUsername(), credentials),
        "Expected IllegalArgumentException for both category and dropdown provided.");
  }

  /**
   * Test creating a new expense with an empty price, which should throw an
   * IllegalArgumentException.
   */
  @Test
  void createNewExpense_EmptyPrice_ThrowsIllegalArgumentException() {
    Map<String, String> credentials = createExpenseCredentials("Groceries", null, "");

    assertThrows(
        IllegalArgumentException.class,
        () -> restapiService.createNewExpense(testUser.getUsername(), credentials),
        "Expected IllegalArgumentException for empty price.");
  }

  /**
   * Test creating a new expense with an invalid price format, which should throw a
   * NumberFormatException.
   */
  @Test
  void createNewExpense_InvalidPriceFormat_ThrowsNumberFormatException() {
    Map<String, String> credentials = createExpenseCredentials("Groceries", null, "invalid_price");

    assertThrows(
        NumberFormatException.class,
        () -> restapiService.createNewExpense(testUser.getUsername(), credentials),
        "Expected NumberFormatException for invalid price format.");
  }

  /**
   * Test creating a new expense with a valid price, which should return a User object.
   *
   * @throws IOException if there's an issue with reading data from JSON.
   */
  @Test
  void createNewExpense_ValidPrice_ReturnsUser() throws IOException {
    Map<String, String> credentials = createExpenseCredentials("Groceries", null, "100.00");

    mockUserAndExpenseServiceBehavior();

    User result = restapiService.createNewExpense(testUser.getUsername(), credentials);
    assertNotNull(result);
  }
}
