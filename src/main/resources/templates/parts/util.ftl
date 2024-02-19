<#macro path ref >http://localhost:8080/${ref}</#macro>

<#macro link label path style="secondary">
    <a class="btn btn-outline-${style}" role="button" href="<@path path />">${label}</a>
</#macro>