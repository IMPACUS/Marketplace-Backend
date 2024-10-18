package com.impacus.maketplace;

import com.impacus.maketplace.config.TestFirebaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(TestFirebaseConfig.class)
class MaketplaceApplicationTests {

	@Test
	void contextLoads() {
	}

}
