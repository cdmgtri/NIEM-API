package gov.niem.tools.api.db;

import gov.niem.tools.api.db.model.Model;
import gov.niem.tools.api.db.namespace.Namespace;
import gov.niem.tools.api.db.niem.genc.CountryAlpha3CodeType;
import gov.niem.tools.api.db.property.Property;
import gov.niem.tools.api.db.steward.Steward;
import gov.niem.tools.api.db.type.Type;
import gov.niem.tools.api.db.version.Version;

public class TestData {

  protected static Steward nmo = Stewards.nmo();
  protected static Steward acme = Stewards.acme();

  public class Stewards {

    public static String nmoKey = "nmo";
    public static String acmeKey = "acme-co";

    public static Steward acme() {
      return Steward.builder()
          .shortName("ACME Co")
          .fullName("ACME Corporation")
          .category(Steward.Category.Industry)
          .address("124 Main St, Old Town, CA")
          .contactName("Marvin Acme")
          .country(CountryAlpha3CodeType.USA)
          .description("A Company that Makes Everything.")
          .email("tweety@acme.org")
          .unit("ink")
          .subunit("disappearing")
          .phone("867-5309")
          .website("https://acme.com")
          .build();
    }

    public static Steward nmo() {
      return Steward.builder()
          .shortName("NMO")
          .fullName("NIEM Management Office")
          .category(Steward.Category.Nonprofit)
          .country(CountryAlpha3CodeType.USA)
          .email("info@niemopen.org")
          .website("https://niemopen.org")
          .build();
    }

  }

  public class Models {

    public static String niemKey = "niem";
    public static String crashKey = "crash-driver";

    public static Model niem(Steward steward) {
      return Model.builder()
          .category(Model.Category.reference)
          .objective(Model.Objective.implementation)
          .description("")
          .fullName("National Information Exchange Model")
          .repo("https://github.com/niem/niem-releases.git")
          .shortName("NIEM")
          .steward(steward)
          .build();
    }

    public static Model crash(Steward steward) {
      return Model.builder()
          .category(Model.Category.message)
          .objective(Model.Objective.example)
          .description("This example IEPD is designed for the training program. It exercises most of the features in the NDR.")
          .fullName("Crash Driver Report IEPD")
          .repo("https://github.com/niem/niem-training.git")
          .shortName("Crash Driver")
          .steward(steward)
          .build();
    }

  }

  public class Versions {

    public static Version major(Model model, String versionNumber, Version niemVersion) {
      Version version = Versions.major(model, versionNumber);
      version.setNiemVersion(niemVersion);
      return version;
    }

    public static Version major(Model model, String versionNumber) {
      return Version.builder()
          .category(Version.Category.major)
          .model(model)
          .versionNumber(versionNumber)
          .build();
    }

    public static Version minor(Model model, String versionNumber, Version niemVersion) {
      Version version = Versions.minor(model, versionNumber);
      version.setNiemVersion(niemVersion);
      return version;
    }

    public static Version minor(Model model, String versionNumber) {
      return Version.builder()
          .category(Version.Category.minor)
          .model(model)
          .versionNumber(versionNumber)
          .build();
    }

  }

  public class Namespaces {

    public static Namespace core(Version version) {
      return Namespace.builder()
          .version(version)
          .prefix("nc")
          .category(Namespace.Category.core)
          .build();
    }

    public static Namespace domain(Version version, String prefix) {
      return Namespace.builder()
          .version(version)
          .prefix(prefix)
          .category(Namespace.Category.domain)
          .build();
    }

    public static Namespace xs(Version version) {
      return Namespace.builder()
          .version(version)
          .prefix("xs")
          .category(Namespace.Category.built_in)
          .build();
    }

  }

  public class Properties {

    public static Property person(Namespace namespace) {
      return Property.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("Person")
          .definition("A human being.")
          .build();
    }

    public static Property personFullName(Namespace namespace) {
      return Property.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("PersonFullName")
          .definition("A full name of a person.")
          .build();
    }

    public static Property sourceText(Namespace namespace) {
      return Property.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("sourceText")
          .definition("A source which provided the information.")
          .category(Property.Category.attribute)
          .build();
    }

  }

  public class Types {

    public static Type personType(Namespace namespace) {
      return Type.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("PersonType")
          .definition("A data type for a human being.")
          .category(Type.Category.complex_object)
          .build();
    }

    public static Type objectType(Namespace namespace) {
      return Type.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("ObjectType")
          .definition("A data type for an object.")
          .category(Type.Category.complex_object)
          .build();
    }

    public static Type textType(Namespace namespace) {
      return Type.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("TextType")
          .definition("A data type for a string.")
          .category(Type.Category.complex_value)
          .build();
    }

    public static Type codeSimpleType(Namespace namespace) {
      return Type.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("CodeSimpleType")
          .definition("A data type for a code set.")
          .category(Type.Category.simple_value)
          .build();
    }

    public static Type xsStringType(Namespace namespace) {
      return Type.builder()
          .namespace(namespace)
          .prefix(namespace.getPrefix())
          .name("string")
          .definition("A data type for a string.")
          .category(Type.Category.simple_value)
          .build();
    }

  }

}
