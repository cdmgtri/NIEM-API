package gov.niem.tools.api.db.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import gov.niem.tools.api.core.config.Config;
import gov.niem.tools.api.db.base.BaseCmfEntity;
import gov.niem.tools.api.db.component.Component;
import gov.niem.tools.api.db.facet.Facet;
import gov.niem.tools.api.db.property.Property;
import gov.niem.tools.api.db.subproperty.Subproperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.hibernate.proxy.HibernateProxy;
import org.mitre.niem.cmf.CMFException;
import org.mitre.niem.cmf.ClassType;
import org.mitre.niem.cmf.Datatype;
import org.mitre.niem.cmf.RestrictionOf;
import org.mitre.niem.cmf.UnionOf;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Audited
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JacksonXmlRootElement(localName = "api:Type")
@Schema(name = "Type")
@Table(
  uniqueConstraints = {@UniqueConstraint(
    name = "type_namespace_name_key", columnNames = { "namespace_id", "name" })
  },
  indexes = {
    @Index(name = "type_category_idx", columnList = "category"),
    @Index(name = "type_base_id_idx", columnList = "base_id")
  }
)
public class Type extends Component<Type> implements BaseCmfEntity<org.mitre.niem.cmf.Component> {

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="base_id", referencedColumnName="id")
  private Type base;

  public enum Category {
    complex_object,
    complex_value,
    simple_value
  }

  @Enumerated(EnumType.STRING)
  private Category category;

  public enum Derivation {
    extension,
    restriction
  }

  @Enumerated(EnumType.STRING)
  private Derivation derivation;

  public enum Pattern {
    object,
    adapter,
    association,
    augmentation,
    metadata,
    complex_value,
    simple_value,
    simple_list,
    simple_union
  }

  @Enumerated(EnumType.STRING)
  private Pattern pattern;

  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(mappedBy = "type")
  private Set<Property> dataProperties = new HashSet<>();

  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
  private Set<Subproperty> subproperties = new HashSet<>();

  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(mappedBy = "type")
  private Set<Facet> facets = new HashSet<>();

  public Type getBase() {
   Type base = this.base;
    if (base instanceof HibernateProxy) {
      base = Hibernate.unproxy(base, Type.class);
    }
    return base;
  }

  @JsonProperty("isComplex")
  public boolean isComplex() {
    if (this.getCategory().toString().startsWith("complex")) {
      return true;
    }
    return false;
  }

  @JsonProperty("isComplexContent")
  public boolean isComplexContent() {
    if (this.getCategory().equals(Category.complex_object)) {
      return true;
    }
    return false;
  }

  @JsonProperty("isSimple")
  public boolean isSimple() {
    if (this.getCategory().toString().startsWith("simple")) {
      return true;
    }
    return false;
  }

  @JsonProperty("isSimpleContent")
  public boolean isSimpleContent() {
    if (this.getCategory().equals(Category.complex_object)) {
      return false;
    }
    return true;
  }

  @JsonProperty("base")
  public Map<String, String> getBaseSummary() {
    return this.base == null ? null : this.base.toSummary();
  }

  @Override
  @Schema(
    example = Config.BASE_URL + "/stewards/niem/models/crash-driver/version/1.1/types/nc:PersonType",
    description = "An endpoint to get information about a type.")
  public String getRoute() {
    String versionRoute = this.getVersion().getRoute();
    return String.format("%s/types/%s", versionRoute, this.getQname());
  }

  @Override
  @Schema(example = "Type", description = "A kind of NIEM entity, such as a Namespace or a Property.")
  public String getClassName() {
    return super.getClassName();
  }

  @Override
  @Schema(
    example = "niem/crash-driver/1.1/nc:PersonType",
    description = "A unique identifier.  For a type, this is combines the stewardKey, modelKey, versionNumber, prefix, and name fields.")
  public String getFullIdentifier() {
    return this.getVersion().getFullIdentifier() + "/" + this.getQname();
  }

  @Override
  @Schema(example = "nc:PersonType", description = "An identifier, unique within its immediate scope.  For a type, this is the same as the qname field (unique within its version).")
  public String getLocalIdentifier() {
    return this.getQname();
  }

  @Override
  @Schema(example = "NIEM Crash Driver 1.1: nc:PersonType", description = "A steward short name, model short name, version number, and qualified type name.")
  public String getTitle() {
    return super.getTitle();
  }

  public void addToCmfModel(org.mitre.niem.cmf.Model cmfModel) throws CMFException {
    cmfModel.addComponent(this.toCmf());
  }

  @Override
  public org.mitre.niem.cmf.Component toCmf() throws CMFException {
    if (this.isComplexContent()) {
      return this.toCmfClassType();
    }
    return this.toCmfDatatype();
  }

  public ClassType toCmfClassType() throws CMFException {
    if (this.isSimpleContent()) {
      return null;
    }

    ClassType classType = new ClassType(this.getNamespace().toCmf(), this.name);
    classType.setDefinition(definition);

    if (this.base != null) {
      classType.setExtensionOfClass(this.base.toCmfClassType());
    }

    // TODO: Set CMF class type is augmentable
    // classType.setIsAugmentable();

    // TODO: Set CMF class type is external
    // classType.setIsExternal();

    // TODO: Add CMF class type has property info
    // classType.addHasProperty(null);

    return classType;
  }

  public Datatype toCmfDatatype() throws CMFException {
    if (this.isComplexContent()) {
      return null;
    }

    Datatype datatype = new Datatype(this.getNamespace().toCmf(), this.name);
    datatype.setDefinition(definition);

    switch (this.pattern) {
      case simple_list:
        datatype.setListOf(this.base.toCmfDatatype());
        break;

        case simple_union:
        UnionOf unionOf = new UnionOf();
        // TODO: Add CMF data type union types
        // unionOf.addDatatype();
        datatype.setUnionOf(unionOf);
        break;

        case simple_value:
        RestrictionOf restrictionOf = new RestrictionOf();

        // TODO: Add CMF data type facets

        // XML Schema datatypes (e.g., xs:string) should not have a RestrictionOf property
        if (this.base == null || this.prefix.equals("xs")) {
          break;
        }

        datatype.setRestrictionOf(restrictionOf);

        // Set the restriction base
        Datatype baseDatatype = this.base.toCmfDatatype();
        restrictionOf.setDatatype(baseDatatype);

        // Add facets
        Set<Facet> facets = this.getFacets();
        for (Facet facet: facets) {
          restrictionOf.addFacet(facet.toCmf());
        }

      default:
        break;
    }
    return datatype;
  }

}
