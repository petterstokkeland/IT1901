package ui;

import java.util.concurrent.TimeUnit;
import javafx.scene.Node;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Provides utility methods to assist with JavaFX operations and interactions during testing using
 * the TestFX framework.
 *
 * <p>This interface defines methods to introduce delays and to wait for JavaFX events to complete.
 * It also provides a method to retrieve JavaFX nodes from the current UI scene based on a query
 * string. Implementing classes can use these utilities to ensure smooth and accurate testing of
 * JavaFX applications.
 */
public interface FxHelper {

  /**
   * Introduces a delay of two seconds.
   *
   * <p>This method can be used to introduce a fixed delay during testing, allowing for certain
   * operations or animations to complete.
   */
  static void waitTwoSeconds() {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Introduces a delay of two deciseconds.
   *
   * <p>This method can be used to introduce a fixed delay during testing, allowing for certain
   * operations or animations to complete.
   */
  static void waitTwoDecisecond() {
    try {
      TimeUnit.MILLISECONDS.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Waits for all JavaFX events to complete.
   *
   * <p>This method ensures that all pending JavaFX events have been processed, making it useful to
   * wait for UI updates or animations to finish during testing.
   */
  static void waitForFX() {
    WaitForAsyncUtils.waitForFxEvents();
  }

  /**
   * Retrieves a JavaFX Node element from the current UI scene based on the provided query string.
   *
   * <p>This method is a utility function to simplify the process of querying UI elements in a
   * JavaFX scene. It uses the lookup-string method to search for the node and then queries the
   * result to retrieve the desired node.
   *
   * @param <T> The type of the JavaFX Node element to be retrieved.
   * @param query The query string used to search for the node in the UI scene.
   * @return The queried JavaFX Node element of type T.
   */
  <T extends Node> T find(final String query);
}
