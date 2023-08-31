package com.codegenerator.app;

import com.codegenerator.app.entity.User;
import com.codegenerator.app.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert() {
        User user = new User();
        user.setId("1");
        user.setName("dasdf");

        userMapper.insert(user);
    }
}
