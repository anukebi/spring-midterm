package org.edu.kiu.midterm.model.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundException extends EntityNotFoundException {

  public NotFoundException(Class<?> entityClass) {
    super(resolveResourceName(entityClass));
  }

  private static String resolveResourceName(Class<?> entityClass) {
    var name =  entityClass.getSimpleName();
    if (name.endsWith("Entity")) {
      return name.substring(0, name.lastIndexOf("Entity"));
    } else if (name.endsWith("Dto")) {
      return name.substring(0, name.lastIndexOf("Dto"));
    }
    return name;
  }

}
