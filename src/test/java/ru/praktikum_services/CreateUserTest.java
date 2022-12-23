package ru.praktikum_services;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private String accessToken;


    //берем юзера
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
    }

    //удаляем юзера
    @After
    public void tearDown() {
        try {
            userClient.delete(accessToken);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //тест на создание юзера
    @Test
    @DisplayName("User can be created Successfully")
    @Description("User return 201 Created when new courier created")
    public void userCanBeCreatedTest() {
        ValidatableResponse response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);
        //логин юзера
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int loginStatusCode = loginResponse.extract().statusCode();
        //проверяем, что юзер залогинился
        assertEquals("User is not login", SC_OK, loginStatusCode);
        accessToken = loginResponse.extract().path("accessToken");
        //проверяем, что accessToken что не равен нулю
        assertNotNull("Access Token is null", accessToken);
    }

}
