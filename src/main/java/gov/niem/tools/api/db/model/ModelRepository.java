package gov.niem.tools.api.db.model;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {

  Optional<Model> findOneBySteward_StewardKeyAndModelKey(String stewardKey, String modelKey);
  Optional<Model> findOneBySteward_StewardKeyAndShortName(String stewardKey, String shortName);

  List<Model> findByCategory(String category);
  List<Model> findByShortName(String shortName);

  List<Model> findStewardsByStewardId(Long id);
  List<Model> findStewardsByCategory(String category);

}
