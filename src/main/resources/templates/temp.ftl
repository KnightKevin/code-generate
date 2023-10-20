package com.zscmp.resource.plugins.mini.api.operate;

import com.mysema.commons.lang.Pair;
import com.zscmp.common.enums.CloudType;
import com.zscmp.resource.pluginsapi.operate.IAccessVmConsole;
import org.springframework.stereotype.Service;

@Service
public class ${fileName} extends com.zscmp.resource.plugins.zstack.api.operate.${fileName} {

    @Override
    public Pair<Class, String> serviceKey() {
        return new Pair(IAccessVmConsole.class, CloudType.MINI.getCode());
    }

}