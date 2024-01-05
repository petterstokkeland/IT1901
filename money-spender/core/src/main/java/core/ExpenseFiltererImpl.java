package core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ExpenseFilterer that provides functionality to filter and sort a list of
 * expenses.
 */
public class ExpenseFiltererImpl implements ExpenseFilterer {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  /**
   * Validates that the start date is not after the end date.
   *
   * @param startDate the start date to validate
   * @param endDate the end date to validate
   * @throws IllegalArgumentException if the start date is after the end date
   */
  private void validateDates(LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }
  }

  /**
   * Filters a list of expenses by the specified start and end dates.
   *
   * @param expenses the list of expenses to filter
   * @param startDate the start date (inclusive) for the filter; can be null
   * @param endDate the end date (inclusive) for the filter; can be null
   * @return a list of expenses that fall between the specified start and end dates
   */
  private List<Expense> filterExpensesByDate(
      List<Expense> expenses, LocalDate startDate, LocalDate endDate) {
    validateDates(startDate, endDate);

    return expenses.stream()
        .filter(
            e -> {
              LocalDate expenseDate = LocalDate.parse(e.getDate(), DATE_FORMATTER);
              return (startDate == null || !expenseDate.isBefore(startDate))
                  && (endDate == null || !expenseDate.isAfter(endDate));
            })
        .collect(Collectors.toList());
  }

  /**
   * Filters expenses based on provided start and end dates and category.
   *
   * @param expenses the list of expenses to filter
   * @param startDate the start date (inclusive) for the filter; can be null
   * @param endDate the end date (inclusive) for the filter; can be null
   * @param category the category to filter by; can be null or empty
   * @return a list of expenses that meet the filter criteria
   * @throws IllegalArgumentException if the start date is after the end date
   */
  @Override
  public List<Expense> filterExpenses(
      List<Expense> expenses, LocalDate startDate, LocalDate endDate, String category) {
    List<Expense> filteredExpenses = filterExpensesByDate(expenses, startDate, endDate);

    if (category != null && !category.trim().isEmpty()) {
      filteredExpenses =
          filteredExpenses.stream()
              .filter(e -> category.equals(e.getCategory()))
              .collect(Collectors.toList());
    }
    return filteredExpenses;
  }

  /**
   * Sorts a list of expenses based on the provided comparator.
   *
   * @param expenses the list of expenses to sort
   * @param comparator the comparator to determine the order of the list
   * @return a sorted list of expenses according to the specified comparator
   */
  @Override
  public List<Expense> getExpensesCompared(List<Expense> expenses, Comparator<Expense> comparator) {
    return new ArrayList<>(expenses.stream().sorted(comparator).collect(Collectors.toList()));
  }
}
