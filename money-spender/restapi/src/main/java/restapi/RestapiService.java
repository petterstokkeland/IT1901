package restapi;

import core.Expense;
import core.ExpenseService;
import core.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistence.JsonController;

/** Service class for handling user and expense-related operations in the REST API. */
@Service
public class RestapiService {

  @Autowired private JsonController jsonController;
  @Autowired private ExpenseService expenseService;

  /**
   * Saves the user data to a file.
   *
   * @param user The user data to be saved.
   * @throws IOException if there's an issue with saving the file.
   */
  private void saveFile(User user) throws IOException {
    jsonController.saveNewUserToJson(user);
  }

  /**
   * Finds and retrieves a specific expense from a user's list of expenses based on the given
   * expense object.
   *
   * @param user1 The user for whom to search for the expense.
   * @param expense The expense object to find within the user's expenses.
   * @return The expense object if found, or null if the expense is not found.
   */
  private Expense findExpense(User user, Expense expense) {
    return expenseService.findExpense(user, expense);
  }

  /**
   * Validates the category provided by the user. This method checks if the user has provided a
   * category, and if so, whether the category is valid. If the user has not provided a category,
   * the method checks if the user has selected a category from the dropdown menu. If the user has
   * provided a category and selected a category from the dropdown menu, an exception is thrown.
   *
   * @param category The category provided by the user.
   * @param dropDownCategory The category selected from the dropdown menu.
   * @return The validated category.
   * @throws IllegalArgumentException if the user has not provided a correct category or price.
   */
  private String validateCategory(String category, String dropDownCategory) {
    boolean isCategoryEmpty = (category == null || category.isEmpty());
    boolean isDropDownCategoryEmpty = (dropDownCategory == null || dropDownCategory.isEmpty());

    if (isCategoryEmpty && isDropDownCategoryEmpty) {
      throw new IllegalArgumentException("Please provide a category.");
    } else if (isCategoryEmpty && !isDropDownCategoryEmpty) {
      return dropDownCategory;
    } else if (!isCategoryEmpty && isDropDownCategoryEmpty) {
      return category;
    } else {
      throw new IllegalArgumentException(
          "You cannot choose from the dropdown and write a new category at the same time!");
    }
  }

  /**
   * Converts the price provided by the user to a double value.
   *
   * @param price The price provided by the user.
   * @return The price as a double value.
   * @throws IllegalArgumentException if the user has not provided a correct category or price.
   * @throws NumberFormatException if the user has not provided a valid price.
   */
  private double convertPrice(String price) {
    if (price.trim().isEmpty()) {
      throw new IllegalArgumentException("Please provide a price.");
    }
    if (!price.matches("[0-9]+(\\.[0-9]{1,2})?")) {
      throw new NumberFormatException("Please provide a valid price.");
    }
    double priceValue = Double.parseDouble(price);
    return priceValue;
  }

  /**
   * Sets the file path for the user data.
   *
   * @param filePath The file path to set.
   * @throws IOException if there's an issue with setting the file path.
   */
  public void setFilePath(String filePath) throws IOException {
    jsonController.setFilePath(filePath);
  }

  /**
   * Creates a new user if the username does not already exist.
   *
   * @param username The username to the user object to create.
   * @return The created user if successful, or null if the username already exists.
   * @throws IOException if there's an issue with user creation.
   */
  public User createUserIfNotExists(String username, String password) throws IOException {
    if (!jsonController.checkIfUserNameExists(username)) {
      User user = new User(username, password);
      saveFile(user);
      return user;
    }
    return null;
  }

  /**
   * Authenticates a user based on the provided username and password.
   *
   * @param username The username to authenticate.
   * @param password The user's password.
   * @return The authenticated user if successful, or null if authentication fails.
   * @throws IOException if there's an issue with user authentication.
   */
  public User authenticateUser(String username, String password) throws IOException {
    if (jsonController.checkIfUserNameExists(username)
        && jsonController.checkIfPasswordIsCorrect(username, password)) {
      return jsonController.getUser(username);
    }
    return null;
  }

  /**
   * Retrieves a list of all users.
   *
   * @return A list of all user objects.
   * @throws IOException if there's an issue with retrieving user data.
   */
  public List<User> getAllUsers() throws IOException {
    return jsonController.readUsersFromJson();
  }

