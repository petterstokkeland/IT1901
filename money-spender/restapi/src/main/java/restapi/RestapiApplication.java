package restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

/**
 * The main class for the REST API application. This class configures and runs the Spring Boot
 * application.
 *
 * <p>It excludes the JacksonAutoConfiguration to customize Jackson configuration if needed.
 */
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class RestapiApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestapiApplication.class, args);
  }
}
