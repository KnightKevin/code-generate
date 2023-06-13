package com.zstack.cmp.resource.api;

import com.querydsl.core.QueryResults;
import com.zstack.cmp.resource.model.reply.${className}Reply;
import com.zstack.cmp.resource.model.req.${className}Cmd;
import com.zstack.cmp.resource.model.req.${className}Qry;

public interface I${className}Api {
    QueryResults<${className}Reply> pageList(${className}Qry qry);

    ${className}Reply getByUuid(String uuid);

    ${className}Reply create(${className}Cmd cmd);

    ${className}Reply update(${className}Cmd cmd);

    void delete(String[] uuids);

}