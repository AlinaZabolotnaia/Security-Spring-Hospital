<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<h3 class="mb-3">Вход в аккаунт</h3>

<#if loginError?? && loginError>
    <div class="alert alert-danger" role="alert">
        Неверное имя пользователя или пароль. Проверьте данные и попробуйте снова.
    </div>
</#if>
<#if registrationSuccess?? && registrationSuccess>
    <div class="alert alert-success" role="alert">
        Регистрация прошла успешно. Теперь войдите, указав логин и пароль.
    </div>
</#if>
<#if loggedOut?? && loggedOut>
    <div class="alert alert-info" role="alert">
        Вы вышли из аккаунта.
    </div>
</#if>

<@l.login "/login" false/>
</@c.page>
