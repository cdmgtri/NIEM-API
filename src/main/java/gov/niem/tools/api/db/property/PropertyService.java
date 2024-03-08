package gov.niem.tools.api.db.property;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import gov.niem.tools.api.db.component.ComponentService;
import gov.niem.tools.api.db.namespace.Namespace;
import gov.niem.tools.api.db.version.Version;

@Component
public class PropertyService extends ComponentService<Property, PropertyRepository> {

  @PersistenceContext
  private EntityManager em;

  @Transactional
  public Property add(String stewardKey, String modelKey, String versionNumber, String prefix, String name)
      throws Exception {
    Property property = new Property();
    property.setPrefix(prefix);
    property.setName(name);
    return this.add(stewardKey, modelKey, versionNumber, property);
  }

  @Transactional
  public Property add(Version version, String prefix, String name) throws Exception {
    Property property = new Property();
    return this.add(version, prefix, name, property);
  }

  @Transactional
  public Property add(Namespace namespace, String name) throws Exception {
    Property property = new Property();
    return this.add(namespace, name, property);
  }

}
