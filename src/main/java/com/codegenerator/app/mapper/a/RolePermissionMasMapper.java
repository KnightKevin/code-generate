package com.codegenerator.app.mapper.a;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codegenerator.app.entity.a.RolePermissionMas;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("zstack_cmp_old")
public interface RolePermissionMasMapper extends BaseMapper<RolePermissionMas> {

    @Select("SELECT b.* FROM role_mas a LEFT JOIN role_permission_mas b ON a.uuid = b.role_id WHERE a.name=#{roleName} and a.create_by <> 'system' AND a.create_by <> '';")
    List<RolePermissionMas> listPermissionByRoleName(String roleName);
}