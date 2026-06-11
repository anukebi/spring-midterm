package org.edu.kiu.midterm.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.credentials")
public class AppCredentialsProperties {

  @NotBlank
  private String adminPassword;

  @NotBlank
  private String userPassword;

}
