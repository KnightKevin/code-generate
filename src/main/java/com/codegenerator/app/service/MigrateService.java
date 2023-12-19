package com.codegenerator.app.service;

import com.codegenerator.app.entity.a.Role;
import com.codegenerator.app.entity.a.User;
import com.codegenerator.app.entity.a.Vdc;

import java.util.List;
import java.util.Map;

public interface MigrateService {

    String platformVdcId = "7c54bc609ffa49caa47f8d22f4d07672";
    String adminId = "37a2ffa6d1664987bca5c814801430bb";

    int migrateUsers(List<User> list);

    int migrateRoles(List<Role> list);

    int migrateVdcList(List<Vdc> list);


    Map<String, List<Role>> migrateRolePermission();

}
