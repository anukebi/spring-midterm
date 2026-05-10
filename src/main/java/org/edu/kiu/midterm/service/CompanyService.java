package org.edu.kiu.midterm.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.mapper.CompanyMapper;
import org.edu.kiu.midterm.model.dto.CompanyDto;
import org.edu.kiu.midterm.model.entity.CompanyEntity;
import org.edu.kiu.midterm.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyMapper companyMapper;

  public CompanyDto createCompany(CompanyDto dto) {
    var companyEntity = companyMapper.toEntity(dto);
    companyEntity = companyRepository.save(companyEntity);
    return companyMapper.toDto(companyEntity);
  }

  public CompanyDto getCompany(Long id) {
    var companyEntity = getCompanyOrThrow(id);
    return companyMapper.toDto(companyEntity);
  }

  public List<CompanyDto> getAllCompanies() {
    return companyRepository.findAll().stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public void updateCompany(Long id, CompanyDto dto) {
    var companyEntity = getCompanyOrThrow(id);
    companyMapper.updateEntity(dto, companyEntity);
    companyRepository.save(companyEntity);
  }

  public void deleteCompany(Long id) {
    getCompanyOrThrow(id);
    companyRepository.deleteById(id);
  }

  CompanyEntity getCompanyOrThrow(Long id) {
    return companyRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Company not found"));
  }


}
