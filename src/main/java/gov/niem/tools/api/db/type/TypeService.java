package gov.niem.tools.api.db.type;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import gov.niem.tools.api.db.component.ComponentService;
import gov.niem.tools.api.db.namespace.Namespace;
import gov.niem.tools.api.db.version.Version;

@Component
public class TypeService extends ComponentService<Type, TypeRepository> {

  @PersistenceContext
  private EntityManager em;

  @Transactional
  public Type add(String stewardKey, String modelKey, String versionNumber, String prefix, String name) throws Exception {
    Type type = new Type();
    type.setPrefix(prefix);
    type.setName(name);
    return this.add(stewardKey, modelKey, versionNumber, type);
  }

  @Transactional
  public Type add(Version version, String prefix, String name) throws Exception {
    Type type = new Type();
    return this.add(version, prefix, name, type);
  }

  @Transactional
  public Type add(Namespace namespace, String name) throws Exception {
    Type type = new Type();
    return this.add(namespace, name, type);
  }

}
