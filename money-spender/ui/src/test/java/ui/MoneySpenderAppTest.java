package ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

/**
 * Test class for the App's user interface.
 *
 * <p>This class extends ApplicationTest and provides unit tests to ensure the proper functioning of
 * the App's UI components, especially the login screen.
 *
 * @see ApplicationTest
 * @see FxHelper
 */
public class MoneySpenderAppTest extends ApplicationTest implements FxHelper {

  /**
   * Initializes the JavaFX application context by starting the App.
   *
   * @param stage the primary stage for this application, onto which the application scene can be
   *     set.
   * @throws Exception if there's an error during the initialization.
   */
  @Start
  public void start(Stage stage) throws Exception {
    new MoneySpenderApp().start(stage);
  }

  /** Tests if the login screen's username field is loaded correctly. */
  @Test
  public void testLoginScreenLoaded() {
    TextField usernameField = find("#username");
    FxHelper.waitForFX();
    assertNotNull(usernameField);
  }

  /** Tests if the login button on the login screen is loaded correctly. */
  @Test
  public void testLogInButtonNotNull() {
    Button logInButton = find("#loginButton");
    FxHelper.waitForFX();
    assertNotNull(logInButton);
  }

  /**
   * Finds and returns a node within the scene graph using the provided query.
   *
   * @param <T> the type of the returned node.
   * @param query the query string used to search for the node.
   * @return the node found using the query.
   */
  @Override
  public <T extends Node> T find(String query) {
    return lookup(query).query();
  }
}
