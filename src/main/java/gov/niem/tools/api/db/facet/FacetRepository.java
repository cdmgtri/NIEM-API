package gov.niem.tools.api.db.facet;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.niem.tools.api.db.facet.Facet.Category;

public interface FacetRepository extends JpaRepository<Facet, Long> {

  Optional<Facet> findOneByVersionIdAndPrefixAndType_NameAndCategoryAndValue(Long versionId, String prefix, String name, Category category, String valueString);

  Set<Facet> findByVersionIdAndPrefix(Long versionId, String prefix);
  Set<Facet> findByVersionIdAndPrefixAndType_Name(Long versionId, String prefix, String name);

}
