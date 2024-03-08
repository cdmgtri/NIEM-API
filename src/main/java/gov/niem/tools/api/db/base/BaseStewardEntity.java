package gov.niem.tools.api.db.base;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.niem.tools.api.db.steward.Steward;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Adds reusable methods for entities that belong to a steward: Model, Version, Namespace, etc.
 */
@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class BaseStewardEntity extends BaseEntity {

  public abstract Steward getSteward();

  // @JacksonXmlProperty(localName = "api:StewardKeyID")
  // @Schema(
  //   example = "niem",
  //   description = "A human-readable and url-friendly steward identifier generated from the steward shortName field.")
  @JsonIgnore
  public String getStewardKey() {
    if (this.getSteward() == null) {
      return null;
    }
    return this.getSteward().getStewardKey();
  }

  @JsonProperty("steward")
  public Map<String, String> getStewardSummary() {
    return this.getSteward().toSummary();
  }

  // @JacksonXmlProperty(localName = "api:StewardRouteID")
  // @Schema(
  //   example = "https://tools.niem.gov/api/v2/stewards/niem",
  //   description = "An endpoint to get information about a steward.")
  // public String getStewardRoute() {
  //   if (this.getSteward() == null) {
  //     return null;
  //   }
  //   return this.getSteward().getRoute();
  // }

  // @JacksonXmlProperty(localName = "api:StewardShortName")
  // @Schema(
  //   example = "NIEM",
  //   description = "A short name or acronym used to identify a steward. This could be the name of an organization or unit, a program name, or other kind of authoritative source.")
  // public String getStewardShortName() {
  //   if (this.getSteward() == null) {
  //     return null;
  //   }
  //   return this.getSteward().getShortName();
  // }

}
