package com.codegenerator.app.entity.a;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`vdc_role_relation`")
public class VdcRoleRelation {
    
    private String roleId;
    @TableId
    private String uuid;
    
    private String vdcId;

}
