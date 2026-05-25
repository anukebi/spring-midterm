package org.edu.kiu.midterm.mapper;

import org.edu.kiu.midterm.model.dto.ProfileDto;
import org.edu.kiu.midterm.model.entity.ProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

  ProfileDto toDto(ProfileEntity profileEntity);

}
