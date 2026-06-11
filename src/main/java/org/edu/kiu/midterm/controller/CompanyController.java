package org.edu.kiu.midterm.controller;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.api.CompanyApi;
import org.edu.kiu.midterm.model.dto.CompanyDto;
import org.edu.kiu.midterm.service.CompanyService;
import org.edu.kiu.midterm.service.PaginationResolver;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompanyApi {

  private final CompanyService companyService;
  private final PaginationResolver paginationResolver;

  @Override
  public ResponseEntity<List<CompanyDto>> getCompanies(Pageable pageable) {
    return ResponseEntity.ok(companyService.getCompanies(paginationResolver.resolve(pageable)));
  }

  @Override
  public ResponseEntity<CompanyDto> saveCompany(CompanyDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(companyService.createCompany(dto));
  }

  @Override
  public ResponseEntity<CompanyDto> getCompany(Long id) {
    return ResponseEntity.ok(companyService.getCompany(id));
  }

  @Override
  public ResponseEntity<Void> updateCompany(Long id, CompanyDto dto) {
    companyService.updateCompany(id, dto);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deleteCompany(Long id) {
    companyService.deleteCompany(id);
    return ResponseEntity.noContent().build();
  }

}
