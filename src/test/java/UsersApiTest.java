import org.framework.api.assertions.ApiAssertions;
import org.framework.api.core.ApiResponse;
import org.framework.api.model.User;
import org.framework.api.service.UserService;
import org.framework.utils.JacksonProvider;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class UsersApiTest {

    private static final String API_BASE_URL_PROPERTY = "api.base.url";

    private final UserService userService = new UserService();
    private HttpServer mockServer;
    private String originalBaseUrl;

    @BeforeClass
    public void startMockServer() throws IOException {
        originalBaseUrl = System.getProperty(API_BASE_URL_PROPERTY);

        mockServer = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        mockServer.createContext("/users", this::handleCreateUser);
        mockServer.start();

        int port = mockServer.getAddress().getPort();
        System.setProperty(API_BASE_URL_PROPERTY, "http://localhost:" + port);
    }

    @AfterClass(alwaysRun = true)
    public void stopMockServer() {
        if (mockServer != null) {
            mockServer.stop(0);
        }

        if (originalBaseUrl == null) {
            System.clearProperty(API_BASE_URL_PROPERTY);
        } else {
            System.setProperty(API_BASE_URL_PROPERTY, originalBaseUrl);
        }
    }

    @Test
    public void createUser() {
        User request = new User(
                "test_user2",
                "test_user2@gmail.com",
                "1234567891"
        );

        ApiResponse response = ApiAssertions.assertStatusCodeIn(
                userService.createUser(request),
                200,
                201
        );

        ApiAssertions.assertJsonField(response, "name", request.getName());
        ApiAssertions.assertJsonField(response, "email", request.getEmail());
        ApiAssertions.assertJsonField(response, "phone", request.getPhone());
    }

    private void handleCreateUser(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        User request = JacksonProvider.mapper()
                .readValue(exchange.getRequestBody(), User.class);

        sendJson(
                exchange,
                201,
                JacksonProvider.mapper().writeValueAsString(request)
        );
    }

    private void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] responseBody = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBody.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        }
    }
}
