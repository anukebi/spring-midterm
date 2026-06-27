package org.edu.kiu.midterm.service;

import org.edu.kiu.midterm.config.properties.AppSettingsProperties;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaginationResolverTest {

  @Mock
  private AppSettingsProperties appSettingsProperties;

  @InjectMocks
  private PaginationResolver paginationResolver;

  @ParameterizedTest
  @CsvSource({
      "10, 50, 10",
      "100, 50, 50",
      "50, 50, 50"
  })
  void resolve_capsPageSizeToConfiguredLimit(int requestedSize, int limit, int expectedSize) {
    when(appSettingsProperties.getPaginationLimit()).thenReturn(limit);
    var pageable = PageRequest.of(0, requestedSize);

    var resolved = paginationResolver.resolve(pageable);

    assertEquals(expectedSize, resolved.getPageSize());
    assertEquals(0, resolved.getPageNumber());
  }

}
