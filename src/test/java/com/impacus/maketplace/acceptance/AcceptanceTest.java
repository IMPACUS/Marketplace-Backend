package com.impacus.maketplace.acceptance;

import com.impacus.maketplace.utils.DataLoader;
import com.impacus.maketplace.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.impacus.maketplace.acceptance.UserSteps.베어러_인증_로그인_요청;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {
	public static final String EMAIL = "admin@email.com";
	public static final String PASSWORD = "password1234!@#$";

	@Autowired
	private DatabaseCleanup databaseCleanup;
	@Autowired
	private DataLoader dataLoader;

	String 관리자;

	@BeforeEach
	public void setUp() {
		databaseCleanup.execute();
		dataLoader.loadData();

		관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
	}
}
