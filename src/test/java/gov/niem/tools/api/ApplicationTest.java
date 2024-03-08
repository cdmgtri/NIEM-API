package gov.niem.tools.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import gov.niem.tools.api.core.config.Config;

@SpringBootTest(properties = "spring.main.lazy-initialization=true", classes = {Config.class})
class ApplicationTest {

  /**
   * Checks that the app will be able to run.
   */
	@Test
	public void checkContextLoads() {
	}

}
