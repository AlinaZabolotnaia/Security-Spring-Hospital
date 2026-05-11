<#import "parts/common.ftl" as c>

<@c.page>

<#if pastBooking?? && pastBooking>
<div class="alert alert-danger alert-dismissible fade show" role="alert">
  <strong>Нельзя записаться задним числом.</strong>
  Выберите сегодняшнюю или будущую дату и время не раньше текущего момента.
  <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть">
    <span aria-hidden="true">&times;</span>
  </button>
</div>
</#if>

<#if slotConflict?? && slotConflict>
<div class="alert alert-warning alert-dismissible fade show" role="alert">
  <strong>Это время уже занято.</strong>
  У выбранного врача на эту дату и время уже есть запись другого пациента (или пересечение в пределах ±15 минут от чужого приёма).
  Запись не сохранена — выберите другое время или другого врача и отправьте форму снова.
  <button type="button" class="close" data-dismiss="alert" aria-label="Закрыть">
    <span aria-hidden="true">&times;</span>
  </button>
</div>
</#if>

<h2 class="mb-3">Запись к врачу</h2>
<p class="text-muted mb-4">
    Ниже — ваши записи. Ошибки бронирования показываются выше.
</p>

<table class="table table-bordered">
  <thead class="thead-dark">
    <tr>
      <th scope="col">#</th>
      <th scope="col">Врач</th>
      <th scope="col">Дата</th>
      <th scope="col">Время</th>
      <th scope="col">Жалоба</th>
      <th scope="col">Действие</th>
    </tr>
  </thead>
  <tbody>
    <#list records as record>
    <tr>
      <th scope="row">${record?index+1}</th>
      <td>${record.doctor}</td>
      <td>${record.date}</td>
      <td>${record.time}</td>
      <td>${record.problem}</td>
      <td><a href="/record/${record.id}" class="btn btn-outline-danger btn-sm">Удалить</a></td>
    </tr>
    </#list>
  </tbody>
</table>

<a class="btn btn-primary btn-lg mb-2" data-toggle="collapse" href="#bookingFormConflict" role="button" aria-expanded="true" aria-controls="bookingFormConflict">
    Записаться на приём
</a>
<div class="collapse show" id="bookingFormConflict">
    <div class="form-group mt-3">
        <form method="post" action="/main" enctype="multipart/form-data">

            <div class="form-group row col-6">
                <label class="col-auto col-form-label">Дата:</label>
                <div class="col-md-4">
                    <input type="date" class="form-control" name="date" min="${minBookingDate}" required/>
                </div>
                <label class="col-auto col-form-label">Время:</label>
                <input type="time" name="time" required>
            </div>

            <div class="form-group">
                <label for="doctorSelectRecover"><strong>Выберите врача</strong></label>
                <select name="doctorId" class="form-control" id="doctorSelectRecover" required>
                    <#list doctors as doctor>
                        <option value="${doctor.id}">${doctor}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <input type="text" class="form-control" name="problem" placeholder="Опишите жалобу или цель визита" required>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="form-group">
                <button type="submit" class="btn btn-outline-success">Отправить запись</button>
            </div>
        </form>
    </div>
</div>
</@c.page>
