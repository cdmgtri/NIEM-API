package gov.niem.tools.api.db.component;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ComponentRepository<T extends Component<T>> extends JpaRepository<T, Long> {

  Optional<T> findOneByNamespace_IdAndName(Long namespaceId, String name);

  List<T> findByNamespace_Version_IdOrderByNamespace_PrefixAscNameAsc(Long versionId);
  List<T> findByNamespace_IdOrderByNamespace_PrefixAscNameAsc(Long namespaceId);
  List<T> findByNamespace_PrefixOrderByNameAsc(String prefix);

}
