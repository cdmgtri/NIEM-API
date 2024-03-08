package gov.niem.tools.api.db.facet;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import gov.niem.tools.api.db.base.BaseEntityService;
import gov.niem.tools.api.db.component.Component;
import gov.niem.tools.api.db.exceptions.EntityNotFoundException;
import gov.niem.tools.api.db.exceptions.EntityNotUniqueException;
import gov.niem.tools.api.db.exceptions.FieldNotFoundException;
import gov.niem.tools.api.db.facet.Facet.Category;
import gov.niem.tools.api.db.type.Type;
import gov.niem.tools.api.db.type.TypeService;
import gov.niem.tools.api.db.version.Version;
import gov.niem.tools.api.db.version.VersionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@org.springframework.stereotype.Component
public class FacetService extends BaseEntityService<Facet> {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  FacetRepository repo;

  @Autowired
  VersionService versionService;

  @Autowired
  TypeService typeService;

  public FacetRepository repository() {
    return this.repo;
  }

  @Transactional
  public Facet add(String stewardKey, String modelKey, String versionNumber, String qname, Category category, String value, String definition) throws Exception {
    Type type = typeService.findOne(stewardKey, modelKey, versionNumber, qname);
    return this.add(type, category, value, definition);
  }

  @Transactional
  public Facet add(Type type, Category category, String value, String definition) throws Exception {
    Facet facet = new Facet();
    facet.setType(type);
    facet.setCategory(category);
    facet.setValue(value);
    facet.setDefinition(definition);
    facet = super.add(facet);
    type.getFacets().add(facet);
    return facet;
  }

  public Optional<Facet> findOneOptional(String stewardKey, String modelKey, String versionNumber, String qname, Category category, String value) throws EntityNotFoundException {
    Long versionId = versionService.findId(stewardKey, modelKey, versionNumber);
    return repo.findOneByVersionIdAndPrefixAndType_NameAndCategoryAndValue(versionId, Component.getPrefix(qname), Component.getName(qname), category, value);
  }

  public Facet findOne(String stewardKey, String modelKey, String versionNumber, String qname, Category category, String value) throws EntityNotFoundException {
    return this
    .findOneOptional(stewardKey, modelKey, versionNumber, qname, category, value)
    .orElseThrow(() -> this.getNotFoundException(qname + "/" + category.toString() + "=" + value));
  }

  public Facet findOne(Facet facet) throws EntityNotFoundException {
    if (facet.getType() == null || facet.getType().getVersion() == null) {
      throw new EntityNotFoundException(facet);
    }
    return this.findOne(facet.getStewardKey(), facet.getModelKey(), facet.getVersionNumber(), facet.getQname(), facet.getCategory(), facet.getValue());
  }

  public Set<Facet> find(String stewardKey, String modelKey, String versionNumber, String qname) throws EntityNotFoundException {
    Version version = versionService.findOne(stewardKey, modelKey, versionNumber);
    return repo.findByVersionIdAndPrefixAndType_Name(version.getId(), Component.getPrefix(qname), Component.getName(qname));
  }

  public void assertRequiredLocalFields(Facet facet) throws FieldNotFoundException {
    assertFieldNotNullAndNotEmpty("qname", facet.getQname());
    assertFieldNotNullAndNotEmpty("value", facet.getValue());
    if (facet.getCategory() == Category.enumeration || facet.getCategory() == Category.pattern) {
      assertFieldNotNullAndNotEmpty("definition", facet.getDefinition());
    }
  }

  public void assertUnique(Facet facet) throws EntityNotUniqueException {
    this
    .findOneOptional(facet)
    .ifPresent(result -> this.throwNotUnique(facet));
  }

}
