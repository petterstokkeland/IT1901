package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** JUnit test class for {@link JsonControllerTest} class. */
public class JsonControllerTest {

  private static final String TEST_JSON_FILE = "test.json";
  private JsonController jsonController;

  /**
   * This method create a new JsonController and sets up the test environment before each test.
   *
   * @throws IOException If an I/O error occurs during file operations while setting up the test.
   */
  @BeforeEach
  public void setUp() throws IOException {
    jsonController = new JsonController(TEST_JSON_FILE);
  }

  /**
   * This method delete the TEST_JSON_FILE after each test.
   *
   * @throws IOException If an I/O error occurs while attempting to delete the test JSON file.
   */
  @AfterEach
  public void tearDownAfterAll() throws IOException {
    Path testFilePath = Paths.get(System.getProperty("user.home"), "money_spender", TEST_JSON_FILE);
    Files.deleteIfExists(testFilePath);
  }

  /**
   * Tests the saving and reading of user data to/from JSON.
   *
   * @throws IOException If an I/O error occurs during file operations.
   */
  @Test
  public void testSaveAndReadUser() throws IOException {
    User user = new User("testuser", "testpassword");
    jsonController.saveNewUserToJson(user);

    // Test overwriting of existing user
    jsonController.saveNewUserToJson(user);

    List<User> users = jsonController.readUsersFromJson();

    assertTrue(jsonController.checkIfUserNameExists("testuser"));
    assertEquals(1, users.size());
    assertEquals(user.getUsername(), users.get(0).getUsername());
    assertEquals(user.getPassword(), users.get(0).getPassword());
  }

  /** Tests the retrieval of the file path used for JSON operations. */
  @Test
  public void testGetFilePath() {
    Path expectedFilePath =
        Paths.get(System.getProperty("user.home"), "money_spender", TEST_JSON_FILE);
    Path actualFilePath = jsonController.getFilePath();

    assertEquals(expectedFilePath, actualFilePath);
  }

  /**
   * Tests reading users from an empty JSON file.
   *
   * @throws IOException If an I/O error occurs during file operations.
   */
  @Test
  public void readUsersFromJson() throws IOException {
    List<User> users = jsonController.readUsersFromJson();
    assertTrue(users.isEmpty());
  }

  /**
   * Tests checking the existence of a user by username in the JSON data.
   *
   * @throws IOException If an I/O error occurs during file operations.
   */
  @Test
  public void checkIfUserNameExists() throws IOException {
    User user = new User("testuser", "testpassword");
    jsonController.saveNewUserToJson(user);

    assertTrue(jsonController.checkIfUserNameExists("testuser"));
    assertFalse(jsonController.checkIfUserNameExists("nonexistentuser"));
  }

  /**
   * Tests checking if a password is correct for a given username.
   *
   * @throws IOException If an I/O error occurs during file operations.
   */
  @Test
  public void checkIfPasswordIsCorrect() throws IOException {
    User user = new User("testuser", "testpassword");
    jsonController.saveNewUserToJson(user);

    assertTrue(jsonController.checkIfPasswordIsCorrect("testuser", "testpassword"));
    assertFalse(jsonController.checkIfPasswordIsCorrect("testuser", "wrongpassword"));
    assertFalse(jsonController.checkIfPasswordIsCorrect("nonexistentuser", "testpassword"));
  }

  /**
   * Tests retrieving a user by username from the JSON data.
   *
   * @throws IOException If an I/O error occurs during file operations.
   */
  @Test
  public void getUser() throws IOException {
    User user = new User("testuser", "testpassword");
    jsonController.saveNewUserToJson(user);

    User retrievedUser = jsonController.getUser("testuser");
    User nullUser = jsonController.getUser("nonexistentuser");
    assertNull(nullUser);
    assertNotNull(retrievedUser);
    assertEquals(user.getUsername(), retrievedUser.getUsername());
    assertEquals(user.getPassword(), retrievedUser.getPassword());
  }
}
