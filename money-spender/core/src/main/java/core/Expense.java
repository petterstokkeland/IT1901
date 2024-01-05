package core;

import com.google.gson.annotations.Expose;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * The Expense class represents an expense item with attributes for date, price, category, and
 * description. It is designed to encapsulate expense information and provides methods for
 * validation and manipulation.
 */
public class Expense {
  @Expose private String description;
  @Expose private String category;
  @Expose private String date;
  @Expose private double price;
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  /** Constructs an empty Expense object. */
  public Expense() {}

  /**
   * Constructs an Expense object with specified attributes.
   *
   * @param date The date of the expense.
   * @param category The category of the expense.
   * @param price The price of the expense.
   * @param description The description of the expense.
   */
  public Expense(LocalDate date, String category, double price, String description) {
    setDate(date);
    setCategory(category);
    setPrice(price);
    setDescription(description);
  }

  /**
   * Constructs an Expense object with attributes specified by the provided strings and values. This
   * constructor accepts the date as a String and parses it into a LocalDate object before
   * formatting.
   *
   * @param date A string representing the date of the expense which should be in the format of
   *     "dd.MM.yyyy".
   * @param chosenCategory The category of the expense which helps in categorizing the expense.
   * @param priceValue The monetary cost associated with the expense, represented as a double value.
   * @param description A brief description of the expense providing details about the nature or
   *     purpose of the expense.
   * @throws DateTimeParseException If the date does not comply with the expected date format or is
   *     otherwise invalid.
   * @throws IllegalArgumentException If any provided parameter is null, empty, or in the case of
   *     priceValue, negative.
   */
  public Expense(String date, String chosenCategory, double priceValue, String description) {
    setDateString(date);
    setCategory(chosenCategory);
    setPrice(priceValue);
    setDescription(description);
  }

  /**
   * Retrieves the date of the expense.
   *
   * @return The date as a formatted string (dd.MM.yyyy).
   */
  public String getDate() {
    return date;
  }

  /**
   * Sets the date of the expense. Converts LocalDate to a date string in the desired format.
   *
   * @param date The date to be set.
   * @throws IllegalArgumentException If the provided date is null.
   */
  public void setDate(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null.");
    }
    this.date = date.format(DATE_FORMATTER);
  }

  /**
   * Sets the expense's date from a string. The method parses the string into a LocalDate object
   * assuming a format of "dd.MM.yyyy". The resulting LocalDate is then formatted back into a string
   * and stored as the expense date.
   *
   * @param date A string representation of the date in the format "dd.MM.yyyy".
   * @throws IllegalArgumentException If the date string is null, empty, or cannot be parsed into a
   *     LocalDate.
   */
  public void setDateString(String date) {
    if (date == null || date.trim().isEmpty()) {
      throw new IllegalArgumentException("Please provide a date.");
    }
    try {
      LocalDate dateValue = LocalDate.parse(date, DATE_FORMATTER);
      this.date = dateValue.format(DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("The date format should be 'dd.MM.yyyy'.");
    }
  }

  /**
   * Retrieves the price of the expense.
   *
   * @return The price of the expense.
   */
  public double getPrice() {
    return price;
  }

  /**
   * Sets the price of the expense.
   *
   * @param price The price to be set.
   * @throws IllegalArgumentException If the provided price is negative.
   */
  public void setPrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative.");
    }
    this.price = price;
  }

  /**
   * Retrieves the category of the expense.
   *
   * @return The category of the expense.
   */
  public String getCategory() {
    return category;
  }

  /**
   * Sets the category of the expense.
   *
   * @param category The category to be set.
   * @throws IllegalArgumentException If the provided category is null or empty.
   */
  public void setCategory(String category) {
    if (category == null || category.trim().isEmpty()) {
      throw new IllegalArgumentException("Category cannot be null or empty.");
    }
    if (!category.matches("^[a-zA-Z]+$")) {
      throw new IllegalArgumentException(
          "Category should only contain alphabetic characters and spaces.");
    }

    this.category = category;
  }

  /**
   * Retrieves the description of the expense.
   *
   * @return The description of the expense.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the expense.
   *
   * @param description The description to be set.
   * @throws IllegalArgumentException If the provided description is null or empty.
   */
  public void setDescription(String description) {
    if (description == null || description.trim().isEmpty()) {
      throw new IllegalArgumentException("Description cannot be null or empty.");
    }
    this.description = description;
  }

  @Override
  public String toString() {
    return String.format("| %s | %s | %s | %.2fkr |", date, category, description, price);
  }

  /**
   * Compares this expense to the specified object for equality. The result is true if and only if
   * the argument is not null and is an Expense object that has the same description, category,
   * date, and price as this object.
   *
   * @param obj the object to compare this Expense against
   * @return true if the given object represents an Expense equivalent to this expense, false
   *     otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Expense expense = (Expense) obj;
    if (Double.compare(expense.price, price) != 0) {
      return false;
    }
    if (!description.equals(expense.description)) {
      return false;
    }
    if (!category.equals(expense.category)) {
      return false;
    }
    return date.equals(expense.date);
  }

  /**
   * Returns a hash code for this expense. The hash code is generated by combining the hash codes
   * for the description, category, date, and price of the expense.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(description, category, date, price);
  }
}
