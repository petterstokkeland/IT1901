package ui;

import core.User;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class responsible for handling the addition of new expenses. This class provides
 * functionalities to add new expenses, select categories, and navigate back to the home screen.
 */
public class NewExpenseController extends AbstractUiController {

  @FXML private TextField notExistingCategory;
  @FXML private Button add;
  @FXML private Button backToHome;
  @FXML private DatePicker datePicker;
  @FXML private TextField description;
  @FXML private ComboBox<String> dragDownCategory;
  @FXML private TextField price;

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  /**
   * Initializes the view by setting up the category dropdown with available categories.
   *
   * @throws IOException if there's an error reading the user data.
   */
  @FXML
  public void initialize() throws IOException {
    this.apiClient = new ApiClient();
  }

  /** Clears all the input fields in the view. */
  private void clearInputFields() {
    this.notExistingCategory.clear();
    this.description.clear();
    this.price.clear();
    this.datePicker.setValue(null);
  }

  /**
   * Loads the user data into the controller. This method is used to pass user data between
   * controllers.
   *
   * @param userIn The user data to be loaded into the controller.
   */
  public void loadUserAndData(User userIn) {
    this.user = apiClient.getUserByUsername(userIn.getUsername());
    Set<String> categories = apiClient.getCategoriesForUser(userIn.getUsername());
    this.dragDownCategory.getItems().addAll(categories);
  }

  /**
   * Handles the addition of a new expense based on the user's input. Validates the input, creates a
   * new expense, and saves it to the user's data.
   *
   * @param event The action event triggered by the user's interaction.
   */
  @FXML
  void handleAdd(ActionEvent event) {
    String newCategoryText = this.notExistingCategory.getText().trim();
    String dateValue =
        this.datePicker.getValue() == null ? "" : formatter.format(this.datePicker.getValue());
    String dropdownCategoryText = this.dragDownCategory.getValue();
    String descriptionText = this.description.getText().trim();
    String inputPrice = this.price.getText().trim();

    HashMap<String, String> expenseCreditentials = new HashMap<>();
    expenseCreditentials.put("date", dateValue);
    expenseCreditentials.put("newCategory", newCategoryText);
    expenseCreditentials.put("dropDownCategory", dropdownCategoryText);
    expenseCreditentials.put("price", inputPrice);
    expenseCreditentials.put("description", descriptionText);
    try {
      user = apiClient.addExpenseForUser(user.getUsername(), expenseCreditentials);

      AlertManager.showInfoMessage(
          "Expense added successfully!", "Expense " + descriptionText + " added successfully!");

      clearInputFields();
      handleBackToHome(null);
    } catch (NumberFormatException e) {
      AlertManager.showErrorMessage("Error", e.getMessage());
    } catch (IllegalArgumentException e) {
      AlertManager.showErrorMessage("Error", e.getMessage());
    } catch (IOException e) {
      AlertManager.showErrorMessage("Error", e.getMessage());
    } catch (RuntimeException e) {
      AlertManager.showErrorMessage("Error", e.getMessage());
    }
  }

  /**
   * Navigates the user back to the home screen.
   *
   * @param event The action event triggered by the user's interaction.
   * @throws IOException if there's an error loading the home screen.
   */
  @FXML
  void handleBackToHome(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/ui/Home.fxml"));
    Parent root = loader.load();
    HomeController homeController = loader.getController();
    homeController.loadUserAndData(user);
    Stage window = (Stage) add.getScene().getWindow();
    window.setScene(new Scene(root, 750, 500));
  }
}
