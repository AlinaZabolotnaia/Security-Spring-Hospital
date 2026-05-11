<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<h3 class="mb-3">Регистрация нового пользователя</h3>

<#if message??>
    <div class="alert alert-danger" role="alert">${message}</div>
</#if>

<@l.login "/registration" true />
</@c.page>
