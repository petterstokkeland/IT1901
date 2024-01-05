package ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.Expense;
import core.ExpenseHandlerImpl;
import core.ListAndValueContainer;
import core.User;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import persistence.ExpenseHandlerImplTypeAdapter;

/**
 * The `ApiClient` class provides communication with a remote REST API for user and expense
 * management. It handles various operations like creating users, authenticating users, managing
 * expenses, and retrieving data from the API.
 */
public class ApiClient {

  private static final String BASE_URL = "http://localhost:8080/moneyspender";
  private static final String USER_URL = BASE_URL + "/user";
  private static final String ACCEPT_HEADER = "Accept";
  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String APPLICATION_JSON = "application/json";
  private final HttpClient httpClient;
  private Gson gson;

  /** Creates a new instance of the `ApiClient` class. */
  public ApiClient() {
    this.httpClient = HttpClient.newHttpClient();
    this.gson = createGson();
  }

  /** A static inner class for creating a TypeToken for {@code List<Expense>}. */
  private static class ExpenseListTypeToken extends TypeToken<List<Expense>> {
    // This class intentionally left blank
  }

  /** A static inner class for creating a TypeToken for {@code Set<String>}. */
  private static class SetStringTypeToken extends TypeToken<Set<String>> {
    // This class intentionally left blank
  }

  /**
   * Builds a URI for filtering expenses based on various criteria. This method constructs a URI
   * string with query parameters for filtering expenses by category and date range for a specific
   * user.
   *
   * @param selectedCategory The category of expenses to filter by. Can be null for no category
   *     filter.
   * @param start The start date of the filter range. Can be null for no start date filter.
   * @param end The end date of the filter range. Can be null for no end date filter.
   * @return A string representing the constructed URI with the specified filter parameters.
   */
  private String buildFilterUri(String category, LocalDate start, LocalDate end) {
    return "?category="
        + (category != null ? URLEncoder.encode(category, StandardCharsets.UTF_8) : "")
        + "&start="
        + (start == null ? "" : start)
        + "&end="
        + (end == null ? "" : end);
  }

  /**
   * Creates a Gson instance with a custom type adapter and exclusion policy.
   *
   * @return Configured Gson instance for serialization/deserialization.
   */
  private Gson createGson() {
    return new GsonBuilder()
        .registerTypeAdapter(ExpenseHandlerImpl.class, new ExpenseHandlerImplTypeAdapter())
        .excludeFieldsWithoutExposeAnnotation()
        .setPrettyPrinting()
        .create();
  }

