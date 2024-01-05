package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** JUnit test class for {@link ExpenseService} class. */
public class ExpenseServiceTest {

  private User user;
  private Expense expense1;
  private Expense expense2;
  private ExpenseService expenseService;

  /** Set up the mock user and expense before each test. */
  @BeforeEach
  public void setUp() {
    this.user = new User("test", "test");
    this.expense1 = new Expense(LocalDate.now(), "Food", 100, "Dinner");
    this.expense2 = new Expense(LocalDate.now().minusDays(1), "Food", 200, "Dinner");
    this.expenseService = new ExpenseService();
  }

  /** Test if the ExpenseService is correctly initialized. */
  @Test
  public void constructorTest() {
    assertNotNull(this.expenseService);
  }

  /** Tests setting a null ExpenseFilterer, expecting an IllegalArgumentException. */
  @Test
  public void setExpenseFiltererNullTest() {
    assertThrows(IllegalArgumentException.class, () -> this.expenseService.setExpenseFilterer(null));
  }

  /** Tests setting a null ExpenseCalculator, expecting an IllegalArgumentException. */
  @Test
  public void setExpenseCalculatorNullTest() {
    assertThrows(IllegalArgumentException.class, () -> this.expenseService.setExpenseCalcuator(null));
  }

  /** Test if the expense is successfully added to the user. */
  @Test
  public void testAddExpenseForUser() {
    this.expenseService.addExpenseForUser(user, expense1);
    List<Expense> allExpenses = this.expenseService.getAllExpensesForUser(user);
    assertTrue(allExpenses.contains(expense1));
  }

  /**
   * Tests adding two expenses for a user, removing one, and then verifying the correct expense
   * remains.
   */
  @Test
  public void testRemoveExpenseForUser() {
    this.expenseService.addExpenseForUser(user, expense1);
    this.expenseService.addExpenseForUser(user, expense2);
    this.expenseService.removeExpenseForUser(user, expense1);
    List<Expense> allExpenses = this.expenseService.getAllExpensesForUser(user);
    assertEquals(1, allExpenses.size());
    assertTrue(allExpenses.contains(expense2));
  }

  /** Test if all expenses for a user are correctly retrieved. */
  @Test
  public void testGetAllExpensesForUser() {
    this.expenseService.addExpenseForUser(user, expense1);
    List<Expense> allExpenses = this.expenseService.getAllExpensesForUser(user);
    assertEquals(1, allExpenses.size());
    assertTrue(allExpenses.contains(expense1));
  }

  /** Test if the unique categories for a user's expenses are correctly retrieved. */
  @Test
  public void testGetCategoriesForUser() {
    Expense otherExpense = new Expense(LocalDate.now(), "Entertainment", 50, "Movie");
    this.expenseService.addExpenseForUser(user, expense1);
    this.expenseService.addExpenseForUser(user, otherExpense);

    Set<String> categories = this.expenseService.getCategoriesForUser(user);
    assertTrue(categories.contains("Food"));
    assertTrue(categories.contains("Entertainment"));
    assertEquals(2, categories.size());
  }

  /**
   * Tests if the total expense value for a user is correctly calculated using the ExpenseCalculator.
   */

  @Test
  public void testFilterExpensesForUser() {
    this.expenseService.addExpenseForUser(user, expense1);
    this.expenseService.addExpenseForUser(user, expense2);
    List<Expense> filteredExpenses =
        this.expenseService.filterExpensesForUser(user, "Food", LocalDate.now().minusDays(2), LocalDate.now());
    assertEquals(Arrays.asList(expense2, expense1), filteredExpenses);
    this.expenseService.addExpenseForUser(user, expense1);
    assertTrue(filteredExpenses.contains(expense1));
    assertEquals(2, filteredExpenses.size());
  }

  /** Tests sorting expenses for a user in ascending order of price. */
  @Test
  public void testGetExpensesComparedCheapestFirst() {
    this.expenseService.addExpenseForUser(user, expense1);
    this.expenseService.addExpenseForUser(user, expense2);
    Comparator<Expense> cheapestFirst = (e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice());
    List<Expense> sortedExpenses = expenseService.getExpensesCompared(user, cheapestFirst);
    assertEquals(Arrays.asList(expense1, expense2), sortedExpenses);
  }

  /** Tests sorting expenses for a user in descending order of price. */
  @Test
  public void testGetExpensesComparedMostExpensiveFirst() {
    this.expenseService.addExpenseForUser(user, expense1);
    this.expenseService.addExpenseForUser(user, expense2);
    Comparator<Expense> expensiveFirst = (e1, e2) -> Double.compare(e2.getPrice(), e1.getPrice());
    List<Expense> sortedExpenses = expenseService.getExpensesCompared(user, expensiveFirst);
    assertEquals(Arrays.asList(expense2, expense1), sortedExpenses);
  }

  /** Tests the retrieval of the total expense value for a user. */
  @Test
  public void testCalculateTotalExpenseValueForUser() {
    this.expenseService.addExpenseForUser(user, expense1);
    this.expenseService.addExpenseForUser(user, expense2);
    assertEquals(300, this.expenseService.calculateTotalExpenseValueForUser(user), 0.001);
  }

  /*
   * Tests the retrieval of the total expense value for a user, when the user has no expenses.
   */
  @Test
  public void testFindExpense() {
    this.expenseService.addExpenseForUser(user, expense1);
    this.expenseService.addExpenseForUser(user, expense2);
    assertEquals(expense1, this.expenseService.findExpense(user, expense1));
    assertEquals(expense2, this.expenseService.findExpense(user, expense2));
    Expense expense3 = new Expense(LocalDate.now(), "PC", 10000, "PC");
    assertNull(this.expenseService.findExpense(user, expense3));
  }
}
