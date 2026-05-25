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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class SecurityIntegrationTest extends CoreTest {

  @Test
  void getCompanies_withoutAuth_isAllowed() throws Exception {
    mockMvc.perform(get("/api/companies").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void getEmployees_withoutAuth_isUnauthorized() throws Exception {
    mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void getEmployees_withUser_isAllowed() throws Exception {
    mockMvc.perform(get("/api/employees").session(userSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void createCompany_withoutCsrf_isForbidden() throws Exception {
    var requestJson = loadResource("data/company/create-request.json");

    mockMvc.perform(post("/api/companies")
            .session(adminSession())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isForbidden());
  }

  @Test
  void createCompany_withoutAuth_isUnauthorized() throws Exception {
    var requestJson = loadResource("data/company/create-request.json");

    mockMvc.perform(post("/api/companies")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Sql("classpath:data/company/companies.sql")
  void deleteCompany_withUser_isForbidden() throws Exception {
    mockMvc.perform(delete("/api/companies/{id}", 1L).with(csrf()).session(userSession()))
        .andExpect(status().isForbidden());
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void deleteEmployee_withUser_isForbidden() throws Exception {
    mockMvc.perform(delete("/api/employees/{id}", 1L).with(csrf()).session(userSession()))
        .andExpect(status().isForbidden());
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void getProfiles_withoutAuth_isUnauthorized() throws Exception {
    mockMvc.perform(get("/api/profiles").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void getProfiles_withUser_isForbidden_byPreAuthorize() throws Exception {
    mockMvc.perform(get("/api/profiles").session(userSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @Sql({"classpath:data/company/companies.sql", "classpath:data/employee/employees.sql"})
  void getProfiles_withAdmin_isAllowed_byPreAuthorize() throws Exception {
    mockMvc.perform(get("/api/profiles").session(adminSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}
