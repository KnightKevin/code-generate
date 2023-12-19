package com.codegenerator.app.entity.a;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`role_permission_mas`")
public class RolePermissionMas {
    
    private Date createDate;
    
    private String permissionKey;
    
    private String roleId;
    @TableId
    private String uuid;

}
