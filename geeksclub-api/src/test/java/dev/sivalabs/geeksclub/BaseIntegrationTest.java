package dev.sivalabs.geeksclub;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import dev.sivalabs.geeksclub.rest.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfig.class)
@AutoConfigureRestTestClient
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "Admin@1234";
    public static final String USER_EMAIL = "siva@gmail.com";
    public static final String USER_PASSWORD = "Siva@1234";

    @Autowired
    protected MockMvcTester mvc;

    @Autowired
    protected RestTestClient restTestClient;

    @Autowired
    protected JsonMapper jsonMapper;

    protected String getAdminAuthToken() {
        return getAuthToken(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    protected String getUserAuthToken() {
        return getAuthToken(USER_EMAIL, USER_PASSWORD);
    }

    protected String getAuthToken(String email, String password) {
        MvcTestResult loginResult = mvc.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"%s",
                            "password":"%s"
                        }
                        """.formatted(email, password))
                .exchange();

        LoginResponse loginResponse = loginResult
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .convertTo(LoginResponse.class)
                .actual();
        return loginResponse.accessToken();
    }
}
