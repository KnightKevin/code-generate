package com.codegenerator.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {
    @TableId
    private String id;
    private String name;
}
