package org.edu.kiu.midterm.service;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.config.properties.AppSettingsProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaginationResolver {

  private final AppSettingsProperties appSettingsProperties;

  public Pageable resolve(Pageable pageable) {
    int pageSize = Math.min(pageable.getPageSize(), appSettingsProperties.getPaginationLimit());
    return Pageable.ofSize(pageSize).withPage(pageable.getPageNumber());
  }

}