  /**
   * Retrieves the file path of the JSON file from the remote API.
   *
   * @param filePath The file path of the JSON file.
   * @throws IOException if there's an issue with the "user.json" file.
   */
  public void setFilePath(String filePath) throws IOException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(
                URI.create(
                    BASE_URL
                        + "/setFilePath/"
                        + URLEncoder.encode(filePath, StandardCharsets.UTF_8)))
            .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.ofString(filePath))
            .build();

    try {
      HttpResponse<Void> response =
          httpClient.send(request, HttpResponse.BodyHandlers.discarding());

      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IOException("Failed to set file path");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException("Error in sending request: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieves a user by their username from the remote API.
   *
   * @param username The username of the user to retrieve.
   * @return The retrieved user object or null if not found.
   */
  public User getUserByUsername(String username) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(USER_URL + "/" + username))
            .header(ACCEPT_HEADER, APPLICATION_JSON)
            .GET()
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        return gson.fromJson(response.body(), User.class);
      } else {
        return null;
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error in sending request: " + e.getMessage(), e);
    }
  }

  /**
   * Creates a new user with the provided username and password. If the user is successfully
   * created, the method returns the created user object. It throws exceptions for various error
   * scenarios like illegal input or if the username already exists.
   *
   * @param username The username of the new user.
   * @param password The password of the new user.
   * @return User object representing the newly created user.
   * @throws IllegalArgumentException If the input is illegal or the username already exists.
   * @throws RuntimeException If there is a client error, or an I/O or interruption error occurs.
   */
  public User createUser(String username, String password) {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("username", username);
    credentials.put("password", password);
    String requestBodyJson = gson.toJson(credentials);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(USER_URL + "/create"))
            .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return gson.fromJson(response.body(), User.class);
      } else if (response.statusCode() == 400) {
        throw new IllegalArgumentException("Illegal input.");
      } else if (response.statusCode() == 406) {
        throw new IllegalArgumentException("Username already exists.");
      } else {
        throw new RuntimeException(
            "Client error: " + response.statusCode() + " " + response.body());
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Authenticates a user with the remote API using their username and password.
   *
   * @param username The username for authentication.
   * @param password The password for authentication.
   * @return Authenticated User object, or throws an exception if authentication fails.
   * @throws IllegalArgumentException for invalid credentials.
   * @throws RuntimeException for network issues or server errors.
   */
  public User logInUser(String username, String password) {
    Map<String, String> loginCredentials = new HashMap<>();
    loginCredentials.put("username", username);
    loginCredentials.put("password", password);
    String requestBodyJson = gson.toJson(loginCredentials);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(USER_URL + "/authenticate"))
            .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 401) {
        throw new IllegalArgumentException("Invalid username or password.");
      } else if (response.statusCode() >= 400) {
        throw new RuntimeException(
            "Client error: " + response.statusCode() + " " + response.body());
      }

      return gson.fromJson(response.body(), User.class);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds an expense for a user via a POST request to a remote API.
   *
   * @param username The user's identifier.
   * @param expenseCredentials Key-value pairs representing the expense details.
   * @return User object reflecting the added expense.
   * @throws IllegalArgumentException for API bad request errors with illegal input or if the
   *     username already exists.
   * @throws RuntimeException for other client-side errors, price input errors, or failures during
   *     API communication.
   */
  public User addExpenseForUser(String username, HashMap<String, String> expenseCredentials)
      throws IllegalArgumentException, RuntimeException {
    String requestBodyJson = gson.toJson(expenseCredentials);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/expense/add/" + username))
            .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == HttpURLConnection.HTTP_OK) {
        return gson.fromJson(response.body(), User.class);
      } else if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
        throw new IllegalArgumentException("Illegal input.");
      } else if (response.statusCode() == HttpURLConnection.HTTP_NOT_ACCEPTABLE) {
        throw new NumberFormatException("Please provide a valid price.");
      } else {
        throw new RuntimeException(
            "Client error: " + response.statusCode() + " " + response.body());
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error in sending request: " + e.getMessage(), e);
    }
  }

  /**
   * Deletes an expense for a user by sending the expense data to the remote API.
   *
   * @param username The username to the user for whom the expense is deleted.
   * @param expense The expense to be deleted.
   * @return The updated user object after the expense is removed or null if the deletion fails.
   */
  public User deleteExpenseForUser(String username, Expense expense) {
    String requestBodyJson = gson.toJson(expense);
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/expense/delete/" + username))
            .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBodyJson))
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == HttpURLConnection.HTTP_OK) {
        return gson.fromJson(response.body(), User.class);
      } else {
        return null;
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error in sending request: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieves a list of expenses for a user from the remote API based on the user's username.
   *
   * @param username The username to the user for whom expenses are to be retrieved.
   * @return A list of expenses associated with the specified user.
   */
  public List<Expense> getExpensesFromUser(String username) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/expense/" + username))
            .header(ACCEPT_HEADER, APPLICATION_JSON)
            .GET()
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      Type listOfExpensesType = new ExpenseListTypeToken().getType();
      return gson.fromJson(response.body(), listOfExpensesType);

    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error in sending request: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieves a set of categories for a user from the remote API based on the user's username.
   *
   * @param username The username to the user for whom categories are to be retrieved.
   * @return A set of category names associated with the specified user.
   */
  public Set<String> getCategoriesForUser(String username) {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/expense/category/" + username))
            .header(ACCEPT_HEADER, APPLICATION_JSON)
            .GET()
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      Type setType = new SetStringTypeToken().getType();
      return gson.fromJson(response.body(), setType);

    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error in sending request: " + e.getMessage(), e);
    }
  }

  /**
   * Filters expenses for a user based on category and date range.
   *
   * <p>Builds a URI using the user's username, selected category, and date range to make a GET
   * request to the expense filter endpoint. Throws different exceptions based on the received HTTP
   * status code, indicating client or server errors.
   *
   * @param username the username to the user for whom expenses are being filtered
   * @param selectedCategory the category to filter by (can be null or empty for no category filter)
   * @param start the start date of the period to filter (inclusive, can be null for no start date
   *     filter)
   * @param end the end date of the period to filter (inclusive, can be null for no end date filter)
   * @return a list of {@code Expense} objects that match the filter criteria
   * @throws IllegalArgumentException on client error or if the start date is after the end date
   * @throws IOException on communication error
   * @throws RuntimeException on server error or unhandled status codes
   */
  public List<Expense> filterExpensesForUser(
      String username, String selectedCategory, LocalDate start, LocalDate end)
      throws IllegalArgumentException, RuntimeException {

    String uri =
        BASE_URL + "/expense/filter/" + username + buildFilterUri(selectedCategory, start, end);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .header(ACCEPT_HEADER, APPLICATION_JSON)
            .GET()
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
        throw new IllegalArgumentException("Start date must be before end date.");
      } else if (response.statusCode() >= 400) {
        throw new RuntimeException(
            "Client error: " + response.statusCode() + " " + response.body());
      }

      Type listType = new ExpenseListTypeToken().getType();
      return gson.fromJson(response.body(), listType);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Communication error: " + e.getMessage(), e);
    }
  }

  /**
   * Retrieves a container with a filtered list of expenses and their total value for a specified
   * user and category, within a given date range.
   *
   * <p>This method constructs a URI to make a GET request to a REST endpoint that returns the
   * filtered expenses. It passes username, category, and date range as query parameters. If the
   * response is successful, it returns a ListAndValueContainer object containing the list of
   * expenses and total value.
   *
   * @param username The username associated with the expenses to filter.
   * @param selectedCategory The category of expenses to include in the filter. Can be null to
   *     include all categories.
   * @param start The start date of the range for filtering expenses. Can be null to impose no start
   *     date restriction.
   * @param end The end date of the range for filtering expenses. Can be null to impose no end date
   *     restriction.
   * @return A ListAndValueContainer object containing the filtered list of expenses and the total
   *     value.
   * @throws IllegalArgumentException if the start date is after the end date or if other argument
   *     validation fails.
   * @throws RuntimeException if there's a client-side or server-side error during the REST call, or
   *     communication errors occur.
   */
  public ListAndValueContainer getFilteredExpensesAndValue(
      String username, String selectedCategory, LocalDate start, LocalDate end)
      throws IllegalArgumentException, RuntimeException {

    String uri =
        BASE_URL + "/expense/response/" + username + buildFilterUri(selectedCategory, start, end);

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .header(ACCEPT_HEADER, APPLICATION_JSON)
            .GET()
            .build();

    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
        throw new IllegalArgumentException("Start date must be before end date.");
      } else if (response.statusCode() >= 400) {
        throw new RuntimeException(
            "Client error: " + response.statusCode() + " " + response.body());
      }
      return gson.fromJson(response.body(), ListAndValueContainer.class);

    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Communication error: " + e.getMessage(), e);
    }
  }
}
