package ui;

import core.Expense;
import core.ListAndValueContainer;
import core.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Represents a controller for the Expense History UI. Allows the user to view their expense history
 * based on various filters.
 */
public class ExpenseHistoryController extends AbstractUiController {

  @FXML private Button back;
  @FXML private ComboBox<String> category;
  @FXML private DatePicker endDate;
  @FXML private Button show;
  @FXML private Button clearFilter;
  @FXML private DatePicker startDate;
  @FXML private TableView<Expense> grdExpenseHistory;
  @FXML private TableColumn<Expense, String> dateColumn;
  @FXML private TableColumn<Expense, String> categoryColumn;
  @FXML private TableColumn<Expense, String> descriptionColumn;
  @FXML private TableColumn<Expense, Double> priceColumn;
  @FXML private TableColumn<Expense, Void> colBtn;
  @FXML private Label totalPriceLabel;

  /**
   * A custom TableCell for displaying expense descriptions. This cell will display the description
   * of an expense and will have a tooltip that displays the same description. If the description is
   * null or the cell is empty, no text will be shown.
   */
  private static class DescriptionCell extends TableCell<Expense, String> {
    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      if (empty || item == null) {
        setText(null);
      } else {

        setText(item);
      }
      Tooltip tooltip = new Tooltip();
      tooltip.textProperty().bind(itemProperty());
      setTooltip(tooltip);
    }
  }

  /**
   * A custom ListCell for displaying categories. This cell will display the category name, or
   * "Category" if the cell is empty or the category is null.
   */
  private static class CategoryListCell extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      if (empty || item == null) {
        setText("Category");
      } else {
        setText(item);
      }
    }
  }

  /**
   * Updates the items in the category ComboBox with the provided set of categories, refreshing the
   * selection options.
   *
   * @param categories The new set of categories to be displayed in the ComboBox.
   */
  private void updateCategoryComboBox(Set<String> categories) {
    category.getItems().clear();
    category.getItems().addAll(categories);
  }

  /**
   * Initialize the JavaFX components and set up cell factories and tooltips for a TableView.
   *
   * @throws IOException if an error occurs during initialization.
   */
  @FXML
  void initialize() throws IOException {
    this.apiClient = new ApiClient();
    dateColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
    categoryColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    descriptionColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

    descriptionColumn.setCellFactory(tc -> new DescriptionCell());
  }

  /**
   * Loads a user's data into the controller for display.
   *
   * @param userIn the user whose data is to be loaded.
   * @throws IOException if an error occurs during loading.
   */
  public void loadUserAndData(User userIn) {
    this.user = apiClient.getUserByUsername(userIn.getUsername());
    Set<String> categories = apiClient.getCategoriesForUser(userIn.getUsername());
    updateCategoryComboBox(categories);
    handleClearFilter(null);
  }

  /**
   * Handles the action when the clear filter button is pressed. Resets all filters and displays the
   * default expense history view. Resets the category ComboBox to its prompt text "Category".
   *
   * @param event the action event.
   */
  @FXML
  void handleClearFilter(ActionEvent event) {
    startDate.setValue(null);
    endDate.setValue(null);
    category.setValue(null);
    category.setPromptText("Category");
    category.setButtonCell(new CategoryListCell());
    handleShow(null);
  }

  /**
   * Handles the action when the back button is pressed. Navigates the user back to the home screen.
   *
   * @param event the action event.
   * @throws IOException if an error occurs during navigation.
   */
  @FXML
  void handleBack(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/ui/Home.fxml"));
    Parent root = loader.load();
    HomeController homeController = loader.getController();
    homeController.loadUserAndData(user);
    Stage window = (Stage) back.getScene().getWindow();
    window.setScene(new Scene(root, 750, 500));
  }

  /**
   * Handles the action when the show button is pressed. Filters and displays the expense history
   * based on the selected criteria.
   *
   * @param event the action event.
   */
  @FXML
  void handleShow(ActionEvent event) {
    String selectedCategory = this.category.getValue();
    LocalDate start = startDate.getValue();
    LocalDate end = endDate.getValue();
    try {
      ListAndValueContainer expenseResponse =
          apiClient.getFilteredExpensesAndValue(user.getUsername(), selectedCategory, start, end);
      List<Expense> filteredExpenses = expenseResponse.getExpenses();
      double totalValue = expenseResponse.getTotal();

      grdExpenseHistory.setItems(FXCollections.observableArrayList(filteredExpenses));
      addButtonToTable();
      totalPriceLabel.setText(
          "Total price: " + String.format(Locale.US, "%.2f", totalValue) + " kr");

    } catch (IllegalArgumentException e) {
      AlertManager.showErrorMessage("Error", e.getMessage());
      handleClearFilter(null);
    } catch (RuntimeException e) {
      AlertManager.showErrorMessage("Error", e.getMessage());
      handleClearFilter(null);
    }
  }

  /**
   * Initializes and adds a button to the `colBtn` TableColumn within the TableView. Each button,
   * when pressed, will remove the corresponding expense from the TableView and update the user's
   * expenses.
   *
   * <p>The delete button is represented by an "X" and has a red background. When a button is
   * pressed, it will: 1. Fetch the associated Expense object. 2. Remove the expense using the
   * expenseService. 3. Save the updated user data using the jsonController. 4. Refresh the
   * TableView by invoking the handleShow method.
   *
   * <p>Note: The TableView is assumed to contain Expense objects.
   */
  @FXML
  private void addButtonToTable() {
    Callback<TableColumn<Expense, Void>, TableCell<Expense, Void>> cellFactory =
        new Callback<TableColumn<Expense, Void>, TableCell<Expense, Void>>() {

          @Override
          public TableCell<Expense, Void> call(final TableColumn<Expense, Void> param) {
            final TableCell<Expense, Void> cell =
                new TableCell<Expense, Void>() {

                  public final Button btn = new Button("X");

                  {
                    btn.setStyle(
                        "-fx-background-color: #FF0000; -fx-text-fill: #FFFFFF; -fx-padding: 2;");
                    btn.getStyleClass().add("expense-delete-button");
                    btn.setMaxWidth(27);

                    btn.setOnAction(
                        (ActionEvent event) -> {
                          Optional<ButtonType> result =
                              AlertManager.showConfirmationMessage(
                                  "Delete Expense",
                                  "Are you sure you want to delete this expense? This action cannot"
                                      + " be undone.");

                          if (result.isPresent() && result.get() == ButtonType.OK) {
                            Expense data = getTableView().getItems().get(getIndex());
                            String username = user.getUsername();
                            user = apiClient.deleteExpenseForUser(username, data);
                            updateCategoryComboBox(apiClient.getCategoriesForUser(username));
                            handleShow(event);
                          } else {
                            // do nothing
                          }
                        });
                  }

                  @Override
                  public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                      setGraphic(null);
                    } else {
                      setGraphic(btn);
                    }
                  }
                };
            return cell;
          }
        };
    colBtn.setCellFactory(cellFactory);
  }
}
