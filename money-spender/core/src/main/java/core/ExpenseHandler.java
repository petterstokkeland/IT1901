package core;

import java.util.List;
import java.util.Set;

/**
 * Represents a handler for managing expenses. Provides operations to add, remove, update, and
 * retrieve expenses as well as to fetch unique categories of expenses.
 */
public interface ExpenseHandler {

  /**
   * Adds a new expense to the collection.
   *
   * @param expense The expense to be added.
   * @throws IllegalArgumentException if the expense is null.
   */
  void addExpense(Expense expense);

  /**
   * Removes an expense from the collection.
   *
   * @param expense The expense to be removed.
   * @return true if the expense was successfully removed, false otherwise.
   * @throws IllegalArgumentException if the expense is not found in the collection.
   */
  boolean removeExpense(Expense expense);

  /**
   * Updates an existing expense with a new one.
   *
   * @param oldExpense The expense to be replaced.
   * @param newExpense The new expense to replace with.
   * @throws IllegalArgumentException if the old expense is not found.
   */
  void updateExpense(Expense oldExpense, Expense newExpense);

  /**
   * Retrieves all expenses in the collection.
   *
   * @return A list containing all expenses.
   */
  List<Expense> getAllExpenses();

  /**
   * Returns a set of unique expense categories.
   *
   * @return A set containing unique categories of expenses.
   */
  Set<String> getCategories();
}
