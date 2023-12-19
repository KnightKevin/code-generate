package com.codegenerator.app.controller;

import com.codegenerator.app.entity.a.Role;
import com.codegenerator.app.entity.a.User;
import com.codegenerator.app.entity.a.Vdc;
import com.codegenerator.app.mapper.a.RoleMapper;
import com.codegenerator.app.mapper.a.UserMapper;
import com.codegenerator.app.mapper.a.VdcMapper;
import com.codegenerator.app.service.MigrateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mas")
public class MasController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MigrateService migrateService;

    @Autowired
    private VdcMapper vdcMapper;

    @GetMapping("/a")
    public Object a() {
        return userMapper.selectList(null);
    }

    @GetMapping("/migrateUser")
    public Object migrateUser() {

        // 查出old
        List<User> oldList = userMapper.selectList(null);

        int n = migrateService.migrateUsers(oldList);

        return n;
    }

    @GetMapping("/migrateRole")
    public Object migrateRole() {

        // 查出old
        List<Role> oldList = roleMapper.selectList(null);


        int n = migrateService.migrateRoles(oldList);



        return n;
    }

    @GetMapping("/migrateVdc")
    public Object migrateVdc() {

        // 查出old
        List<Vdc> oldList = vdcMapper.selectList(null);

        int n = migrateService.migrateVdcList(oldList);

        return n;
    }
}
