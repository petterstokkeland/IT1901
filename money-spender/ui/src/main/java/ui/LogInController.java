package ui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controller for the LogIn view. */
public class LogInController extends AbstractUiController {

  @FXML private Button loginButton;
  @FXML private PasswordField password;
  @FXML private Button registerNewUser;
  @FXML private TextField username;

  /** Handles the initialization of the controller. */
  @FXML
  public void initialize() throws IOException {
    this.apiClient = new ApiClient();
  }

  /**
   * Handles the event when the 'register new user' button is pressed. Navigates the user to the
   * CreateNewUser interface.
   *
   * @param event the triggered event.
   * @throws IOException if there's an issue loading the FXML.
   */
  @FXML
  void handleRegisterNewUser(ActionEvent event) throws IOException {
    Parent root =
        FXMLLoader.load(CreateNewUserController.class.getResource("/ui/CreateNewUser.fxml"));
    Stage window = (Stage) registerNewUser.getScene().getWindow();
    window.setScene(new Scene(root, 750, 500));
  }

  /**
   * Handles the login event when the 'log in' button is pressed. Validates the username and
   * password, provides feedback, and navigates to the home interface if successful.
   *
   * @param event the triggered event.
   */
  @FXML
  void handleLogIn(ActionEvent event) {
    String username = this.username.getText().strip();
    String password = this.password.getText().strip();

    try {
      user = apiClient.logInUser(username, password);

      FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/ui/Home.fxml"));
      Parent root = loader.load();
      HomeController homeController = loader.getController();
      homeController.loadUserAndData(user);
      Stage window = (Stage) loginButton.getScene().getWindow();
      window.setScene(new Scene(root, 750, 500));

      AlertManager.showInfoMessage(
          "Logged successfully in!", "User " + user.getUsername() + " logged in.");

    } catch (IllegalArgumentException e) {
      AlertManager.showErrorMessage("Try again", e.getMessage());
    } catch (IOException e) {
      AlertManager.showErrorMessage("Try again", "Something went wrong with reading from the file");
    } catch (Exception e) {
      AlertManager.showErrorMessage("Try again", "Something went wrong");
    }
  }
}
