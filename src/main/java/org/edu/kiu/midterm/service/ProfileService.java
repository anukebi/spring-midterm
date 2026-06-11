package org.edu.kiu.midterm.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.edu.kiu.midterm.mapper.ProfileMapper;
import org.edu.kiu.midterm.model.dto.ProfileDto;
import org.edu.kiu.midterm.model.entity.CompanyEntity;
import org.edu.kiu.midterm.model.entity.ProfileEntity;
import org.edu.kiu.midterm.model.exception.NotFoundException;
import org.edu.kiu.midterm.repository.ProfileRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository profileRepository;
  private final ProfileMapper profileMapper;

  @PreAuthorize("hasRole('ADMIN')")
  public List<ProfileDto> getProfiles(Pageable pageable) {
    log.debug("getProfiles:: Getting profiles");
    return profileRepository.findAll(pageable).stream()
        .map(profileMapper::toDto)
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public ProfileDto getProfile(Long id) {
    log.debug("getProfiles:: Getting profile with id {}", id);
    return profileRepository.findById(id)
        .map(profileMapper::toDto)
        .orElseThrow(() -> new NotFoundException(ProfileEntity.class));
  }

}
