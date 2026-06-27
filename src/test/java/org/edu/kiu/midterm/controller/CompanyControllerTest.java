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
public class CompanyControllerTest extends CoreTest {

  @Test
  void createCompany_returnsCreatedCompany() throws Exception {
    var requestJson = loadResource("data/company/create-request.json");
    var expectedJson = loadResource("data/company/create-expected.json");

    mockMvc.perform(post("/api/companies")
            .with(csrf())
            .session(adminSession())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().json(expectedJson));
  }

  @Test
  @Sql("classpath:data/company/companies.sql")
  void getCompanies_returnsList() throws Exception {
    var expectedJson = loadResource("data/company/seed-companies.json");

    mockMvc.perform(get("/api/companies").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }

  @Test
  @Sql("classpath:data/company/companies.sql")
  void getCompany_byId_returnsCompany() throws Exception {
    var expectedJson = loadResource("data/company/seed-company.json");

    mockMvc.perform(get("/api/companies/{id}", 1L).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }

  @Test
  @Sql("classpath:data/company/companies.sql")
  void updateCompany_noContent_andPersistsChange() throws Exception {
    var updateJson = loadResource("data/company/update-request.json");

    mockMvc.perform(put("/api/companies/{id}", 1L)
            .with(csrf())
            .session(adminSession())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/companies/{id}", 1L).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(updateJson));
  }

  @Test
  @Sql("classpath:data/company/companies.sql")
  void deleteCompany_noContent_thenGetReturns404() throws Exception {
    mockMvc.perform(delete("/api/companies/{id}", 1L).with(csrf()).session(adminSession()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/companies/{id}", 1L))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql("classpath:data/company/companies.sql")
  void getCompany_notFound_returns404() throws Exception {
    mockMvc.perform(get("/api/companies/{id}", 999L).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void createCompany_validationError_returns400() throws Exception {
    var invalidJson = loadResource("data/company/invalid-name-too-short.json");

    mockMvc.perform(post("/api/companies")
            .with(csrf())
            .session(adminSession())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Sql("classpath:data/company/companies.sql")
  void updateCompany_validationError_returns400() throws Exception {
    var invalidJson = loadResource("data/company/invalid-name-too-short.json");

    mockMvc.perform(put("/api/companies/{id}", 1L)
            .with(csrf())
            .session(adminSession())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest());
  }
}

