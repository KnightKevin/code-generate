package com.codegenerator.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codegenerator.app.entity.a.Role;
import com.codegenerator.app.entity.a.RolePermission;
import com.codegenerator.app.entity.a.RolePermissionMas;
import com.codegenerator.app.entity.a.User;
import com.codegenerator.app.entity.a.UserRole;
import com.codegenerator.app.entity.a.Vdc;
import com.codegenerator.app.entity.a.VdcRoleRelation;
import com.codegenerator.app.entity.a.VdcUserRelation;
import com.codegenerator.app.entity.b.RoleNew;
import com.codegenerator.app.entity.b.RolePermissionNew;
import com.codegenerator.app.entity.b.UserNew;
import com.codegenerator.app.entity.b.UserRoleNew;
import com.codegenerator.app.entity.b.VdcNew;
import com.codegenerator.app.mapper.a.RoleMapper;
import com.codegenerator.app.mapper.a.RolePermissionMapper;
import com.codegenerator.app.mapper.a.RolePermissionMasMapper;
import com.codegenerator.app.mapper.a.UserRoleMapper;
import com.codegenerator.app.mapper.a.VdcRoleRelationMapper;
import com.codegenerator.app.mapper.a.VdcUserRelationMapper;
import com.codegenerator.app.mapper.b.RoleNewMapper;
import com.codegenerator.app.mapper.b.RolePermissionNewMapper;
import com.codegenerator.app.mapper.b.UserNewMapper;
import com.codegenerator.app.mapper.b.UserRoleNewMapper;
import com.codegenerator.app.mapper.b.VdcNewMapper;
import com.codegenerator.app.service.MigrateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionNewMapper rolePermissionNewMapper;


    @Autowired
    private RolePermissionMasMapper rolePermissionMasMapper;

    @Override
    public int migrateUsers(List<User> list) {
        printLog("user 开始迁移");

        userNewMapper.delete(null);

        int n=0;
        for (User i : list) {
            UserNew userNew = new UserNew();
            BeanUtils.copyProperties(i, userNew);

            userNew.setPassword(genPassword("password", i.getUsername()));
            userNew.setMustModifyPwd(true);

            userNewMapper.insert(userNew);
            n++;
        }
        printLog("user 结束迁移");

        return n;
    }

    @Override
    public int migrateRoles(List<Role> list) {
        printLog("role 开始迁移");

        roleNewMapper.delete(null);

        int n=0;
        for (Role i : list) {
            RoleNew roleNew = new RoleNew();
            BeanUtils.copyProperties(i, roleNew);

            if ("member".equals(i.getType())) {
                roleNew.setType("vdc");
            }

//            if ("vdc_admin".equals(i.getRoleKey()) || "vdc_user".equals(i.getRoleKey())) {
////                roleNew.setRoleKey("vdc_user");
//                roleNew.setName("组织用户");
//            }

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
        printLog("role 结束迁移");

        return n;
    }

    @Override
    public int migrateVdcList(List<Vdc> list) {
        printLog("vdc 开始迁移");

        vdcNewMapper.delete(null);

        for (Vdc i : list) {
            VdcNew vdcNew = new VdcNew();
            BeanUtils.copyProperties(i, vdcNew);

            vdcNew.setRegionId("");

            vdcNewMapper.insert(vdcNew);
        }

        VdcNew platformVdc = new VdcNew();
        platformVdc.setUuid(platformVdcId);
        platformVdc.setName("平台");
        platformVdc.setDescription("");
        platformVdc.setParentUuid("");
        platformVdc.setTenantUuid(platformVdcId);
        platformVdc.setLevel(1);
        platformVdc.setNumber("");
        platformVdc.setCreateBy(adminId);
        platformVdc.setCreateByName("admin");
        platformVdc.setCreateDate(new Date());
        platformVdc.setUpdateDate(new Date());
        platformVdc.setRegionId("");
        vdcNewMapper.insert(platformVdc);


        printLog("vdc 结束迁移");

        return 0;
    }

    @Override
    public Map<String, List<Role>> migrateRolePermission() {
        printLog("role permission 开始迁移");

        rolePermissionNewMapper.delete(null);

        // 查出每个角色的permission
        List<Role> roleList = roleMapper.selectList(null);


        Map<String, List<Role>> map = new HashMap<>();
        for (Role role : roleList) {
            if (role.getCreateByName().equals("系统")) {
                log.info("role is system, skip!");
                continue;
            }

            QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", role.getUuid());
            List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(queryWrapper);
            rolePermissionList.sort(Comparator.comparing(RolePermission::getPermissionId));

            List<String> permissions = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

            String hash = DigestUtils.md5DigestAsHex(String.join(",", permissions).getBytes());


            // 查出新环境须要的permission信息
            List<RolePermissionMas> masPermissions = rolePermissionMasMapper.listPermissionByRoleName(hash);

            if (CollectionUtils.isEmpty(masPermissions)) {
                throw new RuntimeException(String.format("role can't find permissions. %s", role.getUuid()));
            }

            for (RolePermissionMas i : masPermissions) {
                RolePermissionNew rolePermissionNew = new RolePermissionNew();
                rolePermissionNew.setUuid(UUID.randomUUID().toString().replace("-", ""));
                rolePermissionNew.setRoleId(role.getUuid());
                rolePermissionNew.setPermissionKey(i.getPermissionKey());
                rolePermissionNew.setCreateDate(new Date());

                rolePermissionNewMapper.insert(rolePermissionNew);
            }


            if (!map.containsKey(hash)) {
                map.put(hash, new ArrayList<>());
            }

            map.get(hash).add(role);
        }

        printLog("role permission 结束迁移");

        return map;

    }

    private UserRoleNew buildUserRoleNew() {
        UserRoleNew userRoleNew = new UserRoleNew();
        userRoleNew.setUuid(UUID.randomUUID().toString().replace("-",""));
        userRoleNew.setCreateDate(new Date());

        return userRoleNew;
    }


    private void printLog(String msg) {
        log.info("================ {} ===============", msg);
    }

    public static String genPassword(String password, String salt) {
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }


}
