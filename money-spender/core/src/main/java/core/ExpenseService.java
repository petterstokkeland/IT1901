package core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The ExpenseService class provides utility methods for handling expenses of a User. It serves as a
 * layer to interact with a user's ExpenseHandler, ensuring encapsulation and providing additional
 * features for expense management.
 */
public class ExpenseService {

  private ExpenseFilterer expenseFilterer;
  private ExpenseCalculator expenseCalculator;

  /** Default constructor initializes the ExpenseService with default implementations. */
  public ExpenseService() {
    this(new ExpenseFiltererImpl(), new ExpenseCalculatorImpl());
  }

  /**
   * Constructs an ExpenseService object with the provided ExpenseFilterer.
   *
   * @param expenseFilterer The ExpenseFilterer to be used for filtering expenses.
   * @param expenseCalculator The ExpenseCalculator to be used for expenses calculation.
   */
  public ExpenseService(
      final ExpenseFilterer expenseFilterer, final ExpenseCalculator expenseCalculator) {
    this.setExpenseFilterer(expenseFilterer);
    this.setExpenseCalcuator(expenseCalculator);
  }

  /**
   * Sets the {@link ExpenseCalculator} for calculating expenses.
   *
   * @param expenseCalculatorImpl The {@link ExpenseCalculator} instance to be set.
   * @throws IllegalArgumentException if the provided expenseCalculator is null.
   */
  public void setExpenseCalcuator(ExpenseCalculator expenseCalculatorImpl) {
    if (expenseCalculatorImpl == null) {
      throw new IllegalArgumentException("ExpenseCalculator cannot be null.");
    }
    this.expenseCalculator = expenseCalculatorImpl;
  }

  /**
   * Sets the {@link ExpenseFilterer} for filtering expenses.
   *
   * @param expenseFilterer The {@link ExpenseFilterer} instance to be set.
   * @throws IllegalArgumentException if the provided expenseFilterer is null.
   */
  public void setExpenseFilterer(ExpenseFilterer expenseFilterer) {
    if (expenseFilterer == null) {
      throw new IllegalArgumentException("ExpenseFilterer cannot be null.");
    }
    this.expenseFilterer = expenseFilterer;
  }

  /**
   * Adds an expense to the provided user's list of expenses.
   *
   * @param user The user for whom the expense needs to be added.
   * @param expense The expense to be added.
   */
  public void addExpenseForUser(User user, Expense expense) {
    user.getExpenseHandler().addExpense(expense);
  }

  /**
   * Deletes an expense from the provided user's list of expenses.
   *
   * @param user The user for whom the expense needs to be removed.
   * @param selectedItem The Expense to be removed.
   * @return true if the expense was removed successfully, false otherwise.
   */
  public boolean removeExpenseForUser(User user, Expense selectedItem) {
    return user.getExpenseHandler().removeExpense(selectedItem);
  }

  /**
   * Filters and sorts a user's expenses based on the specified category and date range. This method
   * first filters the expenses by the provided category and the date range. After filtering, the
   * expenses are sorted in descending order according to the price.
   *
   * @param user The user whose expenses are to be filtered.
   * @param category The category for which expenses are to be filtered.
   * @param startDate The start date of the date range.
   * @param endDate The end date of the date range.
   * @return A list of filtered and sorted expenses based on the provided parameters. Returns an
   *     empty list if no expenses match the filters. The list is sorted in descending order by the
   *     price of each expense.
   * @throws IllegalArgumentException if the startDate is after the endDate, or if other invalid
   *     arguments are provided.
   */
  public List<Expense> filterExpensesForUser(
      User user, String category, LocalDate startDate, LocalDate endDate)
      throws IllegalArgumentException {
    return expenseFilterer.getExpensesCompared(
        expenseFilterer.filterExpenses(
            user.getExpenseHandler().getAllExpenses(), startDate, endDate, category),
        (e1, e2) -> Double.compare(e2.getPrice(), e1.getPrice()));
  }

  /**
   * Retrieves the unique categories for a user's expenses.
   *
   * @param user The user whose expense categories are to be retrieved.
   * @return A set of unique expense categories.
   */
  public List<Expense> getAllExpensesForUser(User user) {
    return new ArrayList<>(user.getExpenseHandler().getAllExpenses());
  }

  /**
   * Retrieves expenses for a user and sorts them based on the provided comparator.
   *
   * @param user The user whose expenses are to be sorted.
   * @param comparator The comparator used to determine the order of the expenses.
   * @return A list of expenses sorted based on the provided comparator.
   */
  public List<Expense> getExpensesCompared(User user, Comparator<Expense> comparator) {
    return new ArrayList<>(
        this.expenseFilterer.getExpensesCompared(
            user.getExpenseHandler().getAllExpenses(), comparator));
  }

  /**
   * Retrieves all expenses for the provided user.
   *
   * @param user The user whose expenses are to be retrieved.
   * @return A list of all expenses of the user.
   */
  public Set<String> getCategoriesForUser(User user) {
    return new HashSet<>(user.getExpenseHandler().getCategories());
  }

  /**
   * Calculates the total expense value for a given user.
   *
   * @param user The user for whom the total expense value is to be calculated.
   * @return The total expense value associated with the given user.
   */
  public double calculateTotalExpenseValueForUser(User user) {
    return calculateTotalExpenseValueForList(user.getExpenseHandler().getAllExpenses());
  }

  /**
   * Calculates the total expense value for a given list of expenses.
   *
   * @param expenses A list of expenses for which the total value needs to be calculated.
   * @return The total value of all the expenses in the list.
   */
  public double calculateTotalExpenseValueForList(List<Expense> expenses) {
    return expenseCalculator.getTotalExpenseValueOfList(expenses);
  }

  /**
   * Finds and retrieves a specific expense from a user's list of expenses based on the given
   * expense object.
   *
   * @param user The user for whom to search for the expense.
   * @param expense The expense object to find within the user's expenses.
   * @return The expense object if found, or null if the expense is not found.
   */
  public Expense findExpense(User user, Expense expense) {
    for (Expense userExpense : getAllExpensesForUser(user)) {
      if (userExpense.equals(expense)) {
        return userExpense;
      }
    }
    return null;
  }
}
