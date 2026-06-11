package org.edu.kiu.midterm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.edu.kiu.midterm.config.properties.AppSettingsProperties;
import org.edu.kiu.midterm.model.dto.AppInfoDto;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import static org.edu.kiu.midterm.util.InternationalizedMessages.APP_WELCOME;

@RequiredArgsConstructor
@Slf4j
@Service
public class AppInfoService {

  private final AppSettingsProperties appSettings;
  private final InternationalizedMessageResolver messageResolver;

  public AppInfoDto getAppInfo() {
    var locale = LocaleContextHolder.getLocale();
    log.debug("getAppInfo:: Getting app info for locale {}", locale);

    return new AppInfoDto()
        .title(appSettings.getTitle())
        .contactEmail(appSettings.getContactEmail())
        .paginationLimit(appSettings.getPaginationLimit())
        .externalServiceUrl(appSettings.getExternalServiceUrl())
        .welcomeMessage(messageResolver.resolve(APP_WELCOME, appSettings.getTitle()));
  }

}
