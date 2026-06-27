package org.edu.kiu.midterm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    var csrfHandler = new CsrfTokenRequestAttributeHandler();
    csrfHandler.setCsrfRequestAttributeName("_csrf");

    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(csrfHandler))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/error")
            .permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
            .permitAll()
            .requestMatchers("/actuator/health")
            .permitAll()
            .requestMatchers("/actuator/**")
            .hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/app-info")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/api/companies", "/api/companies/**")
            .permitAll()
            // URL-level ADMIN rules (no @PreAuthorize on these operations)
            .requestMatchers(HttpMethod.POST, "/api/companies", "/api/companies/**")
            .hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/companies/**")
            .hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/companies/**")
            .hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
            .hasRole("ADMIN")
            .requestMatchers("/api/employees/**")
            .authenticated()
            // Profiles: login required at URL level; ADMIN role checked in ProfileService via @PreAuthorize
            .requestMatchers("/api/profiles/**")
            .authenticated()
            .anyRequest().authenticated())
        .formLogin(form -> form
            .defaultSuccessUrl("/swagger-ui/index.html", true)
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll())
        .exceptionHandling(ex -> ex
            .defaultAuthenticationEntryPointFor(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                request -> request.getRequestURI().startsWith("/api/")
                    || request.getRequestURI().startsWith("/actuator/"))
            .defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                request -> !request.getRequestURI().startsWith("/api/")
                    && !request.getRequestURI().startsWith("/actuator/")))
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

    return http.build();
  }

}
