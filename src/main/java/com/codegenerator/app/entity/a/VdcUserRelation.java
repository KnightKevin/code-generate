package com.codegenerator.app.entity.a;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`vdc_user_relation`")
public class VdcUserRelation {
    
    private Boolean platformAppoint;
    
    private String userUuid;
    @TableId
    private String uuid;
    
    private String vdcUuid;

}