  /**
   * Retrieves a user by their username.
   *
   * @param username The username to search for.
   * @return The user object corresponding to the provided username.
   * @throws IOException if there's an issue with user retrieval.
   */
  public User getUserByUsername(String username) throws IOException {
    return jsonController.getUser(username);
  }

  /**
   * Creates a new expense for a specific user and records it. This method wraps the process of
   * validating expense data, creating an expense instance, and persisting the expense information
   * for the given username.
   *
   * @param username The username for whom the expense record is to be created.
   * @param credentials A map of the expense data, including date, category, description, and price
   *     details.
   * @throws IllegalArgumentException if the provided category is unrecognized or the price is
   *     invalid (e.g., negative or non-numeric).
   * @throws IOException if there is an issue with writing the expense data to the file system.
   */
  public User createNewExpense(String username, Map<String, String> credentials)
      throws IllegalArgumentException, IOException {
    User user = getUserByUsername(username);
    String date = credentials.get("date");
    String category = credentials.get("newCategory");
    String dropDownCategory = credentials.get("dropDownCategory");
    String description = credentials.get("description");
    String price = credentials.get("price").trim();

    String chosenCategory = validateCategory(category, dropDownCategory);
    double priceValue = convertPrice(price);

    expenseService.addExpenseForUser(
        user, new Expense(date, chosenCategory, priceValue, description));
    saveFile(user);
    return user;
  }

  /**
   * Retrieves all expenses associated with a specific user.
   *
   * @param username The username for which expenses are to be retrieved.
   * @return A list of expenses associated with the specified user.
   * @throws IOException if there's an issue with retrieving expenses.
   */
  public List<Expense> getAllExpensesForUser(String username) throws IOException {
    User user = getUserByUsername(username);
    return expenseService.getAllExpensesForUser(user);
  }

  /**
   * Retrieves expense categories associated with a specific user.
   *
   * @param username The username for which expense categories are requested.
   * @return A set of strings representing expense categories.
   * @throws IOException if there's an issue with retrieving expense categories.
   */
  public Set<String> getCategoriesForUser(String username) throws IOException {
    return expenseService.getCategoriesForUser(getUserByUsername(username));
  }

  /**
   * Deletes an expense for a user and saves the updated data to the file.
   *
   * @param username The username for which the expense is deleted.
   * @param expense The expense to be deleted.
   * @return The updated user object.
   * @throws IOException if there's an issue with deleting the expense or saving the file.
   */
  public User deleteExpenseForUser(String username, Expense expense) throws IOException {
    User user = getUserByUsername(username);
    Expense foundExpense = findExpense(user, expense);
    expenseService.removeExpenseForUser(user, foundExpense);
    saveFile(user);
    return user;
  }

  /**
   * Filters and sorts a user's expenses based on the specified category and date range. This method
   * first filters the expenses by the provided category and the date range. After filtering, the
   * expenses are sorted in descending order according to the price.
   *
   * @param username The user whose expenses are to be filtered.
   * @param selectedCategory The category for which expenses are to be filtered.
   * @param start The start date of the date range.
   * @param end The end date of the date range.
   * @return A list of filtered and sorted expenses based on the provided parameters. Returns an
   *     empty list if no expenses match the filters. The list is sorted in descending order by the
   *     price of each expense.
   * @throws IllegalArgumentException if the startDate is after the endDate, or if other invalid
   *     arguments are provided.
   */
  public List<Expense> filterExpensesForUser(
      String username, String selectedCategory, LocalDate start, LocalDate end)
      throws IllegalArgumentException, IOException {
    return expenseService.filterExpensesForUser(
        getUserByUsername(username), selectedCategory, start, end);
  }

  /**
   * Calculates the total value of all expenses in a provided list. This method sums up the value of
   * each expense in the list to compute the total expense value.
   *
   * @param expenses a list of {@link Expense} objects to calculate the total value of
   * @return the total value of all expenses as a double
   */
  public double getTotalExpensesValueOfList(List<Expense> expenses) {
    return expenseService.calculateTotalExpenseValueForList(expenses);
  }
}
