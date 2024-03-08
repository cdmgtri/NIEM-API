package gov.niem.tools.api.db.component;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;

import gov.niem.tools.api.db.base.BaseEntityService;
import gov.niem.tools.api.db.exceptions.EntityNotFoundException;
import gov.niem.tools.api.db.exceptions.EntityNotUniqueException;
import gov.niem.tools.api.db.exceptions.FieldNotFoundException;
import gov.niem.tools.api.db.namespace.Namespace;
import gov.niem.tools.api.db.namespace.NamespaceService;
import gov.niem.tools.api.db.version.Version;
import gov.niem.tools.api.db.version.VersionService;
import jakarta.transaction.Transactional;

public abstract class ComponentService<T extends Component<T>, U extends ComponentRepository<T>> extends BaseEntityService<T> {

  @Autowired
  protected U repo;

  @Autowired
  protected VersionService versionService;

  @Autowired
  protected NamespaceService namespaceService;

  public U repository() {
    return this.repo;
  }

  @Transactional
  public T add(String stewardKey, String modelKey, String versionKey, T component) throws Exception {
    if (component.getNamespace() == null) {
      Namespace namespace = namespaceService.findOne(stewardKey, modelKey, versionKey, component.getPrefix());
      component.setNamespace(namespace);
    }
    Version version = versionService.findOne(stewardKey, modelKey, versionKey);
    return this.add(version, component);
  }

  @Transactional
  public T add(Version version, String prefix, String name, T component) throws Exception {
    Namespace namespace = namespaceService.findOne(version, prefix);
    return this.add(namespace, name, component);
  }

  @Transactional
  public T add(Version version, T component) throws Exception {
    component.getNamespace().setVersion(version);
    return this.add(component);
  }

  @Transactional
  public T add(Namespace namespace, String name, T component) throws Exception {
    component.setName(name);
    component.setPrefix(namespace.getPrefix());
    return this.add(namespace, component);
  }

  @Transactional
  public T add(Namespace namespace, T component) throws Exception {
    component.setNamespace(namespace);
    component.setPrefix(namespace.getPrefix());
    return this.add(component);
  }

  @Transactional
  public T add(T component) throws Exception {
    component = super.add(component);
    return component;
  }

  public T findOne(T component) throws EntityNotFoundException {
    return this.findOne(component.getStewardKey(), component.getModelKey(), component.getVersionNumber(), component.getPrefix(), component.getName());
  }

  public T findOne(Version version, String qname) throws EntityNotFoundException {
    return this.findOne(version.getStewardKey(), version.getModelKey(), version.getVersionNumber(), qname);
  }

  public T findOne(String stewardKey, String modelKey, String versionNumber, String qname) throws EntityNotFoundException {
    return this
        .findOneOptional(stewardKey, modelKey, versionNumber, qname)
        .orElseThrow(() -> this.getNotFoundException(qname));
  }

  public T findOne(String stewardKey, String modelKey, String versionNumber, String prefix, String name) throws EntityNotFoundException {
    return this
        .findOneOptional(stewardKey, modelKey, versionNumber, prefix, name)
        .orElseThrow(() -> this.getNotFoundException(prefix + ":" + name));
  }

  public Optional<T> findOneOptional(Version version, String qname) throws EntityNotFoundException {
    // return repo.findOneByNamespace_Version_IdAndQname(version.getId(), qname);
    return this.findOneOptional(version.getStewardKey(), version.getModelKey(), version.getVersionNumber(), getQualifiedPrefix(qname), getQualifiedName(qname));
  }

  public Optional<T> findOneOptional(String stewardKey, String modelKey,
      String versionNumber, String qname) throws EntityNotFoundException {
    // Version version = versionService.findOne(stewardKey, modelKey, versionNumber);
    // return repo.findOneByNamespace_Version_IdAndQname(version.getId(), qname);
    return this.findOneOptional(stewardKey, modelKey, versionNumber, getQualifiedPrefix(qname), getQualifiedName(qname));
  }

  @SuppressWarnings("unchecked")
  public Optional<T> findOneOptional(String stewardKey, String modelKey,
      String versionNumber, String prefix, String name) throws EntityNotFoundException {

    Namespace namespace = namespaceService.findOne(stewardKey, modelKey, versionNumber, prefix);
    Optional<T> result = repo.findOneByNamespace_IdAndName(namespace.getId(), name);

    // Initialize if the result is a Hibernate proxy due to lazy loading
    if (result.isPresent()) {
      T component = result.get();
      if (component instanceof HibernateProxy) {
        component = (T) Hibernate.unproxy(component);
      }
      return Optional.of(component);
    }

    return result;
  }

  public List<T> findByVersion(String stewardKey, String modelKey, String versionKey) throws Exception {
    Version version = versionService.findOne(stewardKey, modelKey, versionKey);
    return repo.findByNamespace_Version_IdOrderByNamespace_PrefixAscNameAsc(version.getId());
  }

  public List<T> findByNamespace(String stewardKey, String modelKey, String versionKey, String prefix) throws Exception {
    Namespace namespace = namespaceService.findOne(stewardKey, modelKey, versionKey, prefix);
    return repo.findByNamespace_IdOrderByNamespace_PrefixAscNameAsc(namespace.getId());
  }

  public void assertRequiredLocalFields(T component) throws FieldNotFoundException {
    assertFieldNotNullAndNotEmpty("prefix", component.getPrefix());
    assertFieldNotNullAndNotEmpty("name", component.getName());
  }

  public void assertUnique(T component) throws EntityNotUniqueException {
    this.repository()
    .findOneByNamespace_IdAndName(component.getNamespaceId(), component.getName())
    .ifPresent(c -> this.throwNotUnique(component));
  }

  public static String getQualifiedPrefix(String qname) {
    if (qname.contains(":")) {
      return qname.split(":")[0];
    }
    return null;
  }

  public static String getQualifiedName(String qname) {
    if (qname.contains(":")) {
      return qname.split(":")[1];
    }
    return null;
  }

}
