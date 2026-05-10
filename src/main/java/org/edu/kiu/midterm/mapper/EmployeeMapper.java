package org.edu.kiu.midterm.mapper;

import org.edu.kiu.midterm.model.dto.EmployeeDto;
import org.edu.kiu.midterm.model.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  @Mapping(target = "companyId", source = "company.id")
  EmployeeDto toDto(EmployeeEntity employeeEntity);

  @Mapping(target = "company", ignore = true)
  @Mapping(target = "profile.id", ignore = true)
  EmployeeEntity toEntity(EmployeeDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "profile.id", ignore = true)
  void updateEntity(EmployeeDto dto, @MappingTarget EmployeeEntity employeeEntity);

}
