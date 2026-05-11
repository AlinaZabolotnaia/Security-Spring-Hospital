<#import "parts/common.ftl" as c>

<@c.page>

<#if loginSuccess?? && loginSuccess>
    <div class="alert alert-success" role="alert">
        Вход выполнен успешно. Добро пожаловать!
    </div>
</#if>

<h1 class="mb-3">Hospital schedule assistant</h1>

<#if isAuthenticated>
    <p class="lead">Здравствуйте, <strong>${name}</strong>. Вы вошли в систему.</p>

    <#if isUser>
        <p class="mb-3">Чтобы <strong>записаться к врачу</strong> (выбрать врача, дату и время), откройте страницу «Мои записи» — там таблица ваших приёмов и форма новой записи.</p>
        <p class="mb-4">
            <a class="btn btn-success btn-lg" href="/main">Перейти к записи к врачу</a>
        </p>
    </#if>

    <p class="mb-2">Доступные действия для вашей роли:</p>
    <ul>
        <#if isUser>
            <li><a href="/main">Мои записи</a> — записаться на приём и управлять своими визитами (роль USER).</li>
        </#if>
        <#if isAdmin>
            <li><a href="/admin">Записи</a> и <a href="/admin/doctors">врачи</a> — управление записями и каталогом врачей (роль ADMIN).</li>
        </#if>
    </ul>
    <p class="text-muted small mb-0">Чтобы выйти, нажмите <strong>Выйти</strong> в шапке страницы.</p>
<#else>
    <p class="lead">Добро пожаловать. Обычный сценарий:</p>
    <ol class="mb-4">
        <li><strong>Регистрация</strong> — <a href="/registration">создайте аккаунт</a> (назначается роль USER).</li>
        <li><strong>Вход</strong> — <a href="/login">войдите</a> под логином и паролем.</li>
        <li><strong>Запись к врачу</strong> — после входа откройте «Мои записи»: там список приёмов и кнопка «Записаться на приём».</li>
        <li><strong>Выход</strong> — кнопка в шапке после завершения работы.</li>
    </ol>
</#if>

<div class="mt-4">
    <img src="/static/images/hospital-banner.svg" class="img-fluid rounded shadow-sm" width="900" height="320" alt="Больница">
</div>
</@c.page>
