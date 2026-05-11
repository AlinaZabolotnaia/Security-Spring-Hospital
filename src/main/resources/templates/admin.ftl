<#import "parts/common.ftl" as c>

<@c.page>

<#include "parts/admin_nav.ftl">

<#if loginSuccess?? && loginSuccess>
    <div class="alert alert-success" role="alert">
        Вход выполнен успешно. Вы в панели администратора.
    </div>
</#if>
<#if recordDeleted?? && recordDeleted>
    <div class="alert alert-info alert-dismissible fade show" role="alert">
        Запись удалена.
        <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть"><span aria-hidden="true">&times;</span></button>
    </div>
</#if>
<#if recordUpdated?? && recordUpdated>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Запись обновлена.
        <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть"><span aria-hidden="true">&times;</span></button>
    </div>
</#if>

<h3 class="mb-3">Все записи на приём</h3>
<p class="text-muted">Редактирование и удаление — действия администратора.</p>

<table class="table table-bordered table-sm">
  <thead class="thead-dark">
    <tr>
      <th scope="col">#</th>
      <th scope="col">Пациент</th>
      <th scope="col">Врач</th>
      <th scope="col">Дата</th>
      <th scope="col">Время</th>
      <th scope="col">Жалоба</th>
      <th scope="col">Действия</th>
    </tr>
  </thead>
  <tbody>
    <#list records as record>
    <tr>
      <th scope="row">${record?index+1}</th>
      <td>${record.user.username!"-"}</td>
      <td>${record.doctor!"-"}</td>
      <td>${record.date!"-"}</td>
      <td>${record.time!"-"}</td>
      <td>${record.problem!"-"}</td>
      <td class="text-nowrap">
        <a class="btn btn-sm btn-outline-primary" href="/admin/records/${record.id}/edit">Изменить</a>
        <form method="post" action="/admin/records/${record.id}/delete" class="d-inline" onsubmit="return confirm('Удалить эту запись?');">
          <input type="hidden" name="_csrf" value="${_csrf.token}" />
          <button type="submit" class="btn btn-sm btn-outline-danger">Удалить</button>
        </form>
      </td>
    </tr>
    </#list>
  </tbody>
</table>
</@c.page>
