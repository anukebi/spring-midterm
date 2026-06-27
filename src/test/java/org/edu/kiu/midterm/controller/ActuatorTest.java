package org.edu.kiu.midterm.controller;

import org.edu.kiu.midterm.support.CoreTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ActuatorTest extends CoreTest {

  @Test
  void health_withoutAuth_isAllowed() throws Exception {
    mockMvc.perform(get("/actuator/health").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  void metrics_withoutAuth_isUnauthorized() throws Exception {
    mockMvc.perform(get("/actuator/metrics").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void info_withoutAuth_isUnauthorized() throws Exception {
    mockMvc.perform(get("/actuator/info").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void metrics_withAdmin_isAllowed() throws Exception {
    mockMvc.perform(get("/actuator/metrics").session(adminSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.names").isArray());
  }

  @Test
  void info_withAdmin_includesAppMetadata() throws Exception {
    mockMvc.perform(get("/actuator/info").session(adminSession()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.app.title").exists())
        .andExpect(jsonPath("$.app.contactEmail").exists());
  }

}
