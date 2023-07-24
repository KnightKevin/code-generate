package com.zstack.cmp.resource.model.req;

import java.util.Date;
import lombok.Data;

@Data
public class ${className}Cmd {
<#list list as i>

   <#if !(cmdExcludeFields?seq_contains(i.varName))>
    ${dbTypeConvert(i.dataType)} ${i.varName};
   </#if>
</#list>
}
