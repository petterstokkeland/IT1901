package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** JUnit test class for {@link Expense} class. */
public class ExpenseTest {

  private Expense expense;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  /** Setup method to initialize common test data. */
  @BeforeEach
  public void setUp() {
    expense = new Expense(LocalDate.now(), "Groceries", 100.0, "Weekly shopping");
  }

  /** Test to verify that the Expense object is correctly initialized. */
  @Test
  public void testExpenseInitialization() {
    assertNotNull(expense);
    assertEquals(LocalDate.now().format(formatter), expense.getDate());
    assertEquals("Groceries", expense.getCategory());
    assertEquals(100.0, expense.getPrice());
    assertEquals("Weekly shopping", expense.getDescription());
  }

  /** Test to verify that the empty constructor creates a valid Expense object. */
  @Test
  public void testEmptyConstructor() {
    Expense expense = new Expense();
    assertNotNull(expense);
  }

  /**
   * Test to verify that the constructor with date, category, price and description parameters
   * creates a valid Expense object.
   */
  @Test
  public void testConstructor() {
    Expense expense1 = new Expense("14.07.1980", "PC", 6000.0, "New PC");
    assertNotNull(expense1);
    assertThrows(
        IllegalArgumentException.class,
        () -> new Expense("01.01.1990", "PC", -6000.0, "New PC"),
        "Negative price should throws illehalArgumentException");
    assertThrows(
        IllegalArgumentException.class,
        () -> new Expense("01.01.1990", "", 6000.0, "New PC"),
        "Empty categorie should throws illehalArgumentException");
    assertThrows(
        IllegalArgumentException.class,
        () -> new Expense("01.01.1990", "", 6000.0, ""),
        "Empty description should throws illehalArgumentException");
  }

  @Test
  public void testSetDateString() {
    assertThrows(IllegalArgumentException.class, () -> expense.setDateString(""));
    assertThrows(
        IllegalArgumentException.class,
        () -> expense.setDateString("12-12-2022"),
        "This should lead to illegalArgumentException, because is wrong format");
  }

  /** Test to verify that setting a null date throws an IllegalArgumentException. */
  @Test
  public void testSetDateNull() {
    assertThrows(IllegalArgumentException.class, () -> expense.setDate(null));
  }

  /** Test to verify that setting a negative price throws an IllegalArgumentException. */
  @Test
  public void testSetNegativePrice() {
    assertThrows(IllegalArgumentException.class, () -> expense.setPrice(-1.0));
  }

  /** Test to verify that setting a null or empty category throws an IllegalArgumentException. */
  @Test
  public void testSetNullOrEmptyCategory() {
    assertThrows(IllegalArgumentException.class, () -> expense.setCategory(null));
    assertThrows(IllegalArgumentException.class, () -> expense.setCategory(""));
  }

  /** Test to verify that setting a null or empty description throws an IllegalArgumentException. */
  @Test
  public void testSetNullOrEmptyDescription() {
    assertThrows(IllegalArgumentException.class, () -> expense.setDescription(null));
    assertThrows(IllegalArgumentException.class, () -> expense.setDescription(""));
  }

  /** Test to verify that the toString method returns the expected string. */
  @Test
  public void testToString() {
    String expected =
        String.format(
            "| %s | %s | %s | %.2fkr |",
            expense.getDate(), expense.getCategory(), expense.getDescription(), expense.getPrice());
    assertEquals(expected, expense.toString());
  }

  /**
   * Test to verify that the equals method returns true when comparing two Expense objects with the
   * same description, category, date and price.
   */
  @Test
  public void testEquals() {
    Expense expense1 = new Expense(LocalDate.now(), "Groceries", 100.0, "Weekly shopping");
    assertEquals(true, expense.equals(expense1));
    assertEquals(false, expense.equals(new Object()));
    assertEquals(
        false,
        expense.equals(new Expense(LocalDate.now(), "Groceries", 100.0, "Weekly shopping1")));
    assertEquals(
        false,
        expense.equals(new Expense(LocalDate.now(), "GroceriesQ", 100.0, "Weekly shopping")));
    assertEquals(
        false, expense.equals(new Expense(LocalDate.now(), "Groceries", 100.1, "Weekly shopping")));
    assertEquals(
        false,
        expense.equals(new Expense(LocalDate.now(), "Groceries", 100.0, "Weekly shopping1")));
  }

  /**
   * Test to verify that the equals method returns true when comparing two Expense objects with the
   * same description, category, date and price.
   */
  @Test
  public void testHashCode() {
    assertEquals(
        Objects.hash(
            expense.getDescription(), expense.getCategory(), expense.getDate(), expense.getPrice()),
        expense.hashCode());
  }
}
