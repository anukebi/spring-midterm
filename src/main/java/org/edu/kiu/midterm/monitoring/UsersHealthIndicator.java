package org.edu.kiu.midterm.monitoring;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.repository.UserRepository;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersHealthIndicator implements HealthIndicator {

  private static final long MINIMUM_USERS = 2;

  private final UserRepository userRepository;

  @Override
  public Health health() {
    var userCount = userRepository.count();
    if (userCount >= MINIMUM_USERS) {
      return Health.up()
          .withDetail("users", userCount)
          .build();
    }
    return Health.down()
        .withDetail("users", userCount)
        .withDetail("reason", "Expected at least %d seeded users".formatted(MINIMUM_USERS))
        .build();
  }

}
