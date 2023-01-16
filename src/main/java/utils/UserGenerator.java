package utils;

import models.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getRandom() {
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        return new User(name, email, password);
    }
}