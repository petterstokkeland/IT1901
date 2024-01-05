package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** JUnit test class for {@link ExpenseHandlerImpl} class. */
public class ExpenseHandlerImplTest {

  private ExpenseHandlerImpl handler;
  private Expense expense1;
  private Expense expense2;
  private LocalDate dateNow = LocalDate.now();
  private LocalDate dateYesterday = LocalDate.now().minusDays(1);

  /** Initializes common test data and state before each test run. */
  @BeforeEach
  public void setUp() {
    expense1 = new Expense(dateNow, "Food", 10.0, "Lunch at cafe");
    expense2 = new Expense(dateYesterday, "Entertainment", 20.0, "Movie ticket");
    handler = new ExpenseHandlerImpl(Arrays.asList(expense1, expense2));
  }

  /** Tests the initialization of the ExpenseHandler */
  @Test
  public void testEmptyConstructor() {
    ExpenseHandlerImpl emptyHandler = new ExpenseHandlerImpl();
    assertNotNull(emptyHandler);
    assertEquals(0, emptyHandler.getExpenseCount());
    assertEquals(0, emptyHandler.getCategoryCount());
  }

  /** Tests the addition of a new expense to the handler. */
  @Test
  public void testAddExpense() {
    Expense newExpense = new Expense(LocalDate.now().minusDays(2), "Transport", 30.0, "Bus fare");
    handler.addExpense(newExpense);
    assertTrue(handler.getAllExpenses().contains(newExpense));
    assertTrue(handler.getCategories().contains("Transport"));
  }

  /** Tests the addition of a null expense, expecting an exception. */
  @Test
  public void testAddExpenseNull() {
    assertThrows(IllegalArgumentException.class, () -> handler.addExpense(null));
  }

  /** Tests the removal of an existing expense from the handler. */
  @Test
  public void testRemoveExpense() {
    assertTrue(handler.removeExpense(expense1));
    assertFalse(handler.getAllExpenses().contains(expense1));
    assertFalse(handler.getCategories().contains("Food"));
  }

  /** Tests the removal of a non-existent expense, expecting no changes. */
  @Test
  public void testRemoveExpenseNotPresent() {
    Expense notPresent = new Expense(LocalDate.now().minusDays(3), "Travel", 50.0, "Flight ticket");
    assertFalse(handler.removeExpense(notPresent));
  }

  /** Tests the update of an existing expense with new data. */
  @Test
  public void testUpdateExpense() {
    Expense updatedExpense = new Expense(LocalDate.now(), "Food", 15.0, "Dinner at restaurant");
    handler.updateExpense(expense1, updatedExpense);
    assertTrue(handler.getAllExpenses().contains(updatedExpense));
    assertFalse(handler.getAllExpenses().contains(expense1));
  }

  /** Tests the update of a non-existent expense, expecting an exception. */
  @Test
  public void testUpdateExpenseNotFound() {
    Expense notPresent = new Expense(LocalDate.now().minusDays(4), "Travel", 50.0, "Train ticket");
    Expense newExpense = new Expense(LocalDate.now().minusDays(4), "Travel", 55.0, "Train ticket upgrade");
    assertThrows(IllegalArgumentException.class, () -> handler.updateExpense(notPresent, newExpense));
  }

  /** Tests the update of an expense's category and ensures old category is removed if unused. */
  @Test
  public void testUpdateExpenseCategoryWithRemoval() {
    Expense original = new Expense(LocalDate.now(), "Electronics", 200.0, "New headphones");
    handler.addExpense(original);

    Expense updatedExpense = new Expense(LocalDate.now(), "Gadgets", 210.0, "Upgraded headphones");
    handler.updateExpense(original, updatedExpense);

    assertTrue(handler.getAllExpenses().contains(updatedExpense));
    assertFalse(handler.getCategories().contains("Electronics"));
    assertTrue(handler.getCategories().contains("Gadgets"));
  }

  /** Tests the update of an expense's category and ensures old category stays if still used. */
  @Test
  public void testUpdateExpenseCategoryWithRetention() {
    Expense original = new Expense(LocalDate.now(), "Electronics", 200.0, "New headphones");
    Expense anotherExpense = new Expense(LocalDate.now().minusDays(1), "Electronics", 100.0, "Power bank");
    handler.addExpense(original);
    handler.addExpense(anotherExpense);

    Expense updatedExpense = new Expense(LocalDate.now(), "Gadgets", 210.0, "Upgraded headphones");
    handler.updateExpense(original, updatedExpense);

    assertTrue(handler.getAllExpenses().contains(updatedExpense));
    assertTrue(handler.getCategories().contains("Electronics"));
    assertTrue(handler.getCategories().contains("Gadgets"));
  }

  /** Tests the update of an expense within the same category. */
  @Test
  public void testUpdateExpenseSameCategory() {
    Expense original = new Expense(LocalDate.now(), "Electronics", 200.0, "New headphones");
    handler.addExpense(original);

    Expense updatedExpense = new Expense(LocalDate.now(), "Electronics", 210.0, "Upgraded headphones");
    handler.updateExpense(original, updatedExpense);

    assertTrue(handler.getAllExpenses().contains(updatedExpense));
    assertTrue(handler.getCategories().contains("Electronics"));
    assertEquals(3, handler.getCategories().size());
  }

  /** Tests the retrieval of all expenses from the handler. */
  @Test
  public void testGetAllExpenses() {
    List<Expense> expenses = handler.getAllExpenses();
    assertEquals(2, expenses.size());
    assertTrue(expenses.contains(expense1));
    assertTrue(expenses.contains(expense2));
  }

  /** Tests the retrieval of unique categories from the handler. */
  @Test
  public void testGetCategories() {
    Set<String> categories = handler.getCategories();
    assertEquals(2, categories.size());
    assertTrue(categories.contains("Food"));
    assertTrue(categories.contains("Entertainment"));
  }

  /** Tests the retrieval of the count of expenses. */
  @Test
  public void testGetExpenseCount() {
    assertEquals(2, handler.getExpenseCount());
  }

  /** Tests the retrieval of the count of unique categories. */
  @Test
  public void testGetCategoryCount() {
    assertEquals(2, handler.getCategoryCount());
  }

  /*
   * Tests the retrieval of expenses for a given category.
   */
  @Test
  public void testLoadCategories() {
    Expense expense3 = new Expense(LocalDate.now(), "Food", 100.0, "Lunch at cafe");
    Expense expense4 = new Expense(LocalDate.now(), "Entertainment", 200.0, "Movie ticket");
    ExpenseHandlerImpl handler2 = new ExpenseHandlerImpl(Arrays.asList(expense3, expense4));
    assertEquals(2, handler2.getCategoryCount());
  }
}
