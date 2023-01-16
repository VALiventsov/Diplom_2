package UserTests;

import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateUserTest {

    int statusCode;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
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
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        ValidatableResponse createResponse = userClient.create(user);
        statusCode = createResponse
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_OK));
        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createExistingUserTest() {
        ValidatableResponse createResponse1 = userClient.create(user);
        ValidatableResponse createResponse2 = userClient.create(user);
        statusCode = createResponse2
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
        accessToken = createResponse1
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля имя")
    public void createUserWithoutNameTest() {
        user.setName(null);
        statusCode = userClient.create(user)
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля почта")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);
        statusCode = userClient.create(user)
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля пароль")
    public void createUserWithoutPasswordTest() {
        user.setPassword(null);
        statusCode = userClient.create(user)
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }
}
