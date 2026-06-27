package org.edu.kiu.midterm.monitoring;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.config.properties.AppSettingsProperties;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppInfoContributor implements InfoContributor {

  private final AppSettingsProperties appSettings;

  @Override
  public void contribute(Info.Builder builder) {
    builder.withDetail("app", Map.of(
        "title", appSettings.getTitle(),
        "contactEmail", appSettings.getContactEmail(),
        "paginationLimit", appSettings.getPaginationLimit(),
        "externalServiceUrl", appSettings.getExternalServiceUrl())
    );
  }

}
