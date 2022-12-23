package ru.praktikum_services;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client{

    public static final String ORDER_PATH = "/api/orders/";

    //Создание заказа авторизованного пользователя
    @Step("Create order authorized user")
    public ValidatableResponse createOrderWithAuth(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .post(ORDER_PATH)
                .then();
    }

    //Создание заказа неавторизованного пользователя
    @Step("Create order not authorized user")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .post(ORDER_PATH)
                .then();
    }

   //Получение заказа авторизованного пользователя
    @Step("Get order authorized user")
    public ValidatableResponse getOrderWithAuth(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .get(ORDER_PATH)
                .then();
    }

    //получение заказа неавторизованного пользователя
    @Step("Get order not authorized user")
    public ValidatableResponse getOrderWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH)
                .then();
    }

}
