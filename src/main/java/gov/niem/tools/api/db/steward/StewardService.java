package gov.niem.tools.api.db.steward;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.niem.tools.api.db.base.BaseEntityService;
import gov.niem.tools.api.db.exceptions.EntityNotFoundException;
import gov.niem.tools.api.db.exceptions.EntityNotUniqueException;
import gov.niem.tools.api.db.exceptions.FieldNotFoundException;

@Service
public class StewardService extends BaseEntityService<Steward> {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  public StewardRepository repo;

  public StewardRepository repository() {
    return this.repo;
  }

  @Transactional
  public Steward add(String shortName) throws Exception {
    return this.add(shortName, null, null, null);
  }

  @Transactional
  public Steward add(String shortName, String longName) throws Exception {
    return this.add(shortName, longName, null, null);
  }

  @Transactional
  public Steward add(String shortName, String longName, String category) throws Exception {
    return this.add(shortName, longName, Steward.Category.valueOf(category));
  }

  @Transactional
  public Steward add(String shortName, String longName, Steward.Category category) throws Exception {
    return this.add(shortName, longName, category, null);
  }

  @Transactional
  public Steward add(String shortName, String longName, Steward.Category category, String description) throws Exception {
    Steward steward = new Steward(shortName, longName);
    steward.setCategory(category);
    steward.setDescription(description);
    return this.add(steward);
  }

  @Transactional
  public Steward edit(String oldStewardKey, Steward updatedSteward) throws Exception {
    Steward oldSteward = this.findOne(oldStewardKey);

    // TODO: Edit collision check
    // Ensure new values that should be unique do not collide with existing ones
    if (updatedSteward.getShortName() != null) {
      if (! oldSteward.getShortName().equalsIgnoreCase(updatedSteward.getShortName())) {
        this.assertUnique(updatedSteward);
      }
    }

    return this.edit(oldSteward.getId(), updatedSteward);
  }

  @Transactional
  public void delete(String stewardKey) throws Exception {
    Steward steward = this.findOne(stewardKey);
    this.delete(steward);
  }

  // TODO: Add cascading deletes

  public Optional<Steward> findOneOptional(String stewardKey) {
    return repo.findOneByStewardKey(stewardKey);
  }

  public Steward findOne(Steward steward) throws EntityNotFoundException {
    return this.findOne(steward.getStewardKey());
  }

  public Steward findOne(String stewardKey) throws EntityNotFoundException {
    return repo
    .findOneByStewardKey(stewardKey)
    .orElseThrow(() -> new EntityNotFoundException("Steward", stewardKey));
  }

  public Steward findOneByShortName(String shortName) throws EntityNotFoundException {
    return repo
    .findOneByShortName(shortName)
    .orElseThrow(() -> new EntityNotFoundException("Steward", shortName));
  }

  public Steward findOneNiem() throws EntityNotFoundException {
    return this.findOne(Steward.niemStewardKey);
  }

  public void assertRequiredLocalFields(Steward steward) throws FieldNotFoundException {
    assertFieldNotNullAndNotEmpty("shortName", steward.getShortName());
  }

  public void assertUnique(Steward steward) throws EntityNotUniqueException {
    this.assertShortNameDoesNotExist(steward.getShortName());
    this.assertKeyDoesNotExist(steward.getShortName());
  }

  private void assertShortNameDoesNotExist(String shortName) throws EntityNotUniqueException {
    repo.findOneByShortName(shortName).ifPresent( steward -> this.throwNotUnique(steward, shortName) );
  }

  private void assertKeyDoesNotExist(String shortName) throws EntityNotUniqueException {
    String key = repo.slugify(shortName);
    repo.findOneByStewardKey(key).ifPresent( steward -> this.throwNotUnique(steward, key) );
  }

}
