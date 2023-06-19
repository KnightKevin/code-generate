package com.zstack.cmp.resource.model;

import java.util.Date;
import lombok.Data;

@Data
public class ${className}Reply {
<#list list as i>

    private <#if i.dataType == 'varchar'>String<#elseif i.dataType == 'datetime'>Date<#else>还没解析${dataType}</#if> ${i.varName};
</#list>

}
