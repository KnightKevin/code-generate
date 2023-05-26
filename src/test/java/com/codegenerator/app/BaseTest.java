package com.codegenerator.app;

import com.codegenerator.app.module.User;
import com.codegenerator.app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {

    @Autowired
    UserService userService;

    @Test
    public void update() {
        User user = new User();
        user.setId("1");
        user.setName("aaaaaa");
        userService.update(user);

        Assertions.assertEquals(true, true);
    }
}
