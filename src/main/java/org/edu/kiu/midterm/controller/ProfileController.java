package org.edu.kiu.midterm.controller;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.api.ProfileApi;
import org.edu.kiu.midterm.model.dto.ProfileDto;
import org.edu.kiu.midterm.service.PaginationResolver;
import org.edu.kiu.midterm.service.ProfileService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {

  private final ProfileService profileService;
  private final PaginationResolver paginationResolver;

  @Override
  public ResponseEntity<List<ProfileDto>> getProfiles(Pageable pageable) {
    return ResponseEntity.ok(profileService.getProfiles(paginationResolver.resolve(pageable)));
  }

  @Override
  public ResponseEntity<ProfileDto> getProfile(Long id) {
    return ResponseEntity.ok(profileService.getProfile(id));
  }

}
