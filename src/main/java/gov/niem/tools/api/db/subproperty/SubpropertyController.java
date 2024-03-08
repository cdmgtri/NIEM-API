package gov.niem.tools.api.db.subproperty;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.niem.tools.api.core.config.Config.AppMediaType;
import gov.niem.tools.api.core.utils.CmfUtils;
import gov.niem.tools.api.db.ServiceHub;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("stewards/{stewardKey}/models/{modelKey}/versions/{versionNumber}")
@Tag(name = "Data-7: Subproperties", description = "A subproperty is the occurrence of a property as it appears when contained by a type.  It carries additional characteristics in this situation, such as cardinality constraints.")
public class SubpropertyController {

  @Autowired
  ServiceHub hub;

  @GetMapping("/types/{typeQname}/subproperties/{propertyQname}")
  public Subproperty getSubproperty(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String typeQname,
      @PathVariable String propertyQname) throws Exception {
    return hub.subproperties.findOne(stewardKey, modelKey, versionNumber, typeQname, propertyQname);
  }

  @GetMapping("/types.cmf/{typeQname}/subproperties/{propertyQname}")
  public Object getSubpropertyCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String typeQname,
      @PathVariable String propertyQname,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {
    Subproperty subproperty = hub.subproperties.findOne(stewardKey, modelKey, versionNumber, typeQname, propertyQname);
    org.mitre.niem.cmf.Model cmfModel = new org.mitre.niem.cmf.Model();
    subproperty.addToCmfModel(cmfModel);
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  @GetMapping("/subproperties")
  public Set<Subproperty> getAllSubproperties(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionNumber) throws Exception {
    return hub.subproperties.findByVersion(stewardKey, modelKey, versionNumber);
  }

  @GetMapping("/types/{typeQname}/subproperties")
  public Set<Subproperty> getTypeSubproperties(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String typeQname) throws Exception {
    return hub.subproperties.findByType(stewardKey, modelKey, versionNumber, typeQname);
  }

  @GetMapping("/types.cmf/{typeQname}/subproperties")
  public Object getTypeSubpropertiesCmf(
      @PathVariable String stewardKey,
      @PathVariable String modelKey,
      @PathVariable String versionNumber,
      @PathVariable String typeQname,
      @RequestParam(required = false, defaultValue = "json") AppMediaType mediaType) throws Exception {
    Set<Subproperty> subproperties = hub.subproperties.findByType(stewardKey, modelKey, versionNumber, typeQname);
    org.mitre.niem.cmf.Model cmfModel = new org.mitre.niem.cmf.Model();
    for(Subproperty subproperty : subproperties) {
      subproperty.addToCmfModel(cmfModel);
    }
    return CmfUtils.generateString(cmfModel, mediaType);
  }

  @GetMapping("/properties/{propertyQname}/subproperties")
  public Set<Subproperty> getPropertySubproperties(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionNumber, @PathVariable String propertyQname) throws Exception {
    return hub.subproperties.findByProperty(stewardKey, modelKey, versionNumber, propertyQname);
  }

  @GetMapping("/namespaces/{prefix}/subproperties")
  public Set<Subproperty> getNamespaceSubproperties(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionNumber, @PathVariable String prefix) throws Exception {
    return hub.subproperties.findByTypePrefix(stewardKey, modelKey, versionNumber, prefix);
  }

  // @PostMapping("/subproperties")
  // public ResponseEntity<String> postSubproperty(@PathVariable String stewardKey, @PathVariable String modelKey, @PathVariable String versionNumber, Subproperty subproperty) throws Exception {
  //   String message = hub.subproperties.add(stewardKey, modelKey, versionNumber, subproperty.getTypeQname(), subproperty.getPropertyQname(), subproperty.getMin(), subproperty.getMax());
  //   return AppUtils.getResponseOkString(message);
  // }

}
