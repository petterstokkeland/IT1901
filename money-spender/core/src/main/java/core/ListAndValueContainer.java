package core;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a response object that includes a list of expenses and their total value. This class
 * is designed as a Container class to be able to send a list of expenses and their total value as a
 * http response.
 */
public class ListAndValueContainer {

  @Expose private List<Expense> expenses;
  @Expose private double total;

  /**
   * Constructs an empty ListAndValueContainer object. This constructor is provided to facilitate
   * the JSON serialization/deserialization.
   */
  public ListAndValueContainer() {
    // Empty constructor for JSON deserialization
  }

  /**
   * Constructs a new ListAndValueContainer with a specified list of expenses and total value.
   *
   * @param expenses The list of Expense objects.
   * @param total The total value of all the expenses provided.
   */
  public ListAndValueContainer(List<Expense> expenses, double total) {

    this.expenses = new ArrayList<>(expenses);
    this.total = total;
  }

  /**
   * Sets the list of expenses to the specified list.
   *
   * @param expenses The list of Expense objects to set.
   */
  public void setExpenses(List<Expense> expenses) {
    this.expenses = new ArrayList<>(expenses);
  }

  /**
   * Sets the total value of the expenses.
   *
   * @param total The value to set as the total of expenses.
   */
  public void setTotal(double total) {
    this.total = total;
  }

  /**
   * Returns the list of expenses. Modifications to the returned list will not affect the original
   * list.
   *
   * @return A new ArrayList containing the expenses.
   */
  public List<Expense> getExpenses() {
    return new ArrayList<>(expenses);
  }

  /**
   * Returns the total value of the expenses.
   *
   * @return The total value as a double.
   */
  public double getTotal() {
    return total;
  }
}
