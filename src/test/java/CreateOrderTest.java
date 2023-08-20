/*
Создание заказа:
+с авторизацией,
+без авторизации,
+с ингредиентами,
+без ингредиентов,
+с неверным хешем ингредиентов.
*/
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.Order;
import static org.hamcrest.Matchers.*;


public class CreateOrderTest {
    User user;
    Order order;
    String accessToken;
    String[] ingredientsCorrect = new String[] {"61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa7a"};
    String[] ingredientsNotCorrect = new String[] {"61c0c5a71d1f82001b_aaa6f", "61c0c5a71d1f82001bd_aa70", "61c0c5a71d1f82001bdaaa7a"};
    String email= TestData.email;
    String password = TestData.password;
    String name = TestData.name;
    @Before
    public void setUp() {RestAssured.baseURI = Uri.URI;
    }

    @Test
    @DisplayName("createOrderTest")
    @Description("Тест: создание заказа с авторизацией")
    public void createOrderTest() {

          user = new User(email, password, name);
       Response response = Steps.createUser(user);
        accessToken = Steps.getAccessToken(response);

        order = new Order(ingredientsCorrect);
        Response responseOrder = Steps.createOrder(order);
            responseOrder.then()
                .assertThat()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("createOrderWithoutAuthorisationTest")
    @Description("Тест: создание заказа без авторизации")
    public void createOrderWithoutAuthorisationTest() {


        order = new Order(ingredientsCorrect);
        Response responseOrder = Steps.createOrder(order);
            responseOrder.then()
                .assertThat()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("order.number", notNullValue());
    }


    @Test
    @DisplayName("createOrderWithoutIngredientsTest")
    @Description("Тест: создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {


        order = new Order(null);
        Response responseOrder = Steps.createOrder(order);
            responseOrder.then()
                .assertThat()
                    .statusCode(400)
                    .body("success", is(false))
                    .body("message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("createOrderWithNotCorrectIngredientsTest")
    @Description("Тест: создание заказа некорректным значением хэша ингредиентов")
    public void createOrderWithNotCorrectIngredientsTest () {

        order = new Order(ingredientsNotCorrect);
        Response responseOrder = Steps.createOrder(order);
        responseOrder.then()
                .assertThat()
                .statusCode(500);
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
