import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.Login;
import pojo.Order;
import pojo.User;

import static io.restassured.RestAssured.given;


public class Steps {
    @Step("Создание/регистрация пользователя")
    public static Response createUser(User user) {
       return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(Api.AUTH_REGISTER);
    }

    @Step("Аутентификация пользователя")
    public static Response loginUser(Login login) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post(Api.LOGIN);

    }

  //готово
@Step("Получение данных о пользователе")
    public static Response getUserData(String accessToken) {
    return   given()
            .header("Authorization", accessToken)
            .when()
            .get(Api.AUTH_USER);

    }

    @Step("Удалить пользователя")
     public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Api.AUTH_USER);
    }

    @Step("Обновление данных о пользователе")
    public static Response updateUserData(User user, String accessToken) {
        return given()
                 .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .body(user)
                .patch(Api.AUTH_USER);
    }

    @Step("Получить токен доступа (accessToken)")
    public static String getAccessToken(Response response) {
        return response.jsonPath().get("accessToken");
    }

    @Step("Получение ингредиентов")
    public static Response getIngredients() {
       return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(Api.INGREDIENTS);

    }
    @Step("Создание заказа")
    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(Api.ORDER);

    }

    @Step("Получение заказа пользователя")
    public static Response getOrders(String accessToken) {
        return  given()
                .header("Content-type", "application/json")
                .header("authorization", accessToken)
                .get(Api.ORDER);
    }

}
