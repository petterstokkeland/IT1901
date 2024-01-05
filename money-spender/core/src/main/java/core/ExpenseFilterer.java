package core;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/** Provides an interface for filtering and comparing expenses. */
public interface ExpenseFilterer {

  /**
   * Filters a list of expenses based on a specified date range and category.
   *
   * @param expenses The list of expenses to be filtered.
   * @param startDate The start date of the date range for filtering.
   * @param endDate The end date of the date range for filtering.
   * @param category The category by which expenses should be filtered. If null or empty, the filter
   *     will not be applied based on category.
   * @return A list of expenses that match the given date range and category criteria.
   * @throws IllegalArgumentException if the start date is after the end date.
   */
  List<Expense> filterExpenses(
      List<Expense> expenses, LocalDate startDate, LocalDate endDate, String category)
      throws IllegalArgumentException;

  /**
   * Retrieves expenses from a list and sorts them based on a specified comparator.
   *
   * @param expenses The list of expenses to be compared.
   * @param comparator The comparator used to determine the order of the expenses.
   * @return A list of expenses sorted based on the provided comparator.
   */
  List<Expense> getExpensesCompared(List<Expense> expenses, Comparator<Expense> comparator);
}
