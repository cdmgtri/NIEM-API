package gov.niem.tools.api.db.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY)
public class FieldNotFoundException extends RuntimeException {

  public FieldNotFoundException(String fieldName) {
    super(String.format("Field [%s] is required", fieldName));
  }

}
