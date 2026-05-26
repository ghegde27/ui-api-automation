import org.framework.api.assertions.ApiAssertions;
import org.framework.api.core.ApiResponse;
import org.framework.api.model.User;
import org.framework.api.service.UserService;
import org.testng.annotations.Test;

public class UsersApiTest {

    private final UserService userService = new UserService();

    @Test
    public void createUser() {
        User request = new User(
                "test_user2",
                "test_user2@gmail.com",
                "9620969967"
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
}
