package restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Expense;
import core.ListAndValueContainer;
import core.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** JUnit test class for {@link RestapiController} class. */
public class RestapiControllerTest {

  private static final String USER_CREATE_ENDPOINT = "/moneyspender/user/create";
  private static final String USER_AUTHENTICATE_ENDPOINT = "/moneyspender/user/authenticate";
  private static final String GET_USER_ENDPOINT = "/moneyspender/user/";
  private static final String ADD_EXPENSE_ENDPOINT = "/moneyspender/expense/add/";
  private static final String DELETE_EXPENSE_ENDPOINT = "/moneyspender/expense/delete/";
  private static final String GET_FILTERED_EXPENSES_ENDPOINT = "/moneyspender/expense/filter/";
  private static final String GET_ALL_EXPENSES_ENDPOINT = "/moneyspender/expense/";

  @Mock private RestapiService restapiService;

  @InjectMocks private RestapiController restapiController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  private String username;
  private String password;
  private Map<String, String> credentials;
  private String category;
  private LocalDate start;
  private LocalDate end;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(restapiController).build();
    objectMapper = new ObjectMapper();

    username = "testUser";
    password = "testPassword";
    credentials = new HashMap<>();
    credentials.put("username", username);
    credentials.put("password", password);
    category = "Category1";
    start = LocalDate.parse("2023-01-01");
    end = LocalDate.parse("2023-02-01");
  }

  /** Test for successful user creation. */
  @Test
  void testCreateUserSuccessfully() throws Exception {
    User createdUser = new User(username, password);
    when(restapiService.createUserIfNotExists(username, password)).thenReturn(createdUser);

    mockMvc
        .perform(
            post(USER_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username));
  }

  /** Test for setting file path. */
  @Test
  void testSetFilePath() throws IOException {
    String filePath = "testFilePath";
    restapiController.setFilePath(filePath);
    verify(restapiService).setFilePath(filePath);
  }

  /** Test for user creation when user already exists. */
  @Test
  void testcreateUserWhenUserAlreadyExists() throws Exception {
    when(restapiService.createUserIfNotExists(username, password)).thenReturn(null);

    mockMvc
        .perform(
            post(USER_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isNotAcceptable());
  }

  /** Test for user creation when IllegalArgumentException is thrown. */
  @Test
  void testCreateUserWhenIllegalArgumentExceptionThrown() throws Exception {
    when(restapiService.createUserIfNotExists(username, password))
        .thenThrow(new IllegalArgumentException());

    mockMvc
        .perform(
            post(USER_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isBadRequest());
  }

  /** Test for user creation when IOException is thrown. */
  @Test
  void testCreateUserWhenIOExceptionThrown() throws Exception {
    when(restapiService.createUserIfNotExists(username, password)).thenThrow(new IOException());

    mockMvc
        .perform(
            post(USER_CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isInternalServerError());
  }

  /** Test for successful user authentication. */
  @Test
  void testAuthenticateUserSuccessfully() throws Exception {
    User authenticatedUser = new User(username, password);
    when(restapiService.authenticateUser(username, password)).thenReturn(authenticatedUser);

    mockMvc
        .perform(
            post(USER_AUTHENTICATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username));
  }

  /** Test for failed user authentication. */
  @Test
  void testAuthenticateUserFailed() throws Exception {
    when(restapiService.authenticateUser(username, password)).thenReturn(null);

    mockMvc
        .perform(
            post(USER_AUTHENTICATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isUnauthorized());
  }

  /** Test for getting a user by username successfully. */
  @Test
  void testGetUserByUsernameSuccessfully() throws Exception {
    User user = new User(username, password);
    when(restapiService.getUserByUsername(username)).thenReturn(user);

    mockMvc
        .perform(get(GET_USER_ENDPOINT + username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username));
  }

  /** Test for getting all expenses for a username successfully. */
  @Test
  void testGetAllExpensesForUsernameSuccessfully() throws Exception {
    List<Expense> expenses =
        Collections.singletonList(new Expense(LocalDate.now(), "category", 100.00, "description"));
    when(restapiService.getAllExpensesForUser(username)).thenReturn(expenses);

    mockMvc
        .perform(get(GET_ALL_EXPENSES_ENDPOINT + username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].category").value("category"));
  }

  /** Test for adding an expense for a user successfully. */
  @Test
  void testAddExpenseForUserSuccessfully() throws Exception {
    User user = new User(username, password);
    when(restapiService.createNewExpense(eq(username), any())).thenReturn(user);

    mockMvc
        .perform(
            post(ADD_EXPENSE_ENDPOINT + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username));
  }

  /** Test for adding an expense for a user when IllegalArgumentException is thrown. */
  @Test
  void testAddExpenseForUserWhenIllegalArgumentException() throws Exception {
    when(restapiService.createNewExpense(eq(username), any()))
        .thenThrow(new IllegalArgumentException());

    mockMvc
        .perform(
            post(ADD_EXPENSE_ENDPOINT + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isBadRequest());
  }

  /** Test for adding an expense with a RuntimeException. */
  @Test
  void testAddExpenseForUser_RuntimeException() throws Exception {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("key", "value");

    when(restapiService.createNewExpense(eq(username), any())).thenThrow(RuntimeException.class);

    mockMvc
        .perform(
            post(ADD_EXPENSE_ENDPOINT + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isInternalServerError());
  }

  /** Test for adding an expense with a NumberFormatException. */
  @Test
  void testAddExpenseForUserWhenNumberFormatException() throws Exception {
    when(restapiService.createNewExpense(eq(username), any()))
        .thenThrow(new NumberFormatException());

    mockMvc
        .perform(
            post(ADD_EXPENSE_ENDPOINT + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isNotAcceptable());
  }

  /** Test for adding an expense with an IOException. */
  @Test
  void testAddExpenseForUserWhenIOException() throws Exception {
    when(restapiService.createNewExpense(eq(username), any())).thenThrow(new IOException());

    mockMvc
        .perform(
            post(ADD_EXPENSE_ENDPOINT + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
        .andExpect(status().isExpectationFailed());
  }

  /** Test for creating, deleting, and filtering expenses. */
  @Test
  void testCreateExpenseDeleteExpenseAndFilterExpenses() throws Exception {
    String username = "testUser";
    Expense testExpense = new Expense(LocalDate.now(), "TestCategory", 50.0, "TestDescription");

    User user = new User(username, "password");
    when(restapiService.createNewExpense(eq(username), any())).thenReturn(user);

    mockMvc
        .perform(
            post(ADD_EXPENSE_ENDPOINT + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testExpense)))
        .andExpect(status().isOk());

    when(restapiService.deleteExpenseForUser(eq(username), any(Expense.class))).thenReturn(user);

    mockMvc
        .perform(
            delete(DELETE_EXPENSE_ENDPOINT + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testExpense)))
        .andExpect(status().isBadRequest());

    String categoryToFilter = "TestCategory";
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 12, 31);
    List<Expense> expectedExpenses = Collections.singletonList(testExpense);

    when(restapiService.filterExpensesForUser(
            eq(username), eq(categoryToFilter), eq(startDate), eq(endDate)))
        .thenReturn(expectedExpenses);

    mockMvc
        .perform(
            get(GET_FILTERED_EXPENSES_ENDPOINT + username)
                .param("category", categoryToFilter)
                .param("start", startDate.toString())
                .param("end", endDate.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].category").value(testExpense.getCategory()));
  }

  /** Test for getting filtered expenses and their total value successfully. */
  @Test
  void testGetFilteredExpensesAndValueForUser_Success() throws IOException {
    List<Expense> expectedFilteredExpenses = new ArrayList<>();
    double expectedTotalValue = 100.0;

    when(restapiService.filterExpensesForUser(username, category, start, end))
        .thenReturn(expectedFilteredExpenses);
    when(restapiService.getTotalExpensesValueOfList(expectedFilteredExpenses))
        .thenReturn(expectedTotalValue);

    ResponseEntity<ListAndValueContainer> response =
        restapiController.getFilteredExpensesAndValueForUser(username, category, start, end);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedFilteredExpenses, response.getBody().getExpenses());
    assertEquals(expectedTotalValue, response.getBody().getTotal(), 0.01);
  }

  /** Test for getting filtered expenses and their total value with IllegalArgumentException. */
  @Test
  void testGetFilteredExpensesAndValueForUser_IllegalArgumentException() throws IOException {
    when(restapiService.filterExpensesForUser(username, category, start, end))
        .thenThrow(IllegalArgumentException.class);
    ResponseEntity<ListAndValueContainer> response =
        restapiController.getFilteredExpensesAndValueForUser(username, category, start, end);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  /** Test for getting filtered expenses and their total value with IOException. */
  @Test
  void testGetFilteredExpensesAndValueForUser_IOException() throws IOException {
    when(restapiService.filterExpensesForUser(username, category, start, end))
        .thenThrow(IOException.class);
    ResponseEntity<ListAndValueContainer> response =
        restapiController.getFilteredExpensesAndValueForUser(username, category, start, end);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  /** Test for getting filtered expenses and their total value with no parameters. */
  @Test
  void testGetFilteredExpensesAndValueForUser_NoParameters() throws IOException {
    List<Expense> expectedFilteredExpenses = new ArrayList<>();
    double expectedTotalValue = 0.0;
    when(restapiService.filterExpensesForUser(username, null, null, null))
        .thenReturn(expectedFilteredExpenses);
    when(restapiService.getTotalExpensesValueOfList(expectedFilteredExpenses))
        .thenReturn(expectedTotalValue);

    ResponseEntity<ListAndValueContainer> response =
        restapiController.getFilteredExpensesAndValueForUser(username, null, null, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedFilteredExpenses, response.getBody().getExpenses());
    assertEquals(expectedTotalValue, response.getBody().getTotal(), 0.01);
  }

  /** Test for filtering expenses successfully. */
  @Test
  void testFilterExpensesForUser_Success() throws IOException {
    List<Expense> expectedExpenses = new ArrayList<>();
    when(restapiService.filterExpensesForUser(username, category, start, end))
        .thenReturn(expectedExpenses);
    ResponseEntity<List<Expense>> response =
        restapiController.filterExpensesForUser(username, category, start, end);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedExpenses, response.getBody());
  }

  /** Test for filtering expenses with IllegalArgumentException. */
  @Test
  void testFilterExpensesForUser_IllegalArgumentException() throws IOException {
    when(restapiService.filterExpensesForUser(username, category, start, end))
        .thenThrow(IllegalArgumentException.class);
    ResponseEntity<List<Expense>> response =
        restapiController.filterExpensesForUser(username, category, start, end);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  /** Test for filtering expenses with IOException. */
  @Test
  void testFilterExpensesForUser_IOException() throws IOException {
    when(restapiService.filterExpensesForUser(username, category, start, end))
        .thenThrow(IOException.class);
    ResponseEntity<List<Expense>> response =
        restapiController.filterExpensesForUser(username, category, start, end);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  /** Test for filtering expenses with no parameters. */
  @Test
  void testFilterExpensesForUser_NoParameters() throws IOException {
    List<Expense> expectedExpenses = new ArrayList<>();
    when(restapiService.filterExpensesForUser(username, null, null, null))
        .thenReturn(expectedExpenses);
    ResponseEntity<List<Expense>> response =
        restapiController.filterExpensesForUser(username, null, null, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedExpenses, response.getBody());
  }

  /** Test for deleting an expense successfully. */
  @Test
  void testDeleteExpenseForUser_Success() throws IOException {
    Expense expense = new Expense(LocalDate.now(), "TestCategory", 50.0, "TestDescription");
    User expectedUserAfterDeletion = new User(username, "password");
    when(restapiService.deleteExpenseForUser(username, expense))
        .thenReturn(expectedUserAfterDeletion);
    User response = restapiController.deleteExpenseForUser(username, expense);

    assertNotNull(response);
    assertEquals(expectedUserAfterDeletion, response);
  }

  /** Test for getting categories for a user successfully. */
  @Test
  void testGetCategoriesForUser_Success() throws IOException {
    Set<String> expectedCategories = new HashSet<>();
    expectedCategories.add("Category1");
    expectedCategories.add("Category2");

    when(restapiService.getCategoriesForUser(username)).thenReturn(expectedCategories);
    Set<String> response = restapiController.getCategoriesForUser(username);

    assertNotNull(response);
    assertEquals(expectedCategories, response);
  }
}
