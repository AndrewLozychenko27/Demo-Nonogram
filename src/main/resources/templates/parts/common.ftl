<#import "util.ftl" as u />
<#include "security.ftl" />

<#macro page title>
    <html>
    <head>
        <title>${title}</title>
        <link rel="stylesheet" href="../../static/style.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
              crossorigin="anonymous">
    </head>
    <body>
    <div id="header">
        <nav class="navbar navbar-expand-lg bg-bamboo-green-light">
            <div class="container-fluid w-75">
                <a class="navbar-brand" href="<@u.path "" />">Nonogram</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                        </li>
                    </ul>
                    <#if currentUser??>
                        <a class="inline navbar-brand" href="<@u.path "user/profile" />">${currentUser.nickname}</a>
                        <@form "logout">
                            <@submit "Log out" "dark" />
                        </@form>
                    <#else>
                        <@link "Sign in" "login" "dark" />
                    </#if>
                </div>
            </div>
        </nav>
    </div>
    <div id="menu" class="fixed-top-on-scroll">
        <nav class="navbar navbar-expand-lg bg-bamboo-green">
            <div class="container-fluid w-75">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="#">My games</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Puzzles</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link disabled" aria-disabled="true">Disabled</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    <div id="content" class="w-75 h-78 mx-auto my-4 p-4">
        <#nested>
    </div>
    <div id="footer">
        <nav class="navbar navbar-expand-lg bg-bamboo-green-light">
            <div class="container-fluid w-75">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link">About</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    </body>
    </html>
</#macro>

<#macro field label value >
    <div class="mb-3 row">
        <label class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <span class="form-control">${value}</span>
        </div>
    </div>
</#macro>

<#macro hidden name value >
    <input type="hidden" name="${name}" value="${value}">
</#macro>

<#macro input label name=label?c_lower_case >
    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="text" class="form-control" name="${name}" value="<#nested>">
        </div>
    </div>
</#macro>

<#macro password label="Password" name="password" >
    <div class="mb-3 row">
        <label for="${name}" class="col-sm-4 col-form-label">${label}</label>
        <div class="col-sm-8">
            <input type="password" class="form-control" name="${name}">
        </div>
    </div>
</#macro>

<#macro submit label style="success" >
    <input type="submit" class="btn btn-outline-${style}" value="${label}">
</#macro>

<#macro submitPair label cancel="" >
    <div class="mb-3 mt-4 row">
        <div class="col-sm-6">
            <@link "Cancel" cancel "btn btn-outline-danger" />
        </div>
        <div class="col-sm-6 d-flex flex-row-reverse">
            <button type="submit" class="btn btn-outline-success">${label}</button>
        </div>
    </div>
</#macro>

<#macro formBody label >
    <div class="m-3 p-3 border border-success-subtle rounded-3 px-4">
        <div class="row">
            <h3 class="text-center my-3">${label}</h3>
        </div>
        <div class="my-3">
            <#nested />
        </div>
    </div>
</#macro>

<#macro form action >
    <form action="<@u.path action />" method="POST" class="m-0">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#nested />
    </form>
</#macro>

<#macro link label path style="secondary" >
    <a class="btn btn-outline-${style}" role="button" href="<@u.path path />">${label}</a>
</#macro>