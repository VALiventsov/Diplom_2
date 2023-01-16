package client;

import endpoints.EndPoints;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    @Step("Создание заказа авторизованного пользователя")
    public ValidatableResponse createOrderWithAuth(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Создание заказа неавторизованного пользователя")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .log().all()
                .post(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Получение заказа авторизованного пользователя")
    public ValidatableResponse getOrderWithAuth(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("получение заказа неавторизованного пользователя")
    public ValidatableResponse getOrderWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(EndPoints.ORDER_PATH)
                .then()
                .log().all();
    }
}
