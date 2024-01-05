package ui;

import core.Expense;
import core.ListAndValueContainer;
import core.User;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * The HomeController class provides the user interface for the home screen. It allows the user to
 * view recent expenses, log out, add a new expense, and see their expense history.
 */
public class HomeController extends AbstractUiController {

  @FXML private TextArea lastExpenses;
  @FXML private Button logOut;
  @FXML private Button newExpense;
  @FXML private Button seeHistory;
  @FXML private TableView<Expense> grdExpenseHistory;
  @FXML private TableColumn<Expense, String> dateColumn;
  @FXML private TableColumn<Expense, String> categoryColumn;
  @FXML private TableColumn<Expense, String> descriptionColumn;
  @FXML private TableColumn<Expense, Double> priceColumn;
  @FXML private Label totalPriceLabel;

  /**
   * Initializes the TableView columns and cell factories for the home screen. Sets up the data
   * binding for the date, category, price, and description columns in the TableView. Additionally,
   * it assigns a custom cell factory to the description column to provide tooltip functionality.
   */
  @FXML
  public void initialize() {
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
   * Loads user data into the controller and displays the top 10 expenses in the TableView. The
   * user's expenses are filtered and limited to 10 for display in the expense history grid. The
   * total price of these expenses is also calculated and displayed.
   *
   * @param userIn The user data to be loaded into the controller.
   */
  public void loadUserAndData(User userIn) {
    ListAndValueContainer listAndValueResponse =
        apiClient.getFilteredExpensesAndValue(userIn.getUsername(), null, null, null);
    this.user = apiClient.getUserByUsername(userIn.getUsername());
    List<Expense> userExpensesCompared = listAndValueResponse.getExpenses();
    double totalValue = listAndValueResponse.getTotal();
    userExpensesCompared = userExpensesCompared.stream().limit(10).collect(Collectors.toList());
    grdExpenseHistory.setItems(FXCollections.observableArrayList(userExpensesCompared));
    totalPriceLabel.setText("Total price: " + String.format(Locale.US, "%.2f", totalValue) + " kr");
  }

  /**
   * Handles the action to add a new expense by redirecting the user to the new expense screen.
   *
   * @param event The action event triggered by the user's interaction.
   * @throws IOException if there's an error loading the new expense screen.
   */
  @FXML
  void handleNewExpense(ActionEvent event) throws IOException {
    FXMLLoader loader =
        new FXMLLoader(NewExpenseController.class.getResource("/ui/NewExpense.fxml"));
    Parent root = loader.load();
    NewExpenseController newExpenseController = loader.getController();
    newExpenseController.loadUserAndData(user);
    Stage window = (Stage) newExpense.getScene().getWindow();
    window.setScene(new Scene(root, 750, 500));
  }

  /**
   * Handles the action to view the expense history by redirecting the user to the expense history
   * screen.
   *
   * @param event The action event triggered by the user's interaction.
   * @throws IOException if there's an error loading the expense history screen.
   */
  @FXML
  void handleSeeHistory(ActionEvent event) throws IOException {
    FXMLLoader loader =
        new FXMLLoader(ExpenseHistoryController.class.getResource("/ui/ExpenseHistory.fxml"));
    Parent root = loader.load();
    ExpenseHistoryController expenseHistoryController = loader.getController();
    expenseHistoryController.loadUserAndData(user);
    Stage window = (Stage) seeHistory.getScene().getWindow();
    window.setScene(new Scene(root, 750, 500));
  }

  /**
   * Handles the log out action by redirecting the user to the login screen.
   *
   * @param event The action event triggered by the user's interaction.
   * @throws IOException if there's an error loading the login screen.
   */
  @FXML
  void handleLogOut(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(LogInController.class.getResource("/ui/LogIn.fxml"));
    Stage window = (Stage) logOut.getScene().getWindow();
    window.setScene(new Scene(root, 750, 500));
    this.user = null;
  }
}
