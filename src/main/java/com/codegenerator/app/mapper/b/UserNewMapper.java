package com.codegenerator.app.mapper.b;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codegenerator.app.entity.b.UserNew;

@DS("zstack_cmp")
public interface UserNewMapper extends BaseMapper<UserNew> {
}