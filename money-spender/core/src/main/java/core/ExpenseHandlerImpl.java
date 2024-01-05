package core;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles operations related to expenses and their categories. Provides methods for adding,
 * updating, and removing expenses, and tracks unique expense categories.
 */
public class ExpenseHandlerImpl implements ExpenseHandler {

  @Expose private List<Expense> expenses;
  private Set<String> categories;

  /** Default constructor initializes an empty list of expenses and categories. */
  public ExpenseHandlerImpl() {
    this(new ArrayList<>(), new HashSet<>());
  }

  /**
   * Initializes the ExpenseHandler with a list of expenses. Extracts and maintains unique
   * categories from the given expenses.
   *
   * @param expenses The list of expenses to initialize with.
   */
  public ExpenseHandlerImpl(List<Expense> expenses) {
    this(expenses, expenses.stream().map(Expense::getCategory).collect(Collectors.toSet()));
  }

  /**
   * Initializes the ExpenseHandler with a list of expenses and a set of categories.
   *
   * @param expenses The list of expenses to initialize with.
   * @param categories The set of unique categories to initialize with.
   */
  public ExpenseHandlerImpl(List<Expense> expenses, Set<String> categories) {
    this.expenses = new ArrayList<>(expenses);
    this.categories = new HashSet<>(categories);
  }

  /**
   * Adds a new expense to the list and its category to the set.
   *
   * @param expense The expense to be added.
   * @throws IllegalArgumentException if the expense is null.
   */
  @Override
  public void addExpense(Expense expense) {
    if (expense == null) {
      throw new IllegalArgumentException("Expense cannot be null.");
    }
    this.expenses.add(expense);
    this.categories.add(expense.getCategory());
  }

  /**
   * Removes an expense from the list. If the category of the expense is not used by any other
   * expense, it's removed from the set.
   *
   * @param expense The expense to be removed.
   * @return true if the expense was successfully removed, false otherwise.
   */
  @Override
  public boolean removeExpense(Expense expense) {
    // If the expense is successfully removed from the list
    if (this.expenses.remove(expense)) {
      // Then check if the category is used by any other expense
      if (!checkIfCategoryUsed(expense)) {
        this.categories.remove(expense.getCategory());
      }
      return true;
    }
    return false;
  }

  /**
   * Updates an existing expense with a new one. Manages categories based on the update.
   *
   * @param oldExpense The expense to be replaced.
   * @param newExpense The new expense to replace with.
   * @throws IllegalArgumentException if the old expense is not found.
   */
  public void updateExpense(Expense oldExpense, Expense newExpense) {
    int index = expenses.indexOf(oldExpense);
    if (index != -1) {
      // If categories are different, manage category sets accordingly
      if (!oldExpense.getCategory().equals(newExpense.getCategory())) {
        // Add new category
        categories.add(newExpense.getCategory());

        // Check for old category usage before updating the expenses list
        if (!checkIfCategoryUsed(oldExpense)) {
          categories.remove(oldExpense.getCategory());
        }
      }

      // Update the expense in the list after handling categories
      expenses.set(index, newExpense);
    } else {
      throw new IllegalArgumentException("Expense not found.");
    }
  }

  /**
   * Fetches and returns all expenses.
   *
   * @return a new list containing all expenses.
   */
  @Override
  public List<Expense> getAllExpenses() {
    return new ArrayList<>(this.expenses);
  }

  /**
   * Returns a set of unique categories.
   *
   * @return Set of categories.
   */
  @Override
  public Set<String> getCategories() {
    return new HashSet<>(this.categories);
  }

  /** Rebuilds the set of unique categories from the list of expenses. */
  public void loadCategories() {
    for (Expense expense : expenses) {
      this.categories.add(expense.getCategory());
    }
  }

  /**
   * Returns the count of expenses in the list.
   *
   * @return Count of expenses.
   */
  public int getExpenseCount() {
    return expenses.size();
  }

  /**
   * Returns the count of unique categories in the set.
   *
   * @return Count of categories.
   */
  public int getCategoryCount() {
    return categories.size();
  }

  /**
   * Checks if the category of a given expense is used by any other expense.
   *
   * @param expenseToCheck Expense whose category is to be checked.
   * @return true if the category is used by any other expense, false otherwise.
   */
  private boolean checkIfCategoryUsed(Expense expenseToCheck) {
    for (Expense e : this.expenses) {
      if (e.getCategory().equals(expenseToCheck.getCategory()) && !e.equals(expenseToCheck)) {
        return true;
      }
    }
    return false;
  }
}
