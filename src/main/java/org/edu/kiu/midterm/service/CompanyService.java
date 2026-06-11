package org.edu.kiu.midterm.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.edu.kiu.midterm.config.properties.AppSettingsProperties;
import org.edu.kiu.midterm.mapper.CompanyMapper;
import org.edu.kiu.midterm.model.dto.CompanyDto;
import org.edu.kiu.midterm.model.entity.CompanyEntity;
import org.edu.kiu.midterm.model.exception.NotFoundException;
import org.edu.kiu.midterm.repository.CompanyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyMapper companyMapper;

  public CompanyDto createCompany(CompanyDto dto) {
    var companyEntity = companyMapper.toEntity(dto);
    companyEntity = companyRepository.save(companyEntity);
    log.info("createCompany:: Created company with id {}", companyEntity.getId());
    return companyMapper.toDto(companyEntity);
  }

  public CompanyDto getCompany(Long id) {
    log.debug("getCompany:: Getting company with id {}", id);
    var companyEntity = getCompanyOrThrow(id);
    return companyMapper.toDto(companyEntity);
  }

  public List<CompanyDto> getCompanies(Pageable pageable) {
    log.debug("getCompanies:: Getting companies");
    return companyRepository.findAll(pageable).stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public void updateCompany(Long id, CompanyDto dto) {
    var companyEntity = getCompanyOrThrow(id);
    companyMapper.updateEntity(dto, companyEntity);
    companyRepository.save(companyEntity);
    log.info("updateCompany:: Updated company with id {}", id);
  }

  public void deleteCompany(Long id) {
    getCompanyOrThrow(id);
    companyRepository.deleteById(id);
    log.info("deleteCompany:: Deleted company with id {}", id);
  }

  CompanyEntity getCompanyOrThrow(Long id) {
    return companyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(CompanyEntity.class));
  }

}
