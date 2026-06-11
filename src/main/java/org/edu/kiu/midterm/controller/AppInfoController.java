package org.edu.kiu.midterm.controller;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.api.AppInfoApi;
import org.edu.kiu.midterm.model.dto.AppInfoDto;
import org.edu.kiu.midterm.service.AppInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppInfoController implements AppInfoApi {

  private final AppInfoService appInfoService;

  @Override
  public ResponseEntity<AppInfoDto> getAppInfo() {
    return ResponseEntity.ok(appInfoService.getAppInfo());
  }

}
