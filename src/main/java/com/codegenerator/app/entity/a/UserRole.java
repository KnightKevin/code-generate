package com.codegenerator.app.entity.a;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`user_role`")
public class UserRole {
    
    private Date createDate;
    
    private String roleId;
    
    private String userId;
    @TableId
    private String uuid;

}