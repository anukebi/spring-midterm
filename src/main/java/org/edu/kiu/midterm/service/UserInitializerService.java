package org.edu.kiu.midterm.service;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.model.entity.UserEntity;
import org.edu.kiu.midterm.repository.UserRepository;
import org.edu.kiu.midterm.model.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * A simple initializer hook service to create default users if the database is empty. This is useful for testing and development purposes.
 * Only created in scope the assignment to satisfy the project requirements, as actual implementation would require more robust handling
 * and functionality to support real-world user management scenarios (e.g., registration, password reset, etc.).
 */
@Component
@RequiredArgsConstructor
public class UserInitializerService implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) {
    if (userRepository.count() > 0) {
      return;
    }

    userRepository.save(new UserEntity(null, "admin", passwordEncoder.encode("admin123"), Role.ADMIN, true));
    userRepository.save(new UserEntity(null, "user", passwordEncoder.encode("user123"), Role.USER, true));
  }

}
