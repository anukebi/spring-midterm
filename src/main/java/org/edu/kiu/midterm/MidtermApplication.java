package org.edu.kiu.midterm;

import org.edu.kiu.midterm.config.properties.AppCredentialsProperties;
import org.edu.kiu.midterm.config.properties.AppSettingsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ AppSettingsProperties.class, AppCredentialsProperties.class })
public class MidtermApplication {

  public static void main(String[] args) {
    SpringApplication.run(MidtermApplication.class, args);
  }

}
