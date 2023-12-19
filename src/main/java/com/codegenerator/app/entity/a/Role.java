package com.codegenerator.app.entity.a;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`role`")
public class Role {
    
    private Boolean available;
    
    private String createBy;
    
    private String createByName;
    
    private Date createDate;
    
    private Boolean customized;
    
    private String description;
    
    private String name;
    
    private String roleKey;
    
    private String type;
    
    private Date updateDate;
    @TableId
    private String uuid;

}
