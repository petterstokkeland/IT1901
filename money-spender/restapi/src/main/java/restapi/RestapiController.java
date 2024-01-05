package restapi;

import core.Expense;
import core.ListAndValueContainer;
import core.User;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller class for the REST API handling user and expense-related operations. */
@RestController
@RequestMapping("/moneyspender")
public class RestapiController {

  @Autowired private RestapiService restapiService;

  /**
   * Sets the file path for the user data.
   *
   * @param filePath The file path to set.
   * @throws IOException if there's an issue with setting the file path.
   */
  @PostMapping("/setFilePath/{filePath}")
  public void setFilePath(@PathVariable String filePath) throws IOException {
    restapiService.setFilePath(filePath);
  }

  /**
   * Endpoint for creating a new user with the provided credentials.
   *
   * @param credentials A map containing "username" and "password" as keys and their corresponding
   *     values.
   * @return A ResponseEntity containing the created user or an error status.
   */
  @PostMapping("/user/create")
  public ResponseEntity<User> createUser(@RequestBody Map<String, String> credentials)
      throws IOException {
    String username = credentials.get("username");
    String password = credentials.get("password");
    try {
      User createdUser = restapiService.createUserIfNotExists(username, password);
      if (createdUser == null) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
      }
      return ResponseEntity.ok(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Endpoint for user authentication. This method checks the provided credentials and responds with
   * the corresponding user details or an unauthorized status as needed.
   *
   * @param credentials A map containing "username" and "password" as keys to authenticate the user.
   * @return A ResponseEntity containing the authenticated user or an unauthorized status.
   */
  @PostMapping("/user/authenticate")
  public ResponseEntity<User> authenticateUser(@RequestBody Map<String, String> credentials)
      throws IOException {
    String username = credentials.get("username");
    String password = credentials.get("password");

    User authenticatedUser = restapiService.authenticateUser(username, password);
    return authenticatedUser != null
        ? ResponseEntity.ok(authenticatedUser)
        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  /**
   * Endpoint for retrieving all expenses for a specific user.
   *
   * @param username The user for which expenses are to be retrieved.
   * @return A list of expenses associated with the specified user.
   */
  @GetMapping("/user/{username}")
  public User getUserByUsername(@PathVariable String username) throws IOException {
    return restapiService.getUserByUsername(username);
  }

  /**
   * Endpoint for retrieving all expenses for a specific user.
   *
   * @param username The user for which expenses are to be retrieved.
   * @return A list of expenses associated with the specified user.
   */
  @GetMapping("/expense/{username}")
  public List<Expense> getAllExpensesForUsername(@PathVariable String username) throws IOException {
    return restapiService.getAllExpensesForUser(username);
  }

  /**
   * Endpoint for creating a new expense for a user. Handles the HTTP POST request to add an expense
   * associated with a given username.
   *
   * @param username The username for which the expense is being added.
   * @param credentials The map containing expense details.
   * @return A ResponseEntity with the updated user data or an appropriate error status.
   */
  @PostMapping("/expense/add/{username}")
  public ResponseEntity<User> addExpenseForUser(
      @PathVariable String username, @RequestBody Map<String, String> credentials)
      throws IOException {
    try {
      User user = restapiService.createNewExpense(username, credentials);
      return ResponseEntity.ok(user);

    } catch (NumberFormatException e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Endpoint for deleting an expense for a user.
   *
   * @param username The username for which the expense is deleted.
   * @param expense The expense to be deleted.
   * @return The updated user object.
   * @throws IOException if there's an issue with deleting the expense.
   */
  @DeleteMapping("/expense/delete/{username}")
  public User deleteExpenseForUser(@PathVariable String username, @RequestBody Expense expense)
      throws IOException {
    return restapiService.deleteExpenseForUser(username, expense);
  }

  /**
   * Endpoint for retrieving a set of expense categories associated with a specific user.
   *
   * @param username The username of the user for whom expense categories are requested.
   * @return A set of strings representing expense categories.
   * @throws IOException if there's an issue with retrieving the expense categories.
   */
  @GetMapping("/expense/category/{username}")
  public Set<String> getCategoriesForUser(@PathVariable String username) throws IOException {
    return restapiService.getCategoriesForUser(username);
  }

  /**
   * Endpoint for retrieving a list of expenses for a specific user, filtered by category and/or
   * date.
   *
   * @param username The username of the user for whom expenses are requested.
   * @param category The category by which to filter the expenses.
   * @param start The start date by which to filter the expenses.
   * @param end The end date by which to filter the expenses.
   * @return A list of expenses for the specified user, filtered by category and/or date.
   * @throws IllegalArgumentException if the provided start date is after the provided end date.
   * @throws IOException if there's an issue with retrieving the expenses.
   */
  @GetMapping("/expense/filter/{username}")
  public ResponseEntity<List<Expense>> filterExpensesForUser(
      @PathVariable String username,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate start,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate end) {

    try {
      if (category != null) {
        category = URLDecoder.decode(category, StandardCharsets.UTF_8.toString());
      }
      List<Expense> expenses = restapiService.filterExpensesForUser(username, category, start, end);
      return ResponseEntity.ok(expenses);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Handles HTTP GET requests to retrieve a list of filtered expenses and their total value for a
   * specified user. This endpoint allows for optional filtering based on expense category and a
   * date range.
   *
   * @param username The username for which to retrieve and calculate expenses, extracted from the
   *     URL path.
   * @param category The category of expenses to filter by; if not provided, all categories are
   *     included.
   * @param start The beginning of the date range for which to filter expenses; if not provided,
   *     filtering starts from the earliest record.
   * @param end The end of the date range for which to filter expenses; if not provided, filtering
   *     goes up to the latest record.
   * @return A ResponseEntity containing a ListAndValueContainer with the list of filtered expenses
   *     and the total value, or an error status.
   */
  @GetMapping("/expense/response/{username}")
  public ResponseEntity<ListAndValueContainer> getFilteredExpensesAndValueForUser(
      @PathVariable String username,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate start,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate end) {
    try {
      if (category != null) {
        category = URLDecoder.decode(category, StandardCharsets.UTF_8.toString());
      }
      List<Expense> expenses = restapiService.filterExpensesForUser(username, category, start, end);
      double totalValue = restapiService.getTotalExpensesValueOfList(expenses);
      return ResponseEntity.ok(new ListAndValueContainer(expenses, totalValue));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
