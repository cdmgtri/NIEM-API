package gov.niem.tools.api.db.namespace;

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
import gov.niem.tools.api.db.version.Version;

@ActiveProfiles("test")
@SpringBootTest
public class NamespaceTest extends EntityTest<Namespace> {

  Steward nmo;
  Model niem;

  Version niem_v1;
  Version niem_v2;

  Namespace nc_v1;
  Namespace nc_v2;

  Namespace em_v1;

  @Override
  protected NamespaceService service() {
    return this.hub.namespaces;
  }

  @BeforeEach
  @Override
  public void init() throws Exception {
    // Add dependencies to the database
    nmo = hub.stewards.add(TestData.Stewards.nmo());
    niem = hub.models.add(TestData.Models.niem(nmo));
    niem_v1 = hub.versions.add(TestData.Versions.major(niem, "1.0"));
    niem_v2 = hub.versions.add(TestData.Versions.minor(niem, "1.1"));

    niem_v1.setNiemVersion(niem_v1);
    niem_v2.setNiemVersion(niem_v2);

    // Initialize data for namespaces
    nc_v1 = TestData.Namespaces.core(niem_v1);
    nc_v2 = TestData.Namespaces.core(niem_v2);
    em_v1 = TestData.Namespaces.domain(niem_v1, "em");
  }

  @Override
  protected void loadObjects() throws Exception {
    nc_v1 = hub.namespaces.add(nc_v1);
    nc_v2 = hub.namespaces.add(nc_v2);
    em_v1 = hub.namespaces.add(em_v1);
  }

  @Override
  @Test
  public void databaseAddTest() {
    this.add(nc_v1);
    this.add(nc_v2);
    this.add(em_v1);
  }

  @Override
  @Test
  public void databaseAddDuplicateTest() {
    this.addDuplicate(nc_v1);
  }

  @Override
  @Test
  public void databaseEditTest() throws Exception {
    this.edit(nc_v1, "definition", "Core");
  }

  @Override
  @Test
  public void databaseDeleteTest() throws Exception {
    this.deleteNonCascading(hub.namespaces, nc_v1, nc_v2, hub.versions);
  }

  @Override
  @Test
  public void databaseFindOneTest() throws Exception {
    this.loadObjects();
    Namespace result = hub.namespaces.findOne(nc_v2.getStewardKey(), nc_v2.getModelKey(), nc_v2.getVersionNumber(), nc_v2.getPrefix());
    assertEquals(nc_v2.getVersionNumber(), result.getVersionNumber());
    assertEquals(nc_v2.getPrefix(), result.getPrefix());
  }

  @Test
  public void databaseFindAllTest() throws Exception {
    this.loadObjects();
    List<Namespace> results = service().findAll();
    assertEquals(3, results.size());
  }

  @Override
  @Test
  public void objectLabelTest() throws Exception {
    this.loadObjects();
    Namespace result = service().findOne(nc_v1);
    assertEquals("nmo/niem/1.0/nc", result.getFullIdentifier());
  }

  @Override
  @Test
  public void objectSerializationTest() throws Exception {
    serializeObject(nc_v1);
    serializeObject(nc_v2);
    serializeObject(em_v1);
  }

}
