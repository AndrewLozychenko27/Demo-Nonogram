<#import "form.ftl" as f/>
<#import "util.ftl" as u/>
<#include "security.ftl"/>

<#macro page title width="w-75">
    <html lang="en">
    <head>
        <title>${title}</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="../../static/style.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
              crossorigin="anonymous">
    </head>
    <body>
    <div id="header">
        <nav class="navbar navbar-expand-lg bg-bamboo-green-light">
            <div class="container-fluid w-75">
                <a class="navbar-brand" href="<@u.path ""/>">Nonogram</a>
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
                </div>
            </div>
        </nav>
    </div>
    <div id="menu" class="fixed-top-on-scroll z-3">
        <nav class="navbar navbar-expand-lg bg-bamboo-green">
            <div class="container-fluid w-75">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <#if currentUser??>
                        <#if currentUser.role == "ADMIN">
                            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                                <li class="nav-item">
                                    <a class="nav-link active" aria-current="page"
                                       href="<@u.path "user/list"/>">Users</a>
                                </li>
                            </ul>
                        <#else>
                            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                                <li class="nav-item">
                                    <a class="nav-link active" aria-current="page" href="#">My games</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="<@u.path "puzzle/list"/>">Puzzles</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="<@u.path "game/leaders"/>">Leaderboard</a>
                                </li>
                            </ul>
                        </#if>
                        <a class="inline navbar-brand"
                           href="<@u.path "user/" + currentUser.id + "/profile"/>">${currentUser.nickname}</a>
                        <@f.form "logout">
                            <@f.submit "Log out" "dark"/>
                        </@f.form>
                    <#else>
                        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="#">How to play</a>
                            </li>
                        </ul>
                        <@u.link "Sign in" "login" "dark"/>
                    </#if>
                </div>
            </div>
        </nav>
    </div>
    <div id="content" class="${width} h-78 mx-auto my-4 p-4 z-0">
        <#nested>
        <div class="h-25"></div>
    </div>
    <div class="z-3 mt-4 pt-4" id="footer">
        <nav class="navbar navbar-expand-lg fixed-bottom bg-bamboo-green-light">
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
    <script>
        function doGet(url) {
            return fetch(url, {method: "GET"});
        }

        function doPost(url, body) {
            return fetch(url, {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                    "X-CSRF-Token": "${_csrf.token}"
                },
                body: JSON.stringify(body)
            });
        }
    </script>
    </body>
    </html>
</#macro>