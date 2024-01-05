package core;

import java.util.List;

/**
 * An implementation of the {@link ExpenseCalculator} interface.
 *
 * <p>This class provides a concrete implementation of the ExpenseCalculator using Java streams to
 * calculate the total value of a list of expenses.
 */
public class ExpenseCalculatorImpl implements ExpenseCalculator {

  /**
   * Calculates the total expense value of the given list of expenses.
   *
   * @param expenses A list of expenses to calculate the total value for.
   */
  @Override
  public double getTotalExpenseValueOfList(List<Expense> expenses) {
    return expenses.stream().mapToDouble(Expense::getPrice).sum();
  }
}
