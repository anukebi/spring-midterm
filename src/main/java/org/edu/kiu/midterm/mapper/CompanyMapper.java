package org.edu.kiu.midterm.mapper;

import org.edu.kiu.midterm.model.dto.CompanyDto;
import org.edu.kiu.midterm.model.entity.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

  CompanyDto toDto(CompanyEntity companyEntity);

  @Mapping(target = "employees", ignore = true)
  CompanyEntity toEntity(CompanyDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "employees", ignore = true)
  void updateEntity(CompanyDto dto, @MappingTarget CompanyEntity companyEntity);

}
