package org.edu.kiu.midterm.service.hook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.edu.kiu.midterm.model.dto.CompanyDto;
import org.edu.kiu.midterm.model.dto.EmployeeDto;
import org.edu.kiu.midterm.service.CompanyService;
import org.edu.kiu.midterm.service.EmployeeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.edu.kiu.midterm.util.HelperUtils.getResourceContent;

@RequiredArgsConstructor
@Slf4j
@Component
@Profile("dev")
public class ReferenceDataInitializerService implements ApplicationRunner {

  private static final String COMPANIES_REFERENCE_DATA_PATH = "ref-data/companies.json";
  private static final String EMPLOYEES_REFERENCE_DATA_PATH = "ref-data/employees.json";

  private final CompanyService companyService;
  private final EmployeeService employeeService;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    log.info("run:: Initializing reference data");

    var companies = objectMapper.readValue(getResourceContent(COMPANIES_REFERENCE_DATA_PATH), CompanyDto[].class);
    for (CompanyDto company : companies) {
      companyService.createCompany(company);
    }

    var employees = objectMapper.readValue(getResourceContent(EMPLOYEES_REFERENCE_DATA_PATH), EmployeeDto[].class);
    for (EmployeeDto employee : employees) {
      employeeService.createEmployee(employee);
    }

    log.info("run:: Completed reference data initialization");
  }

}
