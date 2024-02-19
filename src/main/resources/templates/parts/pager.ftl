<#macro pager url page sizes>
    <#assign
    number = page.getNumber()
    size = page.getSize()/>

    <nav>
        <ul class="pagination">
            <li class="page-item">
                <div class="dropdown">
                    <a class="page-link dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" role="button">
                        Size
                    </a>

                    <ul class="dropdown-menu">
                        <#list sizes as s>
                            <li><a class="dropdown-item <#if size == s>active</#if>"
                                   href="<@u.path url + "?page=0&size=" + s/>">${s}</a></li>
                        </#list>
                    </ul>
                </div>
            </li>
            <#if number gte 3>
                <li class="page-item"><a class="page-link" href="<@u.path url + "?page=0&size=" + size/>">1</a></li>
                <#if number gt 3>
                    <li class="page-item disabled"><a class="page-link">...</a></li>
                </#if>
            </#if>
            <#if number - 1 gte 1>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path url + "?page=" + (number - 2) + "&size=" + size/>">${number - 1}</a>
                </li>
            </#if>
            <#if page.getNumber() gte 1>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path url + "?page=" + (number - 1) + "&size=" + size/>">${number}</a>
                </li>
            </#if>
            <li class="page-item active"><a class="page-link">${number + 1}</a></li>
            <#if number + 1 lt page.getTotalPages()>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path url + "?page=" + (number + 1) + "&size=" + size/>">${number + 2}</a>
                </li>
            </#if>
            <#if number + 2 lt page.getTotalPages()>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path url + "?page=" + (number + 2) + "&size=" + size/>">${number + 3}</a>
                </li>
            </#if>
            <#if number lt page.getTotalPages() - 3>
                <#if number lt page.getTotalPages() - 4>
                    <li class="page-item disabled"><a class="page-link">...</a></li>
                </#if>
                <li class="page-item"><a class="page-link"
                                         href="<@u.path url + "?page=" + (page.getTotalPages() - 1) + "&size=" + size/>">${page.getTotalPages()}</a>
                </li>
            </#if>
        </ul>
    </nav>
</#macro>