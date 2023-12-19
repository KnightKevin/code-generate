package com.codegenerator.app.entity.b;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`role_permission`")
public class RolePermissionNew {
    
    private Date createDate;
    
    private String permissionKey;
    
    private String roleId;
    @TableId
    private String uuid;

}
