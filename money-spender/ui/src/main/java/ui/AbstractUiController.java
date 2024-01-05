package ui;

import core.User;
import java.io.IOException;
import javafx.stage.Stage;

/**
 * Represents an abstract controller for JavaFX user interfaces. This class provides foundational
 * functionality related to JavaFX stages that specific UI controllers may utilize.
 *
 * <p>This controller acts as an intermediary between the JavaFX views (typically FXML) and the
 * underlying application logic. It provides basic methods for setting and getting the primary
 * stage.
 *
 * <p>Concrete subclasses must implement the {@link Controller} interface.
 */
public abstract class AbstractUiController implements Controller {

  private Stage primaryStage;
  protected ApiClient apiClient;
  protected User user;

  /**
   * Sets the primary stage of the JavaFX application.
   *
   * @param primaryStage The main JavaFX stage.
   */
  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * Sets the file path for a web controller, allowing it to work with the specified file.
   *
   * @param filePath The file path to be set.
   * @throws IOException If an I/O error occurs while setting the file path.
   */
  public void setFilePath(String filePath) throws IOException {
    this.apiClient.setFilePath(filePath);
  }

  /**
   * Retrieves the primary stage of the JavaFX application.
   *
   * @return The primary stage.
   */
  public Stage getPrimaryStage() {
    return primaryStage;
  }
}
