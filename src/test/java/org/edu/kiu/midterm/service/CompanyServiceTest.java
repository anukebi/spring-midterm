package org.edu.kiu.midterm.service;

import org.edu.kiu.midterm.mapper.CompanyMapper;
import org.edu.kiu.midterm.model.dto.CompanyDto;
import org.edu.kiu.midterm.model.entity.CompanyEntity;
import org.edu.kiu.midterm.model.exception.NotFoundException;
import org.edu.kiu.midterm.monitoring.CompanyMetrics;
import org.edu.kiu.midterm.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

  @Mock
  private CompanyRepository companyRepository;

  @Mock
  private CompanyMapper companyMapper;

  @Mock
  private CompanyMetrics companyMetrics;

  @InjectMocks
  private CompanyService companyService;

  @Test
  void createCompany_persistsAndReturnsDto() {
    var request = new CompanyDto().name("Acme");
    var entity = new CompanyEntity();
    entity.setName("Acme");
    var saved = new CompanyEntity();
    saved.setId(1L);
    saved.setName("Acme");
    var response = new CompanyDto().id(1L).name("Acme");

    when(companyMapper.toEntity(request)).thenReturn(entity);
    when(companyRepository.save(entity)).thenReturn(saved);
    when(companyMapper.toDto(saved)).thenReturn(response);

    var result = companyService.createCompany(request);

    assertEquals(1L, result.getId());
    assertEquals("Acme", result.getName());
    verify(companyMetrics).recordCompanyCreated();
  }

  @Test
  void getCompany_whenNotFound_throwsNotFoundException() {
    when(companyRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> companyService.getCompany(99L));
  }

  @Test
  void deleteCompany_whenNotFound_throwsNotFoundException() {
    when(companyRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> companyService.deleteCompany(99L));
  }

}
