package org.edu.kiu.midterm.service;

import lombok.RequiredArgsConstructor;
import org.edu.kiu.midterm.util.InternationalizedMessages;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InternationalizedMessageResolver {

  private final MessageSource messageSource;

  public String resolve(InternationalizedMessages message, Object... params) {
    var locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(message.getValue(), params, locale);
  }

  public String resolve(String message, String defaultMessage, Object... params) {
    var locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(message, params, defaultMessage, locale);
  }

}
