package com.codegenerator.app.entity.a;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`user`")
public class User {
    
    private Boolean available;
    
    private String avatar;
    
    private String createBy;
    
    private String createByName;
    
    private Date createDate;
    
    private String description;
    
    private String email;
    
    private Boolean mustModifyPwd;
    
    private String nickName;
    
    private String password;
    
    private String phone;
    
    private String realName;
    
    private String scope;
    
    private String type;
    
    private Date updateDate;
    
    private String username;
    @TableId
    private String uuid;

}
