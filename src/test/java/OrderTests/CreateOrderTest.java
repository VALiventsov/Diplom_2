package OrderTests;

import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    List<String> ingredients = Arrays.asList(
            "61c0c5a71d1f82001bdaaa75",
            "61c0c5a71d1f82001bdaaa71",
            "61c0c5a71d1f82001bdaaa7a",
            "61c0c5a71d1f82001bdaaa70",
            "61c0c5a71d1f82001bdaaa73",
            "61c0c5a71d1f82001bdaaa6c");
    private String accessToken;
    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private Order order;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        try {
            userClient.delete(accessToken);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Создание заказа после создания пользователя и его авторизации")
    public void createOrderWithAuth() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int statusCodeLogin = loginResponse
                .extract()
                .statusCode();
        accessToken = loginResponse.extract().path("accessToken");

        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrderWithAuth(order, accessToken);
        int statusCodeOrder = orderResponse
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));
        assertThat(statusCodeOrder, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrderWithoutAuth(order);
        int statusCodeOrder = orderResponse
                .extract()
                .statusCode();
        assertThat(statusCodeOrder, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание заказа после создания пользователя и его авторизации, но без ингредиентов")
    public void createOrderWithoutIngredients() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int statusCodeLogin = loginResponse
                .extract()
                .statusCode();
        accessToken = loginResponse.extract().path("accessToken");

        order = new Order(null);
        ValidatableResponse orderResponse = orderClient.createOrderWithAuth(order, accessToken);
        int statusCodeOrder = orderResponse
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));
        assertThat(statusCodeOrder, equalTo(SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("Создание заказа после создания пользователя и его авторизации, с неверным хешем ингредиентов")
    public void createOrderWithWrongHashIngredient() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int statusCodeLogin = loginResponse
                .extract()
                .statusCode();
        accessToken = loginResponse.extract().path("accessToken");

        order = new Order(List.of("60d3463f7034a000269f45e7"));
        ValidatableResponse orderResponse = orderClient.createOrderWithAuth(order, accessToken);
        int statusCodeOrder = orderResponse
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));
        assertThat(statusCodeOrder, equalTo(SC_BAD_REQUEST));
    }
}
