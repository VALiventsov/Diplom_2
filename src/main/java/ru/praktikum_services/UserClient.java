package ru.praktikum_services;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String USER_PATH  = "api/auth/register";
    private static final String USER_LOGIN = "api/auth/login";
    private static final String AUTH_USER = "api/auth/user";

    //Создание пользователя
    @Step("Create new user {user}")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body (user)
                .when()
                .post(USER_PATH)
                .then();
    }

    //Авторизация пользователем
    @Step ("User {user} login ")
    public ValidatableResponse login(UserCredentials credentials ) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    //Удаление пользователя
    @Step ("Delete user {user}")
    public ValidatableResponse delete (String accessToken) {
        return given()
                .spec(getBaseSpec())
                .body(accessToken)
                .when()
                .delete(AUTH_USER)
                .then();
    }

    //Обновление данных авторизованного пользователя
    @Step ("Update authorized  user {user}")
    public ValidatableResponse updateUserWithAuth(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .patch(AUTH_USER)
                .then();
    }

    //Обновление данных не авторизованного пользователя
    @Step ("Update not authorized  user {user}")
    public ValidatableResponse updateUserWithoutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .patch(AUTH_USER)
                .then();
    }

}
