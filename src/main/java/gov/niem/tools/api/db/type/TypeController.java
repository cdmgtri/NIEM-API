package gov.niem.tools.api.db.type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.niem.tools.api.core.config.Config.AppMediaType;
import gov.niem.tools.api.core.utils.CmfUtils;
import gov.niem.tools.api.db.ServiceHub;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("stewards/{stewardKey}/models/{modelKey}/versions/{versionNumber}")
@Tag(name = "Data-6: Types", description = "A type defines a structure - an allowable set of values. A type might describe a simple value (e.g., a string, a number) or a complex object (e.g., PersonType).")
public class TypeController {

  @Autowired
  ServiceHub hub;

  @GetMapping("/types/{qname}")
  @Operation(summary = "Get the type with the given qualified name")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Type getType(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String qname) throws Exception {
    return hub.types.findOne(stewardKey, modelKey, versionNumber, qname);
  }

  @GetMapping("/types.cmf/{qname}")
  @Operation(summary = "Get the type with the given qualified name")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public Object getTypeCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String qname,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {
    Type type = hub.types.findOne(stewardKey, modelKey, versionNumber, qname);
    org.mitre.niem.cmf.Model cmfModel = new org.mitre.niem.cmf.Model();
    cmfModel.addComponent(type.toCmf());
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  @GetMapping("/types")
  @Operation(summary = "Get all types matching the given parameters.  Pending pagination implementation.")
  @ResponseStatus(code = HttpStatus.OK)
  @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)
  public List<Type> getAllTypes(@PathVariable String stewardKey, @PathVariable String modelKey,
      @PathVariable String versionNumber) throws Exception {
    return null;
    // return hub.types.findByVersion(stewardKey, modelKey, versionNumber);
  }

  // @GetMapping("/datatypes")
  // @Operation(summary = "Get all types matching the given parameters that carry a value")
  // @ApiResponses(value = {
  //     @ApiResponse(responseCode = "200", description = "Success", content = {
  //         @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = org.mitre.niem.cmf.Datatype.class)))
  //     }),
  //     @ApiResponse(responseCode = "404", description = "Not Found", content = {
  //         @Content(mediaType = "application/json", schema = @Schema(type = "object"))
  //     })
  // })
  // public List<Datatype> getAllDataTypes(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionKey) throws Exception {
  //   // return hub.types.findByRelease(stewardKey, modelKey, versionNumber);
  //   return new ArrayList<Datatype>();
  // }

  // @GetMapping("/classes")
  // @Operation(summary = "Get all types matching the given parameters that contain properties")
  // @ApiResponses(value = {
  //     @ApiResponse(responseCode = "200", description = "Success", content = {
  //         @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = org.mitre.niem.cmf.ClassType.class)))
  //     }),
  //     @ApiResponse(responseCode = "404", description = "Not Found", content = {
  //         @Content(mediaType = "application/json", schema = @Schema(type = "object"))
  //     })
  // })
  // public List<ClassType> getAllClassTypes(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionKey) throws Exception {
  //   // return hub.types.findByRelease(stewardKey, modelKey, versionNumber);
  //   return new ArrayList<ClassType>();
  // }

  // @GetMapping("/datatypes/{qname}")
  // @Operation(summary = "Get the datatype with the given qualified name")
  // @ApiResponses(value = {
  //     @ApiResponse(responseCode = "200", description = "Success", content = {
  //         @Content(mediaType = "application/json", schema = @Schema(implementation = org.mitre.niem.cmf.Datatype.class))
  //     }),
  //     @ApiResponse(responseCode = "404", description = "Not Found", content = {
  //         @Content(mediaType = "application/json", schema = @Schema(type = "object"))
  //     })
  // })
  // public Datatype getDataType(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionNumber, @PathVariable String qname) throws Exception {
  //   // return hub.types.findOneByQname(stewardKey, modelKey, versionNumber, qname);
  //   return new Datatype();
  // }

  // @GetMapping("/classes/{qname}")
  // @Operation(summary = "Get the class with the given qualified name")
  // @ApiResponses(value = {
  //     @ApiResponse(responseCode = "200", description = "Success", content = {
  //         @Content(mediaType = "application/json", schema = @Schema(implementation = org.mitre.niem.cmf.ClassType.class))
  //     }),
  //     @ApiResponse(responseCode = "404", description = "Not Found", content = {
  //         @Content(mediaType = "application/json", schema = @Schema(type = "object"))
  //     })
  // })
  // public ClassType getClassType(@PathVariable String stewardKey, @PathVariable  String modelKey, @PathVariable String versionNumber, @PathVariable String qname) throws Exception {
  // // return hub.types.findOneByQname(stewardKey, modelKey, versionNumber, qname);
  //   return new ClassType();
  // }

  // @PostMapping("/types")
  // public ResponseEntity<String> postType(@PathVariable String stewardKey,
  //     @PathVariable String modelKey, @PathVariable String versionNumber, Type type)
  //     throws Exception {
  //   String message = hub.types.add(stewardKey, modelKey, versionNumber, type);
  //   return AppUtils.getResponseOkString(message);
  // }

}
