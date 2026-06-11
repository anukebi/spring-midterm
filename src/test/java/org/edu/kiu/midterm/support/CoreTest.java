package org.edu.kiu.midterm.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.edu.kiu.midterm.util.HelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("prod")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class CoreTest {

  private static final String CLASSPATH_PREFIX = "classpath:";

  protected static final String ADMIN_USERNAME = "admin";
  protected static final String ADMIN_PASSWORD = "admin123";
  protected static final String USER_USERNAME = "user";
  protected static final String USER_PASSWORD = "user123";

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

  protected MockHttpSession adminSession() throws Exception {
    return loginSession(ADMIN_USERNAME, ADMIN_PASSWORD);
  }

  protected MockHttpSession userSession() throws Exception {
    return loginSession(USER_USERNAME, USER_PASSWORD);
  }

  protected MockHttpSession loginSession(String username, String password) throws Exception {
    var result = mockMvc.perform(post("/login")
            .with(csrf())
            .param("username", username)
            .param("password", password))
        .andExpect(status().is3xxRedirection())
        .andReturn();
    return (MockHttpSession) result.getRequest().getSession();
  }

  @SneakyThrows
  protected String loadResource(String path) {
    return HelperUtils.getResourceContent(path);
  }

}
