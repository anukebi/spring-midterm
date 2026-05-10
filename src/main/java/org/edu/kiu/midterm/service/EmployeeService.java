package org.edu.kiu.midterm.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.mapper.EmployeeMapper;
import org.edu.kiu.midterm.model.dto.EmployeeDto;
import org.edu.kiu.midterm.model.entity.EmployeeEntity;
import org.edu.kiu.midterm.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;
  private final CompanyService companyService;

  public EmployeeDto createEmployee(EmployeeDto dto) {
    var employeeEntity = employeeMapper.toEntity(dto);
    setEmployeeCompany(employeeEntity, dto.getCompanyId());

    employeeEntity = employeeRepository.save(employeeEntity);
    return employeeMapper.toDto(employeeEntity);
  }

  public EmployeeDto getEmployee(Long id) {
    var employeeEntity = getEmployeeOrThrow(id);
    return employeeMapper.toDto(employeeEntity);
  }

  public List<EmployeeDto> getAllEmployees() {
    return employeeRepository.findAll().stream()
        .map(employeeMapper::toDto)
        .toList();
  }

  public void updateEmployee(Long id, EmployeeDto dto) {
    var employeeEntity = getEmployeeOrThrow(id);
    employeeMapper.updateEntity(dto, employeeEntity);

    setEmployeeCompany(employeeEntity, dto.getCompanyId());
    employeeRepository.save(employeeEntity);
  }

  public void deleteEmployee(Long id) {
    getEmployeeOrThrow(id);
    employeeRepository.deleteById(id);
  }

  EmployeeEntity getEmployeeOrThrow(Long id) {
    return employeeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
  }

  private void setEmployeeCompany(EmployeeEntity employeeEntity, Long companyId) {
    if (companyId != null) {
      employeeEntity.setCompany(companyService.getCompanyOrThrow(companyId));
    }
  }

}
