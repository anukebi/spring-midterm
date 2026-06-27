package org.edu.kiu.midterm.repository;

import org.edu.kiu.midterm.model.entity.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompanyRepositoryTest {

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  void saveAndFindById_persistsCompany() {
    var company = new CompanyEntity();
    company.setName("Repository Test Co");

    var saved = companyRepository.save(company);

    assertThat(saved.getId()).isNotNull();
    assertThat(companyRepository.findById(saved.getId()))
        .isPresent()
        .get()
        .extracting(CompanyEntity::getName)
        .isEqualTo("Repository Test Co");
  }

  @Test
  void findById_whenMissing_returnsEmpty() {
    assertThat(companyRepository.findById(9999L)).isEmpty();
  }

}
