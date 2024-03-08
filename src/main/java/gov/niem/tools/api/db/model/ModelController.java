package gov.niem.tools.api.db.model;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import gov.niem.tools.api.db.ServiceHub;
import gov.niem.tools.api.db.steward.Steward;

@Validated
@RestController
@Tag(name = "Data-2: Models", description = "A reference or message NIEM data model.")
public class ModelController {

  @Autowired
  ModelRepository modelRepo;

  @Autowired
  ServiceHub hub;

  @GetMapping("/stewards/{stewardKey}/models/{modelKey}")
  @Operation(summary = "Get a model")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public gov.niem.tools.api.db.model.Model getModel(@PathVariable String stewardKey, @PathVariable String modelKey) throws Exception {
    return hub.models.findOne(stewardKey, modelKey);
  }

  @GetMapping("/stewards/{stewardKey}/models")
  @Operation(
    summary="Get all models from a steward",
    description = "Use wildcard '*' for the steward key to return all models")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Model> getStewardModels(@PathVariable String stewardKey) throws Exception {
    if (stewardKey.equals("*")) {
      // Wildcard: Return all models
      return modelRepo.findAll();
    }
    Steward steward = hub.stewards.findOne(stewardKey);
    return new ArrayList<Model>(steward.getModels());
  }

}
