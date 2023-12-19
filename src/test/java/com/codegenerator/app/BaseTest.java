package com.codegenerator.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codegenerator.app.entity.a.UserRole;
import com.codegenerator.app.entity.b.UserNew;
import com.codegenerator.app.mapper.a.RoleMapper;
import com.codegenerator.app.mapper.a.UserRoleMapper;
import com.codegenerator.app.mapper.b.UserNewMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class BaseTest {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RoleMapper roleMapper;

    @Test
    public void update() {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();

        List<UserRole> list = userRoleMapper.listPlatform();

        Assertions.assertEquals(true, true);
    }
}
