package gov.niem.tools.api.db.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
public class DatabaseKeyViolationException extends RuntimeException{
  public DatabaseKeyViolationException(String model, String value){
    super(String.format("Your request to add a new '%s' failed. There is already one named '%s'", model, value));
  }
  public DatabaseKeyViolationException(String model, String value, String additionalErrors){
    super(String.format("Your request to add a new %s failed. There is already one named %s. Additional Info: %s", model, value, additionalErrors));
  }
}
