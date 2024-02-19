<#macro path ref >http://localhost:8080/${ref}</#macro>

<#macro link label ref style="secondary">
    <a class="btn btn-outline-${style}" role="button" href="<@path ref/>">${label}</a>
</#macro>