package com.zstack.cmp.resource.apiimpl;

import com.querydsl.core.QueryResults;
import com.zstack.cmp.common.model.PageResults;
import com.zstack.cmp.infrastructure.utils.ModelMapperUtil;
import com.zstack.cmp.resource.api.I${className}Api;
import com.zstack.cmp.resource.entity.${className};
import com.zstack.cmp.resource.model.${className}Reply;
import com.zstack.cmp.resource.model.req.${className}Cmd;
import com.zstack.cmp.resource.model.req.${className}Qry;
import com.zstack.cmp.resource.service.I${className}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ${className}ApiImpl implements I${className}Api {

    @Autowired
    private I${className}Service ${tableClassVarName}Service;

    @Override
    public PageResults<${className}Reply> pageList(${className}Qry qry) {
        QueryResults<${className}> result = ${tableClassVarName}Service.pageList(qry);

        List<${className}Reply> replies = new ArrayList<>();

        result.getResults().forEach(i -> replies.add(convertToReply(i)));

        return new PageResults<>(qry.getLimit(), qry.getOffset(), result.getTotal(), replies);
    }

    @Override
    public ${className}Reply getByUuid(String uuid) {
        ${className} entity = ${tableClassVarName}Service.getByUuid(uuid);
        
        if (entity == null) {
            return null;
        }
        
        ${className}Reply reply = convertToReply(entity);
        return reply;
    }

    @Override
    public ${className}Reply create(${className}Cmd cmd) {
        ${className} entity = ModelMapperUtil.getMapper().map(cmd, ${className}.class);
        
        ${tableClassVarName}Service.create(entity);
        
        return convertToReply(entity);
    }

    @Override
    public ${className}Reply update(${className}Cmd cmd) {
        ${className} entity = ModelMapperUtil.getMapper().map(cmd, ${className}.class);

        ${tableClassVarName}Service.update(entity);

        return convertToReply(entity);
    }

    @Override
    public void delete(String[] uuids) {
        ${tableClassVarName}Service.delete(uuids);
    }

    private ${className}Reply convertToReply(${className} entity) {
        ${className}Reply reply = ModelMapperUtil.getMapper().map(entity, ${className}Reply.class);

        return reply;
    }
}
