package com.codegenerator.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codegenerator.app.entity.a.Role;
import com.codegenerator.app.entity.a.User;
import com.codegenerator.app.entity.a.UserRole;
import com.codegenerator.app.entity.a.Vdc;
import com.codegenerator.app.entity.a.VdcRoleRelation;
import com.codegenerator.app.entity.a.VdcUserRelation;
import com.codegenerator.app.entity.b.RoleNew;
import com.codegenerator.app.entity.b.UserNew;
import com.codegenerator.app.entity.b.UserRoleNew;
import com.codegenerator.app.entity.b.VdcNew;
import com.codegenerator.app.mapper.a.UserRoleMapper;
import com.codegenerator.app.mapper.a.VdcRoleRelationMapper;
import com.codegenerator.app.mapper.a.VdcUserRelationMapper;
import com.codegenerator.app.mapper.b.RoleNewMapper;
import com.codegenerator.app.mapper.b.UserNewMapper;
import com.codegenerator.app.mapper.b.UserRoleNewMapper;
import com.codegenerator.app.mapper.b.VdcNewMapper;
import com.codegenerator.app.service.MigrateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MigrateServiceImpl implements MigrateService {

    @Autowired
    private UserNewMapper userNewMapper;

    @Autowired
    private RoleNewMapper roleNewMapper;

    @Autowired
    private VdcRoleRelationMapper vdcRoleRelationMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private VdcUserRelationMapper vdcUserRelationMapper;

    @Autowired
    private UserRoleNewMapper userRoleNewMapper;

    @Autowired
    private VdcNewMapper vdcNewMapper;

    @Override
    public int migrateUsers(List<User> list) {
        userNewMapper.delete(null);

        int n=0;
        for (User i : list) {
            UserNew userNew = new UserNew();
            BeanUtils.copyProperties(i, userNew);

            userNewMapper.insert(userNew);
            n++;
        }

        return n;
    }

    @Override
    public int migrateRoles(List<Role> list) {
        roleNewMapper.delete(null);

        int n=0;
        for (Role i : list) {
            RoleNew roleNew = new RoleNew();
            BeanUtils.copyProperties(i, roleNew);

            if ("member".equals(i.getType())) {
                roleNew.setType("vdc");
            }

            if ("vdc_admin".equals(i.getRoleKey()) || "vdc_user".equals(i.getRoleKey())) {
//                roleNew.setRoleKey("vdc_user");
                roleNew.setName("组织用户");
            }

            if ("admin".equals(i.getRoleKey())) {
                roleNew.setRoleKey("platform_user");
            }

            roleNewMapper.insert(roleNew);
            n++;
        }


        // 迁移user-role-vdc绑定关系
        List<VdcUserRelation> vdcUserList = vdcUserRelationMapper.selectList(null);

        List<UserRole> userRoleList = userRoleMapper.selectList(null);

        List<UserRoleNew> userRoleNewList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            UserRoleNew userRoleNew = buildUserRoleNew();
            userRoleNew.setUserId(userRole.getUserId());
            userRoleNew.setRoleId(userRole.getRoleId());

            for (VdcUserRelation vdcRoleRelation : vdcUserList) {
                if (vdcRoleRelation.getUserUuid().equals(userRole.getUserId())) {
                    userRoleNew.setVdcId(vdcRoleRelation.getVdcUuid());
                    userRoleNewList.add(userRoleNew);
                }
            }
        }


        // 查出所有平台管理员用户
        // 平台管理员也需要维护一个内置的user_role
        List<UserRole> platformUserRoleList = userRoleMapper.listPlatform();
        platformUserRoleList.forEach(i -> {
            UserRoleNew userRoleNew = buildUserRoleNew();
            userRoleNew.setUserId(i.getUserId());
            userRoleNew.setRoleId(i.getRoleId());
            userRoleNew.setVdcId(platformVdcId);
            userRoleNewList.add(userRoleNew);
        });

        userRoleNewMapper.delete(null);
        for (UserRoleNew i : userRoleNewList) {
            userRoleNewMapper.insert(i);
        }

        return n;
    }

    @Override
    public int migrateVdcList(List<Vdc> list) {

        vdcNewMapper.delete(null);

        for (Vdc i : list) {
            VdcNew vdcNew = new VdcNew();
            BeanUtils.copyProperties(i, vdcNew);

            vdcNew.setRegionId("");

            vdcNewMapper.insert(vdcNew);
        }

        return 0;
    }

    private UserRoleNew buildUserRoleNew() {
        UserRoleNew userRoleNew = new UserRoleNew();
        userRoleNew.setUuid(UUID.randomUUID().toString().replace("-",""));
        userRoleNew.setCreateDate(new Date());

        return userRoleNew;
    }




}
