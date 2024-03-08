package gov.niem.tools.api.db.namespace;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.niem.tools.api.db.base.BaseEntityService;
import gov.niem.tools.api.db.exceptions.EntityNotFoundException;
import gov.niem.tools.api.db.exceptions.EntityNotUniqueException;
import gov.niem.tools.api.db.exceptions.FieldNotFoundException;
import gov.niem.tools.api.db.version.Version;
import gov.niem.tools.api.db.version.VersionService;

@Service
public class NamespaceService extends BaseEntityService<Namespace> {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  NamespaceRepository repo;

  @Autowired
  VersionService versionService;

  public NamespaceRepository repository() {
    return this.repo;
  }

  @Transactional
  public Namespace add(Version version, String prefix, String name) throws Exception {
    Namespace namespace = Namespace.builder()
    .version(version)
    .prefix(prefix)
    .name(name)
    .build();
    return this.add(namespace);
  }

  @Transactional
  public Namespace add(String stewardKey, String modelKey, String versionNumber, String prefix, String name) throws Exception {
    Namespace namespace = Namespace.builder()
    .prefix(prefix)
    .name(name)
    .build();
    return this.add(stewardKey, modelKey, versionNumber, namespace);
  }

  @Transactional
  public Namespace add(String stewardKey, String modelKey, String versionNumber, String prefix) throws Exception {
    Namespace namespace = Namespace.builder().prefix(prefix).build();
    return this.add(stewardKey, modelKey, versionNumber, namespace);
  }

  @Transactional
  public Namespace add(String stewardKey, String modelKey, String versionNumber, Namespace namespace) throws Exception {
    Version version = versionService.findOne(stewardKey, modelKey, versionNumber);
    return this.add(version, namespace);
  }

  @Transactional
  public Namespace add(Version version, Namespace namespace) throws Exception {
    namespace.setVersion(version);
    Namespace result = super.add(namespace);
    version.getNamespaces().add(result);
    return result;
  }

  public List<Namespace> findAll() {
    return repo.findAll();
  }

  public Set<Namespace> findByKeys(String stewardKey, String modelKey, String versionNumber) throws EntityNotFoundException {
    Version version = versionService.findOne(stewardKey, modelKey, versionNumber);
    return version.getNamespaces();
  }

  public Optional<Namespace> findOneOptional(Version version, String prefix) throws EntityNotFoundException {
    return repo.findOneByVersion_IdAndPrefix(version.getId(), prefix);
  }

  public Optional<Namespace> findOneOptional(String stewardKey, String modelKey, String versionNumber, String prefix) throws EntityNotFoundException {
    Version version = versionService.findOne(stewardKey, modelKey, versionNumber);
    return repo.findOneByVersion_IdAndPrefix(version.getId(), prefix);
  }

  public Namespace findOne(String stewardKey, String modelKey, String versionNumber, String prefix) throws EntityNotFoundException {
    return this.findOneOptional(stewardKey, modelKey, versionNumber, prefix)
    .orElseThrow(() -> this.getNotFoundException(prefix));
  }

  public Namespace findOne(Version version, String prefix) throws EntityNotFoundException {
    return this.findOneOptional(version, prefix)
    .orElseThrow(() -> this.getNotFoundException(prefix));
  }

  public Namespace findOne(Namespace namespace) throws EntityNotFoundException {
    return this.findOne(namespace.getVersion(), namespace.getPrefix());
  }

  public void assertRequiredLocalFields(Namespace namespace) throws FieldNotFoundException {
    assertFieldNotNullAndNotEmpty("prefix", namespace.getPrefix());
  }

  public void assertUnique(Namespace namespace) throws EntityNotUniqueException {
    repo
    .findOneByVersion_IdAndPrefix(namespace.getVersion().getId(), namespace.getPrefix())
    .ifPresent(n -> this.throwNotUnique(n));
  }

}
