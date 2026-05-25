package org.edu.kiu.midterm.controller;

import org.edu.kiu.midterm.support.CoreTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class EmployeeControllerTest extends CoreTest {

  @Test
  void createEmployee_returnsCreatedEmployee() throws Exception {
    var requestJson = loadResource("data/employee/create-request.json");
    var expectedJson = loadResource("data/employee/create-expected.json");

    mockMvc.perform(post("/api/employees")
            .with(csrf())
            .session(userSession())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().json(expectedJson));
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void getEmployees_returnsList() throws Exception {
    var expectedJson = loadResource("data/employee/seed-employees.json");

    mockMvc.perform(get("/api/employees").session(userSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void getEmployee_byId_returnsEmployee() throws Exception {
    var expectedJson = loadResource("data/employee/seed-employee.json");

    mockMvc.perform(get("/api/employees/{id}", 1L).session(userSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void updateEmployee_noContent_andPersistsChange() throws Exception {
    var updateJson = loadResource("data/employee/update-request.json");

    mockMvc.perform(put("/api/employees/{id}", 1L)
            .with(csrf())
            .session(userSession())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/employees/{id}", 1L).session(userSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(updateJson));
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void deleteEmployee_noContent_thenGetReturns404() throws Exception {
    mockMvc.perform(delete("/api/employees/{id}", 1L).with(csrf()).session(adminSession()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/employees/{id}", 1L).session(userSession()))
        .andExpect(status().isNotFound());
  }

  @Test
  void createEmployee_validationErrors_returns400() throws Exception {
    var invalidJson = loadResource("data/employee/invalid-request.json");

    mockMvc.perform(post("/api/employees")
            .with(csrf())
            .session(userSession())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void updateEmployee_validationErrors_returns400() throws Exception {
    var invalidJson = loadResource("data/employee/invalid-request.json");

    mockMvc.perform(put("/api/employees/{id}", 1L)
            .with(csrf())
            .session(userSession())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest());
  }
}

