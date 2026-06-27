package org.edu.kiu.midterm.controller;

import org.edu.kiu.midterm.model.dto.ProfileDto;
import org.edu.kiu.midterm.model.entity.ProfileEntity;
import org.edu.kiu.midterm.model.exception.NotFoundException;
import org.edu.kiu.midterm.service.InternationalizedMessageResolver;
import org.edu.kiu.midterm.service.PaginationResolver;
import org.edu.kiu.midterm.service.ProfileService;
import org.edu.kiu.midterm.util.InternationalizedMessages;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerWebTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProfileService profileService;

  @MockitoBean
  private PaginationResolver paginationResolver;

  @MockitoBean
  private InternationalizedMessageResolver messageResolver;

  private ProfileDto sampleProfile;

  @BeforeEach
  void setUp() {
    sampleProfile = new ProfileDto().id(1L).bio("Seed bio").phoneNumber("+995500000000");
    when(paginationResolver.resolve(any(Pageable.class))).thenAnswer(invocation -> invocation.getArgument(0));
    lenient().when(messageResolver.resolve(any(InternationalizedMessages.class), any()))
        .thenReturn("Resource not found");
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(profileService, paginationResolver);
  }

  @Test
  void getProfiles_returnsOk() throws Exception {
    when(profileService.getProfiles(any(Pageable.class))).thenReturn(List.of(sampleProfile));

    mockMvc.perform(get("/api/profiles").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].bio").value("Seed bio"));

    verify(profileService).getProfiles(any(Pageable.class));
    verify(paginationResolver).resolve(any(Pageable.class));
  }

  @Test
  void getProfile_returnsOk() throws Exception {
    when(profileService.getProfile(1L)).thenReturn(sampleProfile);

    mockMvc.perform(get("/api/profiles/{id}", 1L).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.phoneNumber").value("+995500000000"));

    verify(profileService).getProfile(1L);
  }

  @Test
  void getProfile_notFound_returns404() throws Exception {
    when(profileService.getProfile(999L)).thenThrow(new NotFoundException(ProfileEntity.class));

    mockMvc.perform(get("/api/profiles/{id}", 999L).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(profileService).getProfile(999L);
  }

}
