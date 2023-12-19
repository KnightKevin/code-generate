package ${entityPackage};

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`${tableName}`")
public class ${className} {
<#list list as i>
    <#if i.varName == 'uuid'>@TableId</#if>
    private ${dbTypeConvert(i.dataType)} ${i.varName};
</#list>

}
