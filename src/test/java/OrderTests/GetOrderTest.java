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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderTest {

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
    @DisplayName("Получение заказа авторизованного пользователя")
    public void getOrderWithAuth() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        int statusCodeLogin = loginResponse
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));
        accessToken = loginResponse.extract().path("accessToken");

        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrderWithAuth(order, accessToken);
        int statusCodeOrder = orderResponse
                .extract()
                .statusCode();
        assertThat(statusCodeOrder, equalTo(SC_OK));

        ValidatableResponse getResponseWithAuth = orderClient.getOrderWithAuth(accessToken);
        int statusCodeGetOrder = getResponseWithAuth
                .extract()
                .statusCode();
        assertThat(statusCodeGetOrder, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    public void getOrderWithoutAuth() {
        order = new Order(ingredients);
        ValidatableResponse orderResponseWithAuth = orderClient.createOrderWithoutAuth(order);
        int statusCodeGetOrderWithAuth = orderResponseWithAuth
                .extract()
                .statusCode();
        assertThat(statusCodeGetOrderWithAuth, equalTo(SC_OK));

        ValidatableResponse orderResponseWithoutAuth = orderClient.getOrderWithoutAuth();
        int statusCodeGetOrderWithoutAuth = orderResponseWithoutAuth
                .extract()
                .statusCode();
        System.out.println(statusCodeGetOrderWithoutAuth);
        assertThat(statusCodeGetOrderWithoutAuth, equalTo(SC_UNAUTHORIZED));
    }
}
