package gov.niem.tools.api.db.base;

import java.util.Map;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.niem.tools.api.db.namespace.Namespace;
import gov.niem.tools.api.db.version.Version;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Adds reusable methods for entities that belong to a namespace:
 * Property, Type, Facet, etc.
 */
@MappedSuperclass
@Audited
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class BaseNamespaceEntity<T extends BaseVersionEntity<T>> extends BaseVersionEntity<T> {

  @JsonIgnore
  public abstract Namespace getNamespace();

  @JsonIgnore
  public Long getNamespaceId() {
    if (this.getNamespace() == null) {
      return null;
    }
    return this.getNamespace().getId();
  }

  @JsonProperty("namespace")
  public Map<String, String> getNamespaceSummary() {
    return this.getNamespace().toSummary();
  }

  // /**
  //  * A short, non-normative identifier for a namespace.
  //  */
  // @JacksonXmlProperty(localName = "api:NamespacePrefixID")
  // @Schema(example = "nc")
  // public String getPrefix() {
  //   if (this.getNamespace() == null) {
  //     return null;
  //   }
  //   return this.getNamespace().getPrefix();
  // }

  // /**
  //  * A normative identifier for a namespace.
  //  */
  // @JacksonXmlProperty(localName = "api:NamespaceURI")
  // @Schema(example = "http://release.niem.gov/niem/niem-core/5.0")
  // public String getNamespaceURI() {
  //   if (this.getNamespace() == null) {
  //     return null;
  //   }
  //   return this.getNamespace().getUri();
  // }

  // /**
  //  * A kind of namespace.
  //  */
  // @JacksonXmlProperty(localName = "api:NamespaceCategoryCode")
  // @Schema(example = "core")
  // public Category getNamespaceCategory() {
  //   if (this.getNamespace() == null) {
  //     return null;
  //   }
  //   return this.getNamespace().getCategory();
  // }

  // /**
  //  * A name of a namespace.
  //  */
  // @JacksonXmlProperty(localName = "api:NamespaceName")
  // @Schema(example = "NIEM Core")
  // public String getNamespaceName() {
  //   if (this.getNamespace() == null) {
  //     return null;
  //   }
  //   return this.getNamespace().getName();
  // }

  @JsonIgnore
  public Version getVersion() {
    if (this.getNamespace() == null) {
      return null;
    }
    return this.getNamespace().getVersion();
  }

}
