package core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link ExpenseCalculatorImpl} class. */
public class ExpenseCalculatorImplTest {

  private ExpenseCalculatorImpl expenseCalculator = new ExpenseCalculatorImpl();

  /**
   * Test case for the scenario when the provided list of expenses is empty.
   *
   * <p>Validates that the total expense value returned for an empty list is 0.
   */
  @Test
  public void testGetTotalExpenseValueOfListWithEmptyList() {
    assertEquals(0, expenseCalculator.getTotalExpenseValueOfList(Collections.emptyList()), 0.001);
  }

  /**
   * Test case for the scenario when the provided list of expenses contains multiple expenses.
   *
   * <p>Validates that the total expense value is correctly calculated for a list with multiple
   * expenses.
   */
  @Test
  public void testGetTotalExpenseValueOfList() {
    Expense expense1 = new Expense(LocalDate.now(), "Food", 75.0, "Breakfast");
    Expense expense2 = new Expense(LocalDate.now(), "Food", 150, "Lunch");
    Expense expense3 = new Expense(LocalDate.now(), "Food", 750, "Expensive dinner");

    double expectedTotal = expense1.getPrice() + expense2.getPrice() + expense3.getPrice();
    assertEquals(
        expectedTotal,
        expenseCalculator.getTotalExpenseValueOfList(Arrays.asList(expense1, expense2, expense3)),
        0.001);
  }
}
