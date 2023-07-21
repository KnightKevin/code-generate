package com.zstack.cmp.resource.model.req;

import java.util.Date;
import lombok.Data;

@Data
public class ${className}Cmd {
<#list list as i>

   <#if !(cmdExcludeFields?seq_contains(i.varName))>
    private <#if i.dataType == 'varchar'> String <#elseif i.dataType == 'datetime'> Date <#else> 还没解析${i.dataType} </#if> ${i.varName};
   </#if>
</#list>

}
