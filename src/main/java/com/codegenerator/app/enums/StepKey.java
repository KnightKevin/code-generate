package com.codegenerator.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StepKey {
    hostLocation("物理机落点"),

    requestCreateVm("调用云平台的创建云主机接口"),

    requestCreateVolume("调用云平台的创建磁盘接口"),


    queryResult("轮询接口结果"),

    resolveResourcesToBeSync("解析须要同步的关联资源"),

    ansible("创建云主机"),

    requestStopVmHttp("调用云平台的云主机关机接口"),

    requestChangeVmSettingHttpStep("调用云平台的修改云主机规格接口"),

    requestCloneVmHttpStep("调用云平台的克隆云主机接口"),

    requestDeleteVmHttpStep("调用云平台的删除云主机接口"),

    requestDeleteVolumeHttpStep("调用云平台的删除云盘接口"),

    requestResizeVolumeHttpStep("调用云平台的云盘扩容接口"),

    syncResources("同步资源");

    final String msg;
}
