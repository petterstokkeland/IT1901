package core;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user with attributes such as username, password, and a list of
 * expenses. It provides methods for managing user data and expense records.
 */
public class User {

  @Expose private String username;
  @Expose private String password;
  @Expose private ExpenseHandlerImpl expenseHandler;

  /** Constructs an empty User object. */
  public User() {}

  /**
   * Constructs a User object with specified attributes.
   *
   * @param username The username of the user.
   * @param password The password associated with the user.
   * @param expenses The list of expenses associated with the user.
   * @throws IllegalArgumentException If the username is null or empty.
   */
  public User(String username, String password, List<Expense> expenses) {
    this.setUsername(username);
    this.setPassword(password);
    this.expenseHandler = new ExpenseHandlerImpl(expenses);
  }

  /**
   * Constructs a User object based on another user instance. This constructor allows for creating a
   * copy of a user with the same username, password, and a list of expenses derived from the
   * original user's ExpenseHandler. It's useful for situations where you need a duplicate User
   * record with potentially shared or new ExpenseHandler data.
   *
   * @param user The user instance from which to copy properties. The username, password, and
   *     expense data are copied to the new User object.
   */
  public User(User user) {
    this(user.getUsername(), user.getPassword(), user.getExpenseHandler().getAllExpenses());
  }

  /**
   * Constructs a User object with specified username and password.
   *
   * @param username The username of the user.
   * @param password The password associated with the user.
   */
  public User(String username, String password) {
    this(username, password, new ArrayList<>());
  }

  /**
   * Retrieves the username of the user.
   *
   * @return The username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username for the user.
   *
   * @param username The username to be set.
   * @throws IllegalArgumentException If the username is null, empty, exceeds 15 characters, or
   *     contains non-alphabetic characters.
   */
  public void setUsername(final String username) {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty");
    }
    if (username.length() > 15) {
      throw new IllegalArgumentException("Username cannot exceed 15 characters");
    }
    if (!username.matches("^[a-zA-Z]+$")) {
      throw new IllegalArgumentException("Username should only contain alphabetic characters");
    }
    this.username = username;
  }

  /**
   * Retrieves the password of the user.
   *
   * @return The password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password for the user.
   *
   * @param password The password to be set.
   * @throws IllegalArgumentException If the password is null or empty.
   */
  public void setPassword(final String password) {
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }
    this.password = password;
  }

  /**
   * Retrieves the ExpenseHandler associated with the user, which manages the user's expenses and
   * categories.
   *
   * @return The user's ExpenseHandler instance.
   */
  public ExpenseHandler getExpenseHandler() {
    return expenseHandler;
  }
}
