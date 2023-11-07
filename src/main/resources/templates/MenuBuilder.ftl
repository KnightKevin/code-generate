<#macro menuTree i>
Menu.builder()
            .id("${i.id}")
            .name("${i.name}")
            .type("${i.type}")

            <#if i.range?? && (i.range?size > 0)>.range(<#list i.range as m>"${m}"<#if (m_has_next)>,</#if></#list>)</#if>

            <#if i.rights?? && (i.rights?size > 0)>.rights(<#list i.rights as m>"${m}"<#if (m_has_next)>,</#if></#list>)</#if>

            <#list i.children as m>
            .children(<@menuTree i = m />)
            </#list>
            .build()
</#macro>

private static final Menus.Builder MENUS_BUILDER = new Menus.Builder()
<#list list as i>
            .menu(<@menuTree i = i />)
</#list>
            ;