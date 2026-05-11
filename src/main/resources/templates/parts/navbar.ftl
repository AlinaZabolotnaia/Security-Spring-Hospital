<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Запись к врачу</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Главная</a>
            </li>
            <#if isAuthenticated>
                <#if isUser>
                    <li class="nav-item">
                        <a class="nav-link font-weight-bold" href="/main">Мои записи к врачу</a>
                    </li>
                </#if>
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" href="/admin">Админ: записи</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/admin/doctors">Админ: врачи</a>
                    </li>
                </#if>
            <#else>
                <li class="nav-item">
                    <a class="nav-link" href="/registration">Регистрация</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/login">Вход</a>
                </li>
            </#if>
        </ul>

        <#if isAuthenticated>
            <div class="navbar-text mr-3">${name}</div>
            <@l.logout />
        </#if>
    </div>
</nav>
