package org.edu.kiu.midterm.service;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .disabled(!user.isEnabled())
        .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        .build();
  }

}
