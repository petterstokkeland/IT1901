package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Main class for launching the application that displays the Login page. */
public class MoneySpenderApp extends Application {

  /** Sets up the initial logIn stage. */
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(MoneySpenderApp.class.getResource("LogIn.fxml"));
    Parent parent = fxmlLoader.load();
    LogInController controller = fxmlLoader.getController();
    controller.setFilePath("user.json");
    stage.setScene(new Scene(parent, 750, 500));
    stage.setTitle("Money Spender");
    stage.setResizable(false);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
