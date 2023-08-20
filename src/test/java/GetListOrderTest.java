/*Получение заказов конкретного пользователя:
        авторизованный пользователь,
        неавторизованный пользователь.*/


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;

import static org.hamcrest.Matchers.*;

public class GetListOrderTest {
    User user;
    Order order;
    String accessToken;
    String[] ingredients = new String[] {"61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa7a"};

    String email= TestData.email;
    String password = TestData.password;
    String name = TestData.name;
    @Before
    public void setUp() {
        RestAssured.baseURI = Uri.URI;
    }

    @Test
    @DisplayName("getOrderListTest")
    @Description("Тест: получение списка заказов авторизованного пользователя")
    public void getOrderListTest() {

        user = new User(email, password, name);
        Response response = Steps.createUser(user);
        accessToken = Steps.getAccessToken(response);
       order = new Order(ingredients);

        Steps.createOrder(order);
        Response responseListOrder =  Steps.getOrders(accessToken);
        responseListOrder.then()
                .assertThat()
                    .statusCode(200)
                    .body("success", is(true));
    }

    @Test
    @DisplayName("getOrderListWithoutAuthorisationTest")
    @Description("Тест: получение списка заказов неавторизованного пользователя")
    public void getOrderListWithoutAuthorisationTest() {

        order = new Order(ingredients);
        Steps.createOrder(order);
        Response responseListOrder =  Steps.getOrders("accessToken");
        responseListOrder.then()
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
