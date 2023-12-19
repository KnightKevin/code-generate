package <#if db == 'zstack_cmp_old'>com.codegenerator.app.mapper.a;<#else>com.codegenerator.app.mapper.b;</#if>

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${entityPackage}.${className};

@DS("${db}")
public interface ${className}Mapper extends BaseMapper<${className}> {
}