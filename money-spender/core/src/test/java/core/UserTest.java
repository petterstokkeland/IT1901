package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** JUnit test class for {@link User} class. */
public class UserTest {

  private User user;
  private Expense expense1;
  private Expense expense2;
  private ExpenseService expenseService = new ExpenseService();

  /**
   * Sets up the test environment before each test method. Initializes the user instance and two
   * sample expenses.
   */
  @BeforeEach
  public void setUp() {
    user = new User("OlaNordmann", "password123");
    expense1 = new Expense(LocalDate.now(), "Groceries", 100.0, "Weekly shopping");
    expense2 = new Expense(LocalDate.now(), "Beer", 40.0, "Party");
  }

  /** Tests the User constructor that accepts a username and password. */
  @Test
  public void testEmptyUserConstructor() {
    User emptyUser = new User();
    assertNotNull(emptyUser);
  }

  /*
   * Tests the User constructor that accepts a username and password. Verifies that the username and
   * password are correctly initialized.
   */
  @Test
  public void testCopyConstructor() {
    User user1 = new User(user);
    assertEquals(user1.getUsername(), user.getUsername());
    assertEquals(user1.getPassword(), user.getPassword());
    assertEquals(user1.getExpenseHandler().getAllExpenses(), user.getExpenseHandler().getAllExpenses());
  }

  /**
   * Tests the User constructor that accepts a list of expenses. Verifies that the expenses are
   * correctly initialized.
   */
  @Test
  public void testConstructorWithExpenses() {
    this.expenseService.addExpenseForUser(this.user, expense1);
    this.expenseService.addExpenseForUser(this.user, expense2);
    assertEquals(2, this.expenseService.getAllExpensesForUser(this.user).size());
  }

  /**
   * Tests the User constructor with invalid usernames. Verifies that an exception is thrown for null
   * or empty usernames.
   */
  @Test
  public void testInvalidUsername() {
    assertThrows(IllegalArgumentException.class, () -> new User("", "password123"));
    assertThrows(IllegalArgumentException.class, () -> new User(null, "password123"));
    assertThrows(IllegalArgumentException.class, () -> new User("VeryLongUsername", "password123"));
    assertThrows(IllegalArgumentException.class, () -> new User("Kjell-Arne", "password123"));
    assertThrows(IllegalArgumentException.class, () -> new User("John123", "password123"));
  }

  /**
   * Tests the User constructor with invalid passwords. Verifies that an exception is thrown for null
   * or empty passwords.
   */
  @Test
  public void testInvalidPassword() {
    assertThrows(IllegalArgumentException.class, () -> new User("OlaNordmann", ""));
    assertThrows(IllegalArgumentException.class, () -> new User("OlaNordmann", null));
  }
}
