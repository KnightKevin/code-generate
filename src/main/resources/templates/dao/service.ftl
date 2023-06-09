package com.zstack.cmp.resource.service;

import com.querydsl.core.QueryResults;
import com.zstack.cmp.resource.entity.${className};
import com.zstack.cmp.resource.model.req.${className}Qry;

public interface I${className}Service {
    QueryResults<${className}> pageList(${className}Qry qry);

    ${className} getByUuid(String uuid);

    ${className} create(${className} entity);

    ${className} update(${className} entity);

    Long delete(String[] uuids);

}