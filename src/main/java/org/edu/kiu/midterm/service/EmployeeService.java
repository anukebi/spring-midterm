package org.edu.kiu.midterm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.edu.kiu.midterm.mapper.EmployeeMapper;
import org.edu.kiu.midterm.model.dto.EmployeeDto;
import org.edu.kiu.midterm.model.entity.CompanyEntity;
import org.edu.kiu.midterm.model.entity.EmployeeEntity;
import org.edu.kiu.midterm.model.exception.NotFoundException;
import org.edu.kiu.midterm.repository.CompanyRepository;
import org.edu.kiu.midterm.repository.EmployeeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final CompanyRepository companyRepository;
  private final EmployeeMapper employeeMapper;

  @Transactional
  public EmployeeDto createEmployee(EmployeeDto dto) {
    var employeeEntity = employeeMapper.toEntity(dto);
    setEmployeeCompany(employeeEntity, dto.getCompanyId());

    employeeEntity = employeeRepository.save(employeeEntity);
    log.info("createEmployee:: Created employee with id {}", employeeEntity.getId());
    return employeeMapper.toDto(employeeEntity);
  }

  @Transactional(readOnly = true)
  public EmployeeDto getEmployee(Long id) {
    log.debug("getEmployee:: Getting employee with id {}", id);
    return employeeMapper.toDto(getEmployeeOrThrow(id));
  }

  @Transactional(readOnly = true)
  public List<EmployeeDto> getEmployees(Pageable pageable) {
    log.debug("getEmployees:: Getting employees");
    return employeeRepository.findAll(pageable).stream()
        .map(employeeMapper::toDto)
        .toList();
  }

  @Transactional
  public void updateEmployee(Long id, EmployeeDto dto) {
    var employeeEntity = getEmployeeOrThrow(id);
    employeeMapper.updateEntity(dto, employeeEntity);

    setEmployeeCompany(employeeEntity, dto.getCompanyId());
    employeeRepository.save(employeeEntity);
    log.info("updateEmployee:: Updated employee with id {}", id);
  }

  @Transactional
  public void deleteEmployee(Long id) {
    getEmployeeOrThrow(id);
    employeeRepository.deleteById(id);
    log.info("deleteEmployee:: Deleted employee with id {}", id);
  }

  private EmployeeEntity getEmployeeOrThrow(Long id) {
    return employeeRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(EmployeeEntity.class));
  }

  private void setEmployeeCompany(EmployeeEntity employeeEntity, Long companyId) {
    if (companyId != null) {
      employeeEntity.setCompany(companyRepository.findById(companyId)
          .orElseThrow(() -> new NotFoundException(CompanyEntity.class)));
    }
  }

}
