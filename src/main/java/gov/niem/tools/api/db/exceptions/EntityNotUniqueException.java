package gov.niem.tools.api.db.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY)
public class EntityNotUniqueException extends RuntimeException {

  public EntityNotUniqueException(String entityKind, String label) {
    super(String.format("%s [%s] already exists", entityKind, label));
  }

}
