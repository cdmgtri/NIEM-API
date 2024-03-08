package gov.niem.tools.api.db.property;

import gov.niem.tools.api.db.component.ComponentRepository;

public interface PropertyRepository extends ComponentRepository<Property> {

  // TODO: Clean up property repository

  // Optional<Property> findOneByVersionIdAndQname(Long versionId, String qname);

  // List<Property> findByVersionIdOrderByQname(Long versionId);
  // List<Property> findByNamespaceIdOrderByQname(Long namespaceId);
  // List<Property> findByPrefixOrderByNameAsc(String prefix);

}
