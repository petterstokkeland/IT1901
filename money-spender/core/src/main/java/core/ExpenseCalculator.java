package core;

import java.util.List;

/**
 * Represents a calculator for expenses.
 *
 * <p>This interface provides a way to calculate the total value of a list of expenses.
 */
public interface ExpenseCalculator {

  /**
   * Calculates the total expense value of the given list of expenses.
   *
   * @param expenses A list of expenses to calculate the total value for.
   * @return The total value of all the expenses in the list.
   */
  public double getTotalExpenseValueOfList(List<Expense> expenses);
}
