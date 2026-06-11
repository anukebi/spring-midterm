package org.edu.kiu.midterm.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InternationalizedMessages {

  APP_WELCOME("app.welcome"),
  ERROR_NOT_FOUND("error.notFound"),
  ERROR_UNAUTHORIZED("error.unauthorized"),
  ERROR_ACCESS_DENIED("error.accessDenied"),
  ERROR_GENERAL("error.general");

  private final String value;

}
