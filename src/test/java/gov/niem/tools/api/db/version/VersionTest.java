package gov.niem.tools.api.db.version;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import gov.niem.tools.api.db.EntityTest;
import gov.niem.tools.api.db.TestData;
import gov.niem.tools.api.db.model.Model;
import gov.niem.tools.api.db.steward.Steward;

@ActiveProfiles("test")
@SpringBootTest
public class VersionTest extends EntityTest<Version> {

  Steward nmo;
  Steward acme;

  Model niem;
  Model crash_nmo;
  Model crash_acme;

  Version niem_v1;
  Version niem_v2;

  Version crash_acme_v1;
  Version crash_acme_v2;

  @Override
  protected VersionService service() {
    return this.hub.versions;
  }

  @BeforeEach
  @Override
  public void init() throws Exception {
    // Add stewards to the database
    nmo = hub.stewards.add(TestData.Stewards.nmo());
    acme = hub.stewards.add(TestData.Stewards.acme());

    // Add models to the database
    niem = hub.models.add(TestData.Models.niem(nmo));
    crash_nmo = hub.models.add(TestData.Models.crash(nmo));
    crash_acme = hub.models.add(TestData.Models.crash(acme));

    // Initialize data for NIEM model versions
    niem_v1 = TestData.Versions.major(niem, "1.0");
    niem_v2 = TestData.Versions.minor(niem, "1.1");

    niem_v1.setNiemVersion(niem_v1);
    niem_v2.setNiemVersion(niem_v2);

    // Initialize data for ACME crash IEPD versions
    crash_acme_v1 = TestData.Versions.major(crash_acme, "1.0", niem_v1);
    crash_acme_v2 = TestData.Versions.minor(crash_acme, "1.1", niem_v2);
  }

  @Override
  protected void loadObjects() throws Exception {
    niem_v1 = hub.versions.add(niem_v1);
    niem_v2 = hub.versions.add(niem_v2);

    crash_acme_v1 = hub.versions.add(crash_acme_v1);
    crash_acme_v2 = hub.versions.add(crash_acme_v2);
  }

  @Override
  @Test
  public void databaseAddTest() {
    this.add(niem_v1);
    this.add(niem_v2);
    niem_v1.setNiemVersion(niem_v1);
    this.add(crash_acme_v1);
    this.add(crash_acme_v2);
  }

  @Override
  @Test
  public void databaseAddDuplicateTest() {
    this.addDuplicate(crash_acme_v1);
  }

  @Override
  @Test
  public void databaseEditTest() throws Exception {
    crash_acme_v1.setDraft("1");
    this.edit(crash_acme_v1, "draft", "2");
  }

  @Override
  @Test
  public void databaseDeleteTest() throws Exception {
    this.deleteNonCascading(hub.versions, crash_acme_v1, crash_acme_v2, hub.models);
  }

  @Override
  @Test
  public void databaseFindOneTest() throws Exception {
    this.loadObjects();
    Version result = hub.versions.findOne("acme-co", "crash-driver", "1.1");
    assertEquals("ACME Co", result.getSteward().getShortName());
    assertEquals("1.1", result.getVersionNumber());
    assertEquals(Version.Category.minor, result.getCategory());
  }

  @Test
  public void databaseFindAllTest() throws Exception {
    this.loadObjects();
    List<Version> results = hub.versions.findAll();
    assertEquals(4, results.size());
  }

  @Override
  @Test
  public void objectLabelTest() throws Exception {
    this.loadObjects();
    Version result = hub.versions.findOne(crash_acme_v1);
    assertEquals("acme-co/crash-driver/1.0", result.getFullIdentifier());
  }

  @Override
  @Test
  public void objectSerializationTest() throws Exception {
    serializeObject(niem_v1);
    serializeObject(niem_v2);
    serializeObject(crash_acme_v1);
    serializeObject(crash_acme_v2);
  }

}
