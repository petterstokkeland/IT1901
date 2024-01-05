package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** JUnit test class for {@link ExpenseFiltererImpl} class. */
public class ExpenseFiltererImplTest {

  private Expense expense1;
  private Expense expense2;
  private LocalDate dateNow = LocalDate.now();
  private LocalDate dateYesterday = LocalDate.now().minusDays(1);
  private ExpenseFiltererImpl filterer;
  private List<Expense> expenses;

  /** Initializes common test data and state before each test run. */
  @BeforeEach
  public void setUp() {
    this.expenses = new ArrayList<>();
    this.expense1 = new Expense(dateNow, "Food", 10.0, "Lunch at cafe");
    this.expense2 = new Expense(dateYesterday, "Entertainment", 20.0, "Movie ticket");
    this.expenses.add(expense1);
    this.expenses.add(expense2);
    this.filterer = new ExpenseFiltererImpl();
  }

  /** Tests the filtering of expenses by date range. */
  @Test
  public void testFilterExpensesByDate() {
    List<Expense> result = filterer.filterExpenses(expenses, dateYesterday, dateNow, null);

    assertEquals(2, result.size());
    assertTrue(result.contains(expense1));
    assertTrue(result.contains(expense2));
  }

  /** Tests the filtering of expenses by date range where no expense falls in the given range. */
  @Test
  public void testFilterExpensesByDateOutsideRange() {
    LocalDate dateTwoDaysAgo = LocalDate.now().minusDays(2);
    LocalDate dateThreeDaysAgo = LocalDate.now().minusDays(3);
    List<Expense> result =
        filterer.filterExpenses(expenses, dateThreeDaysAgo, dateTwoDaysAgo, null);

    assertTrue(result.isEmpty());
  }

  /**
   * Tests filtering of expenses when the start date is null and an end date is provided. Expected:
   * Expenses up to and including the specified end date.
   */
  @Test
  public void testFilterExpensesByDateWithStartDateNull() {
    List<Expense> result = filterer.filterExpenses(expenses, null, dateNow, null);

    assertEquals(2, result.size());
    assertTrue(result.contains(expense1));
    assertTrue(result.contains(expense2));
  }

  /**
   * Tests filtering of expenses when the end date is null and a start date is provided. Expected:
   * Expenses from and including the specified start date onwards.
   */
  @Test
  public void testFilterExpensesByDateWithEndDateNull() {
    List<Expense> result = filterer.filterExpenses(expenses, dateYesterday, null, null);

    assertEquals(2, result.size());
    assertTrue(result.contains(expense1));
    assertTrue(result.contains(expense2));
  }

  /**
   * Tests filtering of expenses when both start and end dates are null. Expected: All expenses are
   * returned.
   */
  @Test
  public void testFilterExpensesByDateWithBothDatesNull() {
    List<Expense> result = filterer.filterExpenses(expenses, null, null, null);

    assertEquals(2, result.size());
    assertTrue(result.contains(expense1));
    assertTrue(result.contains(expense2));
  }

  /**
   * Tests filtering of expenses by valid start and end dates and a specific category. Expected:
   * Expenses between the specified dates and of the specified category.
   */
  @Test
  public void testFilterExpensesWithValidDatesAndCategory() {
    List<Expense> result = filterer.filterExpenses(expenses, dateYesterday, dateNow, "Food");

    assertEquals(1, result.size());
    assertTrue(result.contains(expense1));
  }

  /**
   * Tests filtering of expenses by valid start and end dates with an empty category. Expected:
   * Expenses between and including the specified dates regardless of category.
   */
  @Test
  public void testFilterExpensesWithValidDatesAndEmptyCategory() {
    List<Expense> result = filterer.filterExpenses(expenses, dateYesterday, dateNow, "");

    assertEquals(2, result.size());
    assertTrue(result.contains(expense1));
    assertTrue(result.contains(expense2));
  }

  /**
   * Tests filtering of expenses by valid start and end dates with an invalid category. Expected: An
   * empty list as no expenses match the category.
   */
  @Test
  public void testFilterExpensesWithInvalidCategory() {
    List<Expense> result =
        filterer.filterExpenses(expenses, dateYesterday, dateNow, "InvalidCategory");

    assertTrue(result.isEmpty());
  }

  /**
   * Tests filtering of expenses by valid start and end dates with a category that does not match
   * any expenses.
   */
  @Test
  public void testFilterExpensesByDateWithStartDateAfterEndDateThrowsException() {
    LocalDate futureDate = LocalDate.now().plusDays(1);
    LocalDate pastDate = LocalDate.now().minusDays(1);

    // This should throw an exception since the start date is after the end date.
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              filterer.filterExpenses(expenses, futureDate, pastDate, null);
            });

    // Ensure the exception has the correct message.
    assertEquals("Start date cannot be after end date.", exception.getMessage());
  }

  /** Tests the sorting of expenses in ascending order based on price. */
  @Test
  public void testGetExpensesComparedCheapestFirst() {
    Comparator<Expense> cheapestFirst = (e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice());
    List<Expense> sortedExpenses = filterer.getExpensesCompared(expenses, cheapestFirst);
    assertEquals(Arrays.asList(expense1, expense2), sortedExpenses);
  }

  /** Tests the sorting of expenses in descending order based on price. */
  @Test
  public void testGetExpensesComparedMostExpensiveFirst() {
    Comparator<Expense> expensiveFirst = (e1, e2) -> Double.compare(e2.getPrice(), e1.getPrice());

    List<Expense> sortedExpenses = filterer.getExpensesCompared(expenses, expensiveFirst);
    assertEquals(Arrays.asList(expense2, expense1), sortedExpenses);
  }
}
