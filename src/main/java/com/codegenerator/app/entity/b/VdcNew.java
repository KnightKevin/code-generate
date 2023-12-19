package com.codegenerator.app.entity.b;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`vdc`")
public class VdcNew {
    
    private String createBy;
    
    private String createByName;
    
    private Date createDate;
    
    private String description;
    
    private Integer level;
    
    private String name;
    
    private String number;
    
    private String parentUuid;
    
    private String regionId;
    
    private String tenantUuid;
    
    private Date updateDate;
    @TableId
    private String uuid;

}
