package com.codegenerator.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MoType {
//    VM("vm", "云主机"),
    VOLUME("volume", "云盘"),
    SNAPSHOT("snapshot", "快照"),
    IMAGE("image", "镜像"),
    VPC("vpc", "虚拟私有云"),
    SECURITY_GROUP("sg", "安全组"),
    EIP("eip", "弹性IP"),
    ELB("elb", "弹性负载均衡"),
    BMS("bms", "裸金属服务器"),
    HOST("host","物理机"),
    STORAGE("storage","存储"),
    ;

    private final String code;
    private final String msg;

    public static String msgOfCode(String code) {
        for (MoType i : values()) {
            if (i.code.equals(code)) {
                return i.msg;
            }
        }

        return "";
    }

    public static MoType moTypeOf(String code) {
        for (MoType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        throw new IllegalArgumentException(String.format("no enum item %s", code));
    }

//    public static boolean isShared(String code) {
//        return !VM.code.equals(code) && !VOLUME.code.equals(code) && !BMS.code.equals(code);
//    }
}
