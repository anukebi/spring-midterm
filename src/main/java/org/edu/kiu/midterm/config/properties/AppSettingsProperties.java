package org.edu.kiu.midterm.config.properties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.settings")
public class AppSettingsProperties {

  @NotBlank
  private String title;

  @Min(1)
  private int paginationLimit;

  @Email
  private String contactEmail;

  @NotBlank
  private String externalServiceUrl;

}
