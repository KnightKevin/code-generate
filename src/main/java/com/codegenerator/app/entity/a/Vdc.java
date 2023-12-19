package com.codegenerator.app.entity.a;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`vdc`")
public class Vdc {
    
    private String createBy;
    
    private String createByName;
    
    private Date createDate;
    
    private String description;
    
    private String dispatchStrategy;
    
    private Integer level;
    
    private String name;
    
    private String number;
    
    private String parentUuid;
    
    private String tenantUuid;
    
    private Date updateDate;
    @TableId
    private String uuid;

}
