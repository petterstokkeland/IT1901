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

/**
 * The CreateNewUserController class manages the user creation functionality in the UI. This
 * controller facilitates user registration and validation while communicating with the back-end
 * through a JSON controller. The controller also provides feedback to the user in the form of
 * dialogs.
 */
public class CreateNewUserController extends AbstractUiController {

  @FXML private Button backToLogIn;
  @FXML private PasswordField password;
  @FXML private Button registerNewUser;
  @FXML private TextField username;

  /**
   * Initializes the controller and sets up necessary resources, in this case, a ApiClient
   * instance.
   *
   * @throws IOException if there are issues reading the user JSON file.
   */
  public void initialize() throws IOException {
    apiClient = new ApiClient();
  }

  /**
   * Handles the action of the backToLogIn button. Navigates the user back to the login screen.
   *
   * @param event The ActionEvent triggered by the button.
   * @throws IOException if there are issues loading the Login.fxml file.
   */
  @FXML
  void handleBackToLogIn(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(LogInController.class.getResource("/ui/LogIn.fxml"));
    Stage window = (Stage) backToLogIn.getScene().getWindow();
    window.setScene(new Scene(root, 750, 500));
  }

  /**
   * Handles the action of the registerNewUser button. Registers a new user if the provided username
   * does not already exist.
   *
   * @param event The ActionEvent triggered by the button.
   */
  @FXML
  void handleRegisterNewUser(ActionEvent event) {
    String username = this.username.getText().strip();
    String password = this.password.getText().strip();

    try {
      user = apiClient.createUser(username, password);

      FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/ui/Home.fxml"));
      Parent root = loader.load();
      HomeController homeController = loader.getController();
      homeController.loadUserAndData(user);
      Stage window = (Stage) registerNewUser.getScene().getWindow();
      window.setScene(new Scene(root, 750, 500));

      AlertManager.showInfoMessage(
          "New user created!", "User " + user.getUsername() + " logged in.");
    } catch (IllegalArgumentException | IOException e) {
      AlertManager.showErrorMessage("Try again", e.getMessage());
    } catch (Exception e) {
      AlertManager.showErrorMessage("Try again", e.getMessage());
    }
  }
}
