package com.codegenerator.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codegenerator.app.entity.a.Role;
import com.codegenerator.app.entity.a.User;
import com.codegenerator.app.entity.a.UserRole;
import com.codegenerator.app.entity.a.Vdc;
import com.codegenerator.app.entity.b.UserNew;
import com.codegenerator.app.mapper.a.RoleMapper;
import com.codegenerator.app.mapper.a.UserMapper;
import com.codegenerator.app.mapper.a.UserRoleMapper;
import com.codegenerator.app.mapper.a.VdcMapper;
import com.codegenerator.app.mapper.b.UserNewMapper;
import com.codegenerator.app.service.MigrateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class BaseTest {

    @Autowired
    MigrateService migrateService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    VdcMapper vdcMapper;

    @Test
    public void update() {
        List<User> oldList = userMapper.selectList(null);
        migrateService.migrateUsers(oldList);

        List<Role> oldRoleList = roleMapper.selectList(null);
        migrateService.migrateRoles(oldRoleList);



        List<Vdc> oldVdcList = vdcMapper.selectList(null);
        migrateService.migrateVdcList(oldVdcList);

        migrateService.migrateRolePermission();

        Assertions.assertEquals(true, true);
    }
}
