/*
Создание пользователя:
+создать уникального пользователя;
+создать пользователя, который уже зарегистрирован;
+создать пользователя и не заполнить одно из обязательных полей.
+В тестах проверяется тело и код ответа.
+ Нужные тестовые данные создаются перед тестом и удаляются после того, как он выполнится.
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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class CreateUserTest {
    User user;
    String accessToken;
    String email= TestData.email;
    String password = TestData.password;
    String name = TestData.name;


    @Before
    public void setUp() {
        RestAssured.baseURI = Uri.URI;
    }


    @Test
    @DisplayName("createUserTest")
    @Description("Тест: Создание/регистрация нового пользователя с корректными данными")
    public void createUserTest() {

        user = new User(email, password, name);
        Response response = Steps.createUser(user);
        response.then().body(not(empty()))
                .assertThat().statusCode(200);

        accessToken = Steps.getAccessToken(response);

    }

    @Test
    @DisplayName("registrationRegisteredUserTest")
    @Description("Тест: Создание/регистрация зарегистрированного пользователя")
    public void registrationRegisteredUserTest() {

        user = new User(email, password, name);
       Response response = Steps.createUser(user);
       Response responseSameUser = Steps.createUser(user);

        responseSameUser.then()
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
        accessToken = Steps.getAccessToken(response);

    }
    @Test
    @DisplayName("createUserWithoutEmailTest")
    @Description("Тест: Создание/регистрация пользователя без обязательного поля")
    public void createUserWithoutEmailTest() {

        user = new User(null, password, name);
        Response response = Steps.createUser(user);
        response.then()
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));

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
