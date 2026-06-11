package org.edu.kiu.midterm.controller;

import org.edu.kiu.midterm.support.CoreTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;

import java.util.Locale;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AppInfoControllerTest extends CoreTest {

  @ParameterizedTest
  @CsvSource(value = {
      "null,data/appinfo/default.json",
      "en,data/appinfo/en.json",
      "ka,data/appinfo/ka.json"
  }, nullValues = "null")
  void getAppInfo_withLocale(String localeCode, String path) throws Exception {
    var expectedJson = loadResource(path);
    var locale = Optional.ofNullable(localeCode)
        .map(Locale::forLanguageTag)
        .orElse(null);

    mockMvc.perform(get("/api/app-info")
            .accept(MediaType.APPLICATION_JSON)
            .locale(locale))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }

}

