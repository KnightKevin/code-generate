package com.zscmp.resource.plugins.ics.api.operate;

import com.alibaba.fastjson.JSONObject;
import com.mysema.commons.lang.Pair;
import com.zscmp.common.enums.ApiKey;
import com.zscmp.common.enums.CloudType;
import com.zscmp.common.enums.RespType;
import com.zscmp.common.model.MoOperateResult;
import com.zscmp.infrastructure.cloudclient.CloudClient;
import com.zscmp.infrastructure.cloudclient.CloudRequest;
import com.zscmp.resource.model.value.operate.MoOperateCtx;
import com.zscmp.resource.plugins.ics.IcsCommonUtil;
import com.zscmp.resource.plugins.ics.service.IResourceIcs;
import com.zscmp.resource.pluginsapi.operate.I${fileName};
import com.zscmp.sdk.cloudics.model.IcsApiUri;
import com.zscmp.sdk.cloudics.utils.IcsUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ${fileName} implements I${fileName} {

    @Autowired
    private CloudClient cloudClient;

    @Autowired
    private IResourceIcs resourceIcs;

    @Autowired
    private IcsCommonUtil icsCommonUtil;

    @Override
    public Pair<Class, String> serviceKey() {
        return new Pair(I${fileName}.class, CloudType.ICS.getCode());
    }

    public MoOperateCtx executeRemote(MoOperateCtx ctx) {
        InputParams inParams = ctx.getParams().toJavaObject(InputParams.class);
        // todo
        CloudRequest request = IcsUtil.buildCloudRequest(ctx.getAccountParams())
                .serviceUri(IcsApiUri.vms())
                .httpMethod(HttpMethod.POST)
                .body(inParams)
                .apiKey(ApiKey.None)
                .build();

        JSONObject res = cloudClient.execute(request, JSONObject.class);

        MoOperateResult result = new MoOperateResult(RespType.ASYNC_WITH_JOB, res.toJSONString());
        ctx.setResult(result);
        ctx.getDataHolder().put(IcsCommonUtil.KEY_TASK_ID, res.getString(IcsCommonUtil.KEY_TASK_ID));

        return ctx;
    }

    @SneakyThrows
    @Override
    public MoOperateCtx queryJobResult(MoOperateCtx ctx) {
        String taskId = (String) ctx.getDataHolder().get(IcsCommonUtil.KEY_TASK_ID);
        MoOperateResult result = icsCommonUtil.queryTask(ctx, taskId);
        ctx.setResult(result);
        return ctx;
    }


    @Getter
    @Setter
    private static class InputParams implements Serializable {

    }
}
