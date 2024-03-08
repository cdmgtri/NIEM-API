package gov.niem.tools.api.db.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import gov.niem.tools.api.core.config.Config.AppMediaType;
import gov.niem.tools.api.core.utils.CmfUtils;
import gov.niem.tools.api.db.ServiceHub;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/stewards/{stewardKey}/models/{modelKey}")
@Tag(name = "Data-3: Versions", description = "An instance of a model, e.g., release.")
public class VersionController {

  @Autowired
  ServiceHub hub;

  @GetMapping("/versions/{versionKey}")
  @Operation(summary = "Get a version")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Version getVersion(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionKey) throws Exception {
    return hub.versions.findOne(stewardKey, modelKey, versionKey);
  }

  @GetMapping("/versions.cmf/{versionKey}")
  @Operation(summary = "Get a version")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Object getVersionCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionKey,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {
    Version version = hub.versions.findOne(stewardKey, modelKey, versionKey);
    org.mitre.niem.cmf.Model cmfModel = new org.mitre.niem.cmf.Model();
    version.addToCmfModel(cmfModel);
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  @GetMapping("/versions")
  @Operation(summary = "Get all versions of a model.")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Version> getVersions(@PathVariable String stewardKey, @PathVariable String modelKey) throws Exception {
    if (stewardKey.equals("*") && modelKey.equals("*")) {
      return hub.versions.findAll();
    }
    return new ArrayList<Version>(hub.versions.findByKeys(stewardKey, modelKey));
  }

  // @GetMapping("/versions/{versionKey}/catalog")
  // public Catalog getCatalog(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionKey) throws Exception {
  //   Version version = hub.versions.findOne(stewardKey, modelKey, versionKey);
  //   return version.getCatalog();
  // }

  // @PostMapping("/versions")
  // public ResponseEntity<String> postRelease(@PathVariable String stewardSlug, @PathVariable String modelSlug, @RequestBody Version version) throws Exception {
  //   String message = hub.versions.add(stewardSlug, modelSlug, version);
  //   return AppUtils.getResponseOkString(message);
  // }

}
