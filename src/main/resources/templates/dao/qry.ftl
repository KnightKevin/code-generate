package com.zstack.cmp.resource.model.req;

import com.zstack.cmp.common.model.Pagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ${className}Qry extends Pagination {
    private String uuid;
    private String name;

    public ${className}Qry() {
        super();
    }

    public ${className}Qry(Long offset, Long limit, String order, String sort) {
        super(offset, limit, order, sort);
    }
}
