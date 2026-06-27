package org.edu.kiu.midterm.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.repository.CompanyRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyMetrics {

  private final MeterRegistry meterRegistry;
  private final CompanyRepository companyRepository;

  @PostConstruct
  void registerGauges() {
    meterRegistry.gauge("midterm.companies.total", companyRepository, CompanyRepository::count);
  }

  public void recordCompanyCreated() {
    meterRegistry.counter("midterm.companies.created").increment();
  }

}
