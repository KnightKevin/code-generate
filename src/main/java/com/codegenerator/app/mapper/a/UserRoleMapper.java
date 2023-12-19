package com.codegenerator.app.mapper.a;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codegenerator.app.entity.a.UserRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("zstack_cmp_old")
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("select a.* from user_role a left join role b on a.role_id = b.uuid where b.type = 'platform'")
    List<UserRole> listPlatform();
}