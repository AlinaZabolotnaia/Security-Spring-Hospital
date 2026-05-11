<#import "parts/common.ftl" as c>

<@c.page>

<#include "parts/admin_nav.ftl">

<#if slotConflict?? && slotConflict>
<div class="alert alert-warning alert-dismissible fade show" role="alert">
  <strong>Время занято.</strong> У этого врача на выбранную дату уже есть другая запись в пределах ±15 минут (не считая текущую).
  <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть"><span aria-hidden="true">&times;</span></button>
</div>
</#if>

<h3 class="mb-3">Редактирование записи №${record.id}</h3>

<form method="post" action="/admin/records/${record.id}/edit" class="border rounded p-3">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />

    <div class="form-group">
        <label>Пациент</label>
        <select name="userId" class="form-control" required>
            <#list patients as p>
                <option value="${p.id}" <#if record.user?? && record.user.id == p.id>selected</#if>>${p.username}</option>
            </#list>
        </select>
    </div>

    <div class="form-group">
        <label>Врач</label>
        <select name="doctorId" class="form-control" required>
            <#list doctors as d>
                <option value="${d.id}" <#if record.doctor?? && record.doctor.id == d.id>selected</#if>>${d}</option>
            </#list>
        </select>
    </div>

    <div class="form-row">
        <div class="form-group col-md-4">
            <label>Дата</label>
            <input type="date" name="date" class="form-control" required value="${record.date!''}" />
        </div>
        <div class="form-group col-md-4">
            <label>Время</label>
            <input type="time" name="time" class="form-control" required value="${record.time!''}" />
        </div>
    </div>

    <div class="form-group">
        <label>Жалоба / цель визита</label>
        <input type="text" name="problem" class="form-control" value="${record.problem!''}" required maxlength="500"/>
    </div>

    <p class="text-muted small">Правило слотов ±15 минут как у пациентов; прошлые даты администратор может оставить для исправления данных.</p>

    <button type="submit" class="btn btn-primary">Сохранить</button>
    <a class="btn btn-secondary ml-2" href="/admin">К списку записей</a>
</form>
</@c.page>
