package gov.niem.tools.api.db.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.niem.tools.api.db.base.BaseEntityService;
import gov.niem.tools.api.db.exceptions.EntityNotFoundException;
import gov.niem.tools.api.db.exceptions.EntityNotUniqueException;
import gov.niem.tools.api.db.exceptions.FieldNotFoundException;
import gov.niem.tools.api.db.steward.Steward;
import gov.niem.tools.api.db.steward.StewardService;
import gov.niem.tools.api.db.version.Version;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService extends BaseEntityService<Model> {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  ModelRepository repo;

  @Autowired
  StewardService stewardService;

  public ModelRepository repository() {
    return this.repo;
  }

  @Transactional
  public Model add(String stewardKey, String shortName, String longName, Model.Category category) throws Exception {
    return add(stewardKey, shortName, longName, category, null);
  }

  @Transactional
  public Model add(String stewardKey, String shortName, String longName, Model.Category category, String description) throws Exception {
    Model model = new Model(shortName, longName, category);
    model.setDescription(description);
    return add(stewardKey, model);
  }

  @Transactional
  public Model add(Model model) throws Exception {
    return add(model.getStewardKey(), model);
  }

  @Transactional
  public Model add(String stewardKey, Model model) throws Exception {
    Steward steward = stewardService.findOne(stewardKey);
    model.setSteward(steward);
    model = super.add(model);

    // Update the other side of the relationship
    steward.getModels().add(model);
    stewardService.saveExisting(steward);

    // TODO: Model stewardship
    // addStewardship(model, steward, true, null, null);
    // addUserToModel(model )
    // log the status

    return model;
  }

  @Transactional
  public Model add(Steward steward, String shortName, String longName, Model.Category category, String description) throws Exception {
    Model model = new Model(shortName, longName, category);
    model.setSteward(steward);
    model.setDescription(description);
    model = super.add(model);

    steward.getModels().add(model);
    return model;
  }

  @Transactional
  public Model edit(String stewardKey, String modelKey, Model updatedModel) throws Exception {
    Steward oldSteward = stewardService.findOne(stewardKey);
    return this.edit(oldSteward, modelKey, updatedModel);
  }

  @Transactional
  public Model edit(Steward steward, String modelKey, Model updatedModel) throws Exception {
    Model originalModel = this.findOne(steward.getStewardKey(), modelKey);
    if (steward.getStewardKey() != updatedModel.getStewardKey()) {
      // TODO: edit model check permission to move to new steward
    }
    if (modelKey != updatedModel.getModelKey()) {
      assertUnique(updatedModel);
    }
    return edit(originalModel.getId(), updatedModel);
  }

  @Transactional
  public void delete(String stewardKey, String modelKey) throws Exception {
    Model model = this.findOne(stewardKey, modelKey);
    this.delete(model);
  }

  public Model findOne(Model model) throws EntityNotFoundException {
    return this.findOne(model.getStewardKey(), model.getModelKey());
  }

  public Optional<Model> findOneOptional(String stewardKey, String modelKey) throws EntityNotFoundException {
    Steward steward = stewardService.findOne(stewardKey);
    return steward.getModelOptional(modelKey);
  }

  public Model findOne(String stewardKey, String modelKey) throws EntityNotFoundException {
    return this
    .findOneOptional(stewardKey, modelKey)
    .orElseThrow(() -> this.getNotFoundException(stewardKey + "/" + modelKey));
  }

  public Optional<Model> findOneByShortNameOptional(String stewardKey, String shortName) {
    return repo.findOneBySteward_StewardKeyAndShortName(stewardKey, shortName);
  }

  public Model findOneByShortName(String stewardKey, String shortName) throws Exception {
    return this
    .findOneByShortNameOptional(stewardKey, shortName)
    .orElseThrow(() -> this.getNotFoundException(stewardKey + ", " + shortName));
  }

  public Model findOneNiem() throws EntityNotFoundException {
    return this.findOne(Steward.niemStewardKey, Model.niemModelKey);
  }

  public Version currentNiemVersion() {
    Model niem = this.findOneNiem();
    return niem.getVersions()
    .stream()
    .filter(version -> version.isCurrent() == true)
    .findFirst()
    .get();
  }

  public List<Model> findAll() {
    return repo.findAll();
  }

  // TODO: Support model stewardships

  // @Transactional
  // public void addStewardship(Model model, Steward steward, Boolean isCurrent, String from, String to) {
  //   ModelStewardship stewardship = new ModelStewardship(model, steward, isCurrent, from, to);
  //   em.persist(stewardship);
  // }

  // @Transactional
  // public void addStewardship(Model model, String stewardKey, Boolean isCurrent, String from, String to) throws Exception {
  //   Steward steward = stewardService.findOneByKey(stewardKey);
  //   ModelStewardship stewardship = new ModelStewardship(model, steward, isCurrent, from, to);
  //   em.persist(stewardship);
  // }

  public void assertRequiredLocalFields(Model model) throws FieldNotFoundException {
    assertFieldNotNullAndNotEmpty("shortName", model.getShortName());
  }

  public void assertUnique(Model model) throws EntityNotUniqueException {
    assertShortNameDoesNotExist(model.getStewardKey(), model.getShortName());
  }

  private void assertShortNameDoesNotExist(String stewardKey, String modelShortName) throws EntityNotUniqueException {
    List<Model> models = repo.findByShortName(modelShortName);
    models.forEach(model -> System.out.println(model.getFullIdentifier()));
    // TODO: Fix ModelService short name does not exist

    repo
    .findOneBySteward_StewardKeyAndShortName(stewardKey, modelShortName)
    .ifPresent(model -> this.throwNotUnique("Model", String.format("[%s] %s", stewardKey, modelShortName)));
  }

}
