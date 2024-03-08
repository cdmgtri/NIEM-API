package gov.niem.tools.api.db.namespace;

import java.util.ArrayList;
import java.util.List;

import org.mitre.niem.cmf.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.niem.tools.api.core.config.Config.AppMediaType;
import gov.niem.tools.api.core.utils.CmfUtils;
import gov.niem.tools.api.db.ServiceHub;
import gov.niem.tools.api.db.property.Property;
import gov.niem.tools.api.db.type.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/stewards/{stewardKey}/models/{modelKey}/versions/{versionNumber}")
@Tag(name = "Data-4: Namespace", description = "A collection of properties and types managed by an authoritative source.")
public class NamespaceController {

  @Autowired
  ServiceHub hub;

  /**
   * Get basic details about a namespace.
   *
   * @example http://tools.niem.gov/api/v2/stewards/niem/models/model/versions/5.2/namespaces/nc
   */
  @GetMapping("/namespaces/{prefix}")
  @Operation(summary = "Get a namespace")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Namespace getNamespace(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String prefix) throws Exception {
    return hub.namespaces.findOne(stewardKey, modelKey, versionNumber, prefix);
  }

  /**
   * Get basic details about a namespace.
   *
   * @example http://tools.niem.gov/api/v2/stewards/niem/models/model/versions/5.2/namespaces.cmf/nc
   */
  @GetMapping("/namespaces.cmf/{prefix}")
  @Operation(summary = "Get a namespace as CMF")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Object getNamespaceCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String prefix,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {
    Namespace namespace = hub.namespaces.findOne(stewardKey, modelKey, versionNumber, prefix);
    org.mitre.niem.cmf.Model cmfModel = new Model();
    namespace.addToCmfModel(cmfModel);
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  /**
   * Get basic details about all namespaces in a version of a model.
   *
   * @example http://tools.niem.gov/api/v2/stewards/niem/models/model/versions/5.2/namespaces
   */
  @GetMapping("/namespaces")
  @Operation(summary = "Get all namespaces from a specific model version")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public List<Namespace> getVersionNamespaces(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber) throws Exception {

    return new ArrayList<Namespace>(hub.namespaces.findByKeys(stewardKey, modelKey, versionNumber));

  }

  /**
   * Get basic details about all namespaces in a version of a model.
   *
   * @example http://tools.niem.gov/api/v2/stewards/niem/models/model/versions/5.2/namespaces.cmf
   */
  @GetMapping("/namespaces.cmf")
  @Operation(summary = "Get all namespaces in CMF from a specific model version")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Object getVersionNamespacesCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {

    // Get results
    ArrayList<Namespace> namespaces = new ArrayList<Namespace>(hub.namespaces.findByKeys(stewardKey, modelKey, versionNumber));

    // Convert results to CMF
    org.mitre.niem.cmf.Model cmfModel = new Model();
    for(Namespace namespace : namespaces) {
      namespace.addToCmfModel(cmfModel);
    }
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  @GetMapping("/namespaces/{prefix}/types")
  @Operation(summary = "Get all types from a namespace.")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public List<Type> getNamespaceTypes(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String prefix) throws Exception {
    return hub.types.findByNamespace(stewardKey, modelKey, versionNumber, prefix);
  }

  @GetMapping("/namespaces.cmf/{prefix}/types")
  @Operation(summary = "Get all types from a namespace as CMF.")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Object getNamespaceTypesCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String prefix,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {
    List<Type> types = hub.types.findByNamespace(stewardKey, modelKey, versionNumber, prefix);
    org.mitre.niem.cmf.Model cmfModel = new org.mitre.niem.cmf.Model();
    for(Type type : types) {
      type.addToCmfModel(cmfModel);
    }
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  @GetMapping("/namespaces/{prefix}/properties")
  @Operation(summary = "Get all properties from a namespace")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public List<Property> getNamespaceProperties(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String prefix) throws Exception {
    return hub.properties.findByNamespace(stewardKey, modelKey, versionNumber, prefix);
  }

  @GetMapping("/namespaces.cmf/{prefix}/properties")
  @Operation(summary = "Get all properties from a namespace")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Object getNamespacePropertiesCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String prefix,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {
    List<Property> properties = hub.properties.findByNamespace(stewardKey, modelKey, versionNumber, prefix);
    org.mitre.niem.cmf.Model cmfModel = new Model();
    for (Property property : properties) {
      property.addToCmfModel(cmfModel);
    }
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  // @PostMapping("/namespaces")
  // public ResponseEntity<String> postNamespace(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionNumber, Namespace namespace) throws Exception {
  //   String message = hub.namespaces.add(stewardKey, modelKey, versionNumber, namespace);
  //   return AppUtils.getResponseOkString(message);
  // }

}
