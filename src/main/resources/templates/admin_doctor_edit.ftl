<#import "parts/common.ftl" as c>

<@c.page>

<#include "parts/admin_nav.ftl">

<h3 class="mb-3">Редактирование врача</h3>

<form method="post" action="/admin/doctors/${doctor.id}/edit" class="border rounded p-3">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <div class="form-row">
        <div class="form-group col-md-4">
            <label>Имя</label>
            <input type="text" name="firstName" class="form-control" value="${doctor.firstName!""}" required maxlength="120"/>
        </div>
        <div class="form-group col-md-4">
            <label>Фамилия</label>
            <input type="text" name="lastName" class="form-control" value="${doctor.lastName!""}" required maxlength="120"/>
        </div>
        <div class="form-group col-md-4">
            <label>Специализация</label>
            <select name="specialization" class="form-control" required>
                <#list specializations as sp>
                    <option value="${sp.name()}" <#if doctor.specialization?? && doctor.specialization == sp>selected</#if>>${sp.title}</option>
                </#list>
            </select>
        </div>
    </div>
    <button type="submit" class="btn btn-primary">Сохранить</button>
    <a class="btn btn-secondary ml-2" href="/admin/doctors">Отмена</a>
</form>
</@c.page>
