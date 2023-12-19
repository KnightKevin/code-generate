package com.codegenerator.app.service;

import com.codegenerator.app.entity.a.Role;
import com.codegenerator.app.entity.a.User;
import com.codegenerator.app.entity.a.Vdc;

import java.util.List;

public interface MigrateService {

    String platformVdcId = "7c54bc609ffa49caa47f8d22f4d07672";

    int migrateUsers(List<User> list);

    int migrateRoles(List<Role> list);

    int migrateVdcList(List<Vdc> list);

}
