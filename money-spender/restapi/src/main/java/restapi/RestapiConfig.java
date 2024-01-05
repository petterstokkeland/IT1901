package restapi;

import core.ExpenseService;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import persistence.JsonController;

/** Configuration class for setting up various beans in the REST API application. */
@Configuration
public class RestapiConfig {

  /**
   * Creates and configures a JsonController bean.
   *
   * @return A JsonController instance.
   * @throws IOException if there's an issue with the "user.json" file.
   */
  @Bean
  public JsonController jsonController() throws IOException {
    return new JsonController("user.json");
  }

  /**
   * Creates an ExpenseService bean.
   *
   * @return An ExpenseService instance.
   */
  @Bean
  public ExpenseService expenseService() {
    return new ExpenseService();
  }
}
