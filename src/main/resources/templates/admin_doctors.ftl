<#import "parts/common.ftl" as c>

<@c.page>

<#include "parts/admin_nav.ftl">

<#if doctorSaved?? && doctorSaved>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Данные врача сохранены.
        <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть"><span aria-hidden="true">&times;</span></button>
    </div>
</#if>
<#if doctorDeleted?? && doctorDeleted>
    <div class="alert alert-info alert-dismissible fade show" role="alert">
        Врач удалён.
        <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть"><span aria-hidden="true">&times;</span></button>
    </div>
</#if>
<#if doctorDeleteError??>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${doctorDeleteError}
        <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть"><span aria-hidden="true">&times;</span></button>
    </div>
</#if>

<h3 class="mb-3">Врачи</h3>

<table class="table table-bordered">
  <thead class="thead-dark">
    <tr>
      <th>#</th>
      <th>Имя</th>
      <th>Фамилия</th>
      <th>Специализация</th>
      <th>Действия</th>
    </tr>
  </thead>
  <tbody>
    <#list doctors as doc>
    <tr>
      <th scope="row">${doc?index+1}</th>
      <td>${doc.firstName!""}</td>
      <td>${doc.lastName!""}</td>
      <td>${(doc.specialization.title)!""}</td>
      <td class="text-nowrap">
        <a class="btn btn-sm btn-outline-primary" href="/admin/doctors/${doc.id}/edit">Изменить</a>
        <form method="post" action="/admin/doctors/${doc.id}/delete" class="d-inline" onsubmit="return confirm('Удалить врача? Только если нет записей пациентов.');">
          <input type="hidden" name="_csrf" value="${_csrf.token}" />
          <button type="submit" class="btn btn-sm btn-outline-danger">Удалить</button>
        </form>
      </td>
    </tr>
    </#list>
  </tbody>
</table>

<h4 class="mt-4 mb-3">Добавить врача</h4>
<form method="post" action="/admin/doctors" class="border rounded p-3 mb-4">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <div class="form-row">
        <div class="form-group col-md-4">
            <label>Имя</label>
            <input type="text" name="firstName" class="form-control" required maxlength="120"/>
        </div>
        <div class="form-group col-md-4">
            <label>Фамилия</label>
            <input type="text" name="lastName" class="form-control" required maxlength="120"/>
        </div>
        <div class="form-group col-md-4">
            <label>Специализация</label>
            <select name="specialization" class="form-control" required>
                <#list specializations as sp>
                    <option value="${sp.name()}">${sp.title}</option>
                </#list>
            </select>
        </div>
    </div>
    <button type="submit" class="btn btn-success">Добавить</button>
</form>
</@c.page>
