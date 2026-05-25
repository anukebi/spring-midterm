package org.edu.kiu.midterm.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.mapper.ProfileMapper;
import org.edu.kiu.midterm.model.dto.ProfileDto;
import org.edu.kiu.midterm.repository.ProfileRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository profileRepository;
  private final ProfileMapper profileMapper;

  @PreAuthorize("hasRole('ADMIN')")
  public List<ProfileDto> getAllProfiles() {
    return profileRepository.findAll().stream()
        .map(profileMapper::toDto)
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public ProfileDto getProfile(Long id) {
    return profileRepository.findById(id)
        .map(profileMapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
  }

}
