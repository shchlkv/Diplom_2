/*
Изменение данных пользователя:
     +с авторизацией,
      +без авторизации,
      +Для обеих ситуаций нужно проверить, что любое поле можно изменить.
      +Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
*/
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import static org.hamcrest.Matchers.*;


public class ChangeUserDataTest {

User user;
String accessToken;


    @Before
    public void setUp() {
        RestAssured.baseURI = Uri.URI;
        String name = TestData.name;
        String email = TestData.email;
        String password = TestData.password;
        user = new User(email, password, name);
        Response response = Steps.createUser(user);
        accessToken = Steps.getAccessToken(response);

    }

    @Test
    @DisplayName("changeUserNameTest")
    @Description("Тест: Изменение имени пользователя после авторизации")
    public void changeUserNameTest() {

        String newName = "Новый_" + TestData.name;
        user.setName(newName);

        Response responseUpdate = Steps.updateUserData(user, accessToken);
        responseUpdate.then()
                .assertThat()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("user.name", equalTo(newName));

    }

    @Test
    @DisplayName("changeUserEmailTest")
    @Description("Тест: Изменение email пользователя после авторизации")
    public void changeUserEmailTest() {

        String newEmail = "new"+TestData.email;
        user.setEmail(newEmail);

        Response responseUpdate = Steps.updateUserData(user, accessToken);
        responseUpdate.then()
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(newEmail));


    }

    @Test
    @DisplayName("changeUserAllDataTest")
    @Description("Тест: Изменение всех данных пользователя после авторизации")
    public void changeUserAllDataTest() {

        String newName = "Новый_" + TestData.name;
        String newEmail = "new"+TestData.email;
        String newPassword = TestData.password;

        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassword(newPassword);

        Response responseUpdate = Steps.updateUserData(user, accessToken);
        responseUpdate.then()
                .assertThat()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("user.name", equalTo(newName))
                    .body("user.email", equalTo(newEmail));
       // System.out.println(responseUpdate.getBody().asString());
    }

    @Test
    @DisplayName("changeUserDataWithoutAuthorizationTest")
    @Description("Тест: Изменение данных пользователя без авторизации")
    public void changeUserDataWithoutAuthorizationTest() {

        String newName = "Новый_" + TestData.name;
        String newEmail = "new"+TestData.email;
        String newPassword = TestData.password;

        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassword(newPassword);

        Response responseUpdate = Steps.updateUserData(user, "");
        responseUpdate.then()
                .assertThat()
                    .statusCode(401)
                    .body("success", is(false))
                    .body("message", equalTo("You should be authorised"));
    }
    @After

    public void delUser() {
        if (accessToken != null) {
            Response responseDelData = Steps.deleteUser(accessToken);
            responseDelData.then()
                    .assertThat()
                        .statusCode(202)
                        .body("success", is(true))
                        .body("message", equalTo("User successfully removed"));
        }
    }
}
