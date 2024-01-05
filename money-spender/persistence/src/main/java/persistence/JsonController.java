package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.ExpenseHandlerImpl;
import core.User;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The JsonController class provides methods for handling JSON data related to User objects. It
 * allows saving, reading, and manipulating user data in a JSON format.
 */
public class JsonController {

  private Path filePath;
  private final Gson gson =
      new GsonBuilder()
          .registerTypeAdapter(ExpenseHandlerImpl.class, new ExpenseHandlerImplTypeAdapter())
          .excludeFieldsWithoutExposeAnnotation()
          .setPrettyPrinting()
          .create();

  /**
   * Constructs a `JsonController` with the provided file name and sets the file path for working
   * with JSON data.
   *
   * @param file The name of the JSON file to work with.
   * @throws IOException If an I/O error occurs while setting the file path or creating necessary
   *     directories or files.
   */
  public JsonController(String file) throws IOException {
    setFilePath(file);
  }

  /**
   * Sets the file path for working with JSON data. The file path is created if it does not exist.
   *
   * @param file The name of the JSON file to work with.
   * @throws IOException If an I/O error occurs while setting the file path or creating necessary
   *     directories or files.
   */
  public void setFilePath(String file) throws IOException {
    Path userHomePath = Paths.get(System.getProperty("user.home"));
    Path userFolderPath = userHomePath.resolve("money_spender");
    Path userJsonFilePath = userFolderPath.resolve(file);

    if (!Files.exists(userFolderPath)) {
      Files.createDirectory(userFolderPath);
    }

    if (!Files.exists(userJsonFilePath)) {
      Files.createFile(userJsonFilePath);
    }

    this.filePath = userJsonFilePath;
  }

  /**
   * Retrieves the current file path for the JSON data storage.
   *
   * @return The file path as a Path object.
   */
  public Path getFilePath() {
    return this.filePath;
  }

  /**
   * Saves a new User object to the JSON data file.
   *
   * @param user The User object to be saved.
   * @throws IOException If an error occurs while writing to the file.
   */
  public void saveNewUserToJson(User user) throws IOException {
    List<User> users = readUsersFromJson();
    if (checkIfUserNameExists(user.getUsername())) {
      User oldUser = getUserFromList(user.getUsername(), users);
      users.remove(oldUser);
    }
    users.add(user);

    try (Writer writer = new FileWriter(this.filePath.toFile(), StandardCharsets.UTF_8)) {
      gson.toJson(users, writer);
    }
  }

  /**
   * Reads User objects from the JSON data file.
   *
   * @return A list of User objects read from the JSON file.
   * @throws IOException If an error occurs while reading from the file.
   */
  public List<User> readUsersFromJson() throws IOException {
    List<User> users = new ArrayList<>();
    if (Files.exists(filePath)) {
      try (Reader reader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8)) {
        User[] userArray = gson.fromJson(reader, User[].class);
        if (userArray != null) {
          for (User user : userArray) {
            users.add(user);
          }
        }
      }
    }
    return users;
  }

  /**
   * Checks if a username already exists in the JSON data file.
   *
   * @param username The username to check for existence.
   * @return True if the username exists; otherwise, false.
   * @throws IOException If an error occurs while reading from the file.
   */
  public boolean checkIfUserNameExists(String username) throws IOException {
    List<User> users;
    users = readUsersFromJson();
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Validates if the provided username and password combination exists in the JSON data.
   *
   * @param username The username to validate.
   * @param password The password to validate.
   * @return True if the combination is correct; otherwise, false.
   * @throws IOException If an error occurs while reading from the file.
   */
  public boolean checkIfPasswordIsCorrect(String username, String password) throws IOException {
    List<User> users;
    users = readUsersFromJson();
    for (User user : users) {
      if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves a User object by its username.
   *
   * @param username The username of the user to retrieve.
   * @return The User object if found; otherwise, null.
   * @throws IOException If an error occurs while reading from the file.
   */
  public User getUser(String username) throws IOException {
    List<User> users = readUsersFromJson();
    return getUserFromList(username, users);
  }

  /**
   * Helper method to retrieve a User object from a list of users by its username.
   *
   * @param username The username of the user to retrieve.
   * @param users The list of users to search within.
   * @return The User object if found; otherwise, null.
   */
  private User getUserFromList(String username, List<User> users) {
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        return user;
      }
    }
    return null;
  }
}
