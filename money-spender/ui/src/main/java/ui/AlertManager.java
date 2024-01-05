package ui;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/** Our own class that manages the display of alerts. */
public class AlertManager {

  private AlertManager() {
    // Static class
  }

  /**
   * Displays an error alert with the specified header and message.
   *
   * @param header The header text for the error alert.
   * @param message The main content message for the error alert.
   */
  public static void showErrorMessage(String header, String message) {
    showAlert(AlertType.ERROR, "Error", header, message);
  }

  /**
   * Displays an information alert with the specified header and message.
   *
   * @param header The header text for the information alert.
   * @param message The main content message for the information alert.
   */
  public static void showInfoMessage(String header, String message) {
    showAlert(AlertType.INFORMATION, "Info", header, message);
  }

  /**
   * Displays a confirmation dialog with the specified title and message.
   *
   * @param title The title of the confirmation dialog.
   * @param message The main content message for the dialog.
   * @return An Optional containing the user's response (e.g., OK, Cancel).
   */
  public static Optional<ButtonType> showConfirmationMessage(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText("Confirmation");
    alert.setContentText(message);
    return alert.showAndWait();
  }

  /**
   * A generic method to display alerts of any type.
   *
   * @param alertType The type of the alert to be displayed.
   * @param title The title of the alert.
   * @param header The header text for the alert.
   * @param message The main content message for the alert.
   */
  private static void showAlert(AlertType alertType, String title, String header, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
