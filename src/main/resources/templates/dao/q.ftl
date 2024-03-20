package ${entityPackage}.querydsl;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import ${entityPackage}.${className};

import javax.annotation.Generated;

import java.math.BigDecimal;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * Q${className} is a Querydsl query type for ${className}
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class Q${className} extends EntityPathBase<${className}> {

    private static final long serialVersionUID = 2104313057L;

    public static final Q${className} ${tableClassVarName} = new Q${className}("${tableClassVarName}");

<#list list as i>

 <#if i.dataType == 'varchar' || i.dataType == 'mediumtext' || i.dataType == 'text'>
    public final StringPath ${i.varName} = createString("${i.varName}");
 <#elseif i.dataType == 'int' || i.dataType == 'tinyint'>
    public final NumberPath<Integer> ${i.varName} = createNumber("${i.varName}", Integer.class);
 <#elseif i.dataType == 'bigint'>
    public final NumberPath<Long> ${i.varName} = createNumber("${i.varName}", Long.class);
 <#elseif i.dataType == 'double'>
    public final NumberPath<Double> ${i.varName} = createNumber("${i.varName}", Double.class);
 <#elseif i.dataType == 'decimal'>
    public final NumberPath<BigDecimal> ${i.varName} = createNumber("${i.varName}", BigDecimal.class);
 <#elseif i.dataType == 'datetime'>
    public final DateTimePath<java.util.Date> ${i.varName} = createDateTime("${i.varName}", java.util.Date.class);
 <#else>
    todo 须要支持${i.dataType}的解析
 </#if>
</#list>


    public Q${className}(String variable) {
        super(${className}.class, forVariable(variable));
    }

    public Q${className}(Path<? extends ${className}> path) {
        super(path.getType(), path.getMetadata());
    }

    public Q${className}(PathMetadata metadata) {
        super(${className}.class, metadata);
    }

}

