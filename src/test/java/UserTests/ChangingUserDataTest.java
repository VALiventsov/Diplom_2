package UserTests;

import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChangingUserDataTest {
    private String accessToken;
    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
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
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changeUserDataWithAuthTest() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = userClient.login(userCredentials);
        accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse updateResponse = userClient.updateUserWithAuth(UserGenerator.getRandom(), accessToken);
        int statusCode = updateResponse
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserDataWithoutAuthTest() {
        ValidatableResponse updateResponse = userClient.updateUserWithoutAuth(UserGenerator.getRandom());
        int statusCode = updateResponse
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }
}