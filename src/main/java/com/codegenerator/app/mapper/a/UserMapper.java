package com.codegenerator.app.mapper.a;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codegenerator.app.entity.a.User;

@DS("zstack_cmp_old")
public interface UserMapper extends BaseMapper<User> {
}