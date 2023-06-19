package com.codegenerator.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Xmm on 2018/08/23.
 */
@Getter
@AllArgsConstructor
public enum RespCode {
    IP_POOL_IP_RANGE_OVERLAP_CHECK(736, "[%s ~ %s]不能与其他网络段重叠"),
    IP_POOL_IP_EXIST_CHECK(737, "IP池不存在"),
    IP_POOL_738_CHECK(738, "无法从ip池中找到合适的ip，[%s]"),
    IP_POOL_739_CHECK(739, "ip不存在，[%s]"),
    IP_POOL_740_CHECK(740, "ip[%s]的状态必须为“可用“。 [%s]");

    private final int code;
    private final String msg;

    public static RespCode get(int code) {
        for (RespCode item : values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
