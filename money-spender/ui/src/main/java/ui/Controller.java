package ui;

import javafx.stage.Stage;

/**
 * Represents a UI controller that provides functionality for managing the primary stage of a JavaFX
 * application.
 */
public interface Controller {

  /**
   * Sets the primary stage of the application to the provided stage.
   *
   * @param primaryStage The main stage to be used by this controller.
   */
  public void setPrimaryStage(Stage primaryStage);

  /**
   * Retrieves the primary stage currently associated with this controller.
   *
   * @return The current primary stage of the application.
   */
  public Stage getPrimaryStage();
}
