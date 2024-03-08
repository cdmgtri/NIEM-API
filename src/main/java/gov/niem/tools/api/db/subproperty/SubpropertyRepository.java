package gov.niem.tools.api.db.subproperty;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubpropertyRepository extends JpaRepository<Subproperty, Long> {

  Optional<Subproperty> findOneByVersionIdAndType_Namespace_PrefixAndType_NameAndProperty_Namespace_PrefixAndProperty_Name(Long versionId, String typePrefix, String typeName, String propertyPrefix, String propertyName);

  Set<Subproperty> findByVersionIdAndType_Namespace_PrefixAndType_Name(Long versionId, String prefix, String name);
  Set<Subproperty> findByVersionIdAndProperty_Namespace_PrefixAndProperty_Name(Long versionId, String prefix, String name);

  Set<Subproperty> findByVersionIdAndType_Namespace_Prefix(Long versionId, String prefix);
  Set<Subproperty> findByVersionIdAndProperty_Namespace_Prefix(Long versionId, String prefix);

  Set<Subproperty> findByVersionId(Long versionId);

}
