/*
Логин пользователя:
+логин под существующим пользователем,
+логин с неверным логином и паролем.
 */
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.Login;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;


public class LoginTest {
   User user;
    String accessToken;
    Login loginData;
   String email= TestData.email;
    String password = TestData.password;
    String name = TestData.name;
    @Before
    public void setUp() {

        RestAssured.baseURI = Uri.URI;
        user = new User(email, password, name);
        Steps.createUser(user);
    }

    @Test
    @DisplayName("loginUserTest")
    @Description("Тест: аутентификация пользователя с корректными данными")
    public void loginUserTest() {

        loginData = new Login(email, password);
        Response responseLogin = Steps.loginUser(loginData);
        responseLogin.then().body(not(empty()))
                .assertThat().statusCode(200)
                .body("success", is(true));
        accessToken = Steps.getAccessToken(responseLogin);

    }

    @Test
    @DisplayName("loginUserIncorrectEmailTest")
    @Description("Тест: аутентификация пользователя с некорректными данными")
    public void loginUserIncorrectEmailTest() {

        loginData = new Login(null, password);
        Response responseLogin = Steps.loginUser(loginData);
        responseLogin.then()
                .assertThat()
                    .statusCode(401)
                    .body("success", is(false))
                    .body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("loginUserIncorrectPasswordTest")
    @Description("Тест: аутентификация пользователя с некорректным паролем")
    public void loginUserIncorrectPasswordTest() {

        String password = RandomStringUtils.randomAlphanumeric(12); //несуществующий пароль

        loginData = new Login(email, password);
        Response responseLogin = Steps.loginUser(loginData);
        responseLogin.then()
                .assertThat()
                    .statusCode(401)
                    .body("success", is(false))
                    .body("message", equalTo("email or password are incorrect"));

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