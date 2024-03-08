package gov.niem.tools.api.db.steward;

import gov.niem.tools.api.db.ServiceHub;
import gov.niem.tools.api.db.exceptions.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@Tag(name = "Data-1: Stewards",
  description = "A group or entity responsible for managing NIEM content.")
public class StewardController {

  @Autowired
  ServiceHub hub;

  @GetMapping("/stewards/{stewardKey}")
  @Operation(summary = "Get a steward")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Steward getSteward(@PathVariable String stewardKey) throws EntityNotFoundException {
    return hub.stewards.findOne(stewardKey);
  }

  @GetMapping("/stewards")
  @Operation(summary="Get all stewards")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Steward> getStewards() throws Exception {
    return hub.stewards.repository().findAll();
  }

  // @PostMapping(path="/stewards")
  // @Operation(summary="Create a steward")
  // public ResponseEntity<String> postSteward(@RequestBody Steward steward) throws Exception {
  //   String message = hub.stewards.add(steward);
  //   return AppUtils.getResponseOkString(message);
  // }

  // @PutMapping(path="/stewards/{stewardKey}")
  // @Operation(summary="Updated a steward")
  // public ResponseEntity<String> putSteward(@PathVariable String stewardKey, @RequestBody Steward updatedSteward, BindingResult bindingResult) throws Exception {
  //   String message = hub.stewards.edit(stewardKey, updatedSteward);
  //   return AppUtils.getResponseOkString(message);
  // }

  // @PatchMapping(path="/stewards/{stewardKey}")
  // @Operation(summary="Updated specific fields on a steward")
  // public ResponseEntity<String> patchSteward(@PathVariable String stewardKey, Steward updatedSteward) throws Exception {
  //   String message = hub.stewards.edit(stewardKey, updatedSteward);
  //   return AppUtils.getResponseOkString(message);
  // }

  // @DeleteMapping(path="/stewards/{stewardKey}")
  // @Operation(summary="Delete a steward")
  // public ResponseEntity<String> deleteSteward(@PathVariable String stewardKey) throws Exception {
  //   String message = hub.stewards.delete(stewardKey);
  //   return AppUtils.getResponseOkString(message);
  // }

}
