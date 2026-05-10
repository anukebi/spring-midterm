package org.edu.kiu.midterm.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class CoreTest {

  private static final String CLASSPATH_PREFIX = "classpath:";

  @Container
  protected static final PostgreSQLContainer POSTGRESQL_CONTAINER = new PostgreSQLContainer("postgres:16-alpine").withReuse(true);

  @Autowired
  protected MockMvc mockMvc;

  @DynamicPropertySource
  static void setDataSourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    registry.add("spring.liquibase.default-schema", () -> "public");
  }

  @SneakyThrows
  public <T> T loadResource(String path, Class<T> cls) {
    var objectMapper = new ObjectMapper();
    var content = loadResource(path);
    return objectMapper.readValue(content, cls);
  }

  @SneakyThrows
  protected String loadResource(String path) {
    var resource = new PathMatchingResourcePatternResolver().getResource(CLASSPATH_PREFIX + path);
    return new BufferedReader(new InputStreamReader(resource.getInputStream()))
        .lines().collect(Collectors.joining(System.lineSeparator()));
  }

}