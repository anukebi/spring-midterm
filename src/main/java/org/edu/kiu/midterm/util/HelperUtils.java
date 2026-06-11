package org.edu.kiu.midterm.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class HelperUtils {

  private static final String CLASSPATH_PREFIX = "classpath:";

  public static String getResourceContent(String path) {
    try {
      val resource = new PathMatchingResourcePatternResolver().getResource(CLASSPATH_PREFIX + path);
      return new BufferedReader(new InputStreamReader(resource.getInputStream()))
          .lines().collect(Collectors.joining(System.lineSeparator()));
    } catch (IOException e) {
      log.error("getResourceContent:: Failed to read resource content: {}", path, e);
      throw new IllegalArgumentException(path, e);
    }
  }

}
