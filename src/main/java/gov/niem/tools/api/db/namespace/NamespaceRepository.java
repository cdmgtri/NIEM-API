package gov.niem.tools.api.db.namespace;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NamespaceRepository extends JpaRepository<Namespace, Long> {

  Optional<Namespace> findOneByVersion_IdAndPrefix(Long versionId, String prefix);

}
