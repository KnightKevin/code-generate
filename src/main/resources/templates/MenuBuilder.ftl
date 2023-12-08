<#macro menuTree i>
Menu.builder()
            .key("${i.key}")
            .name("${i.name}")
            .type(MenuType.${i.type?upper_case}.getCode())
            <#if i.range?? && (i.range?size > 0)>.range(<#list i.range as m>RoleType.${m?upper_case}.getCode()<#if (m_has_next)>,</#if></#list>)</#if>
            <#if i.rights?? && (i.rights?size > 0)>.rights(<#list i.rights as m>"${m}"<#if (m_has_next)>,</#if></#list>)</#if>
            <#list i.children as m>
            .children(<@menuTree i = m />)
            </#list>
            .build()
</#macro>

private static final ModuleMenu.Builder MENUS_BUILDER = new ModuleMenu.Builder()
<#list list as i>
            .menu(<@menuTree i = i />)
</#list>
            ;