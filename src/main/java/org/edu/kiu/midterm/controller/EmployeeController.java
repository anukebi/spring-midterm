package org.edu.kiu.midterm.controller;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.api.EmployeeApi;
import org.edu.kiu.midterm.model.dto.EmployeeDto;
import org.edu.kiu.midterm.service.EmployeeService;
import org.edu.kiu.midterm.service.PaginationResolver;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

  private final EmployeeService employeeService;
  private final PaginationResolver paginationResolver;

  @Override
  public ResponseEntity<List<EmployeeDto>> getEmployees(Pageable pageable) {
    return ResponseEntity.ok(employeeService.getEmployees(paginationResolver.resolve(pageable)));
  }

  @Override
  public ResponseEntity<EmployeeDto> saveEmployee(EmployeeDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(employeeService.createEmployee(dto));
  }

  @Override
  public ResponseEntity<EmployeeDto> getEmployee(Long id) {
    return ResponseEntity.ok(employeeService.getEmployee(id));
  }

  @Override
  public ResponseEntity<Void> updateEmployee(Long id, EmployeeDto dto) {
    employeeService.updateEmployee(id, dto);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deleteEmployee(Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }

}
