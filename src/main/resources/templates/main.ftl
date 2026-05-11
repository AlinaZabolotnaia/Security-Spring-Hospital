<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>

<#if loginSuccess?? && loginSuccess>
    <div class="alert alert-success" role="alert">
        Вход выполнен успешно. Здесь вы можете записаться к врачу и посмотреть свои записи.
    </div>
</#if>

<h2 class="mb-3">Запись к врачу</h2>
<p class="text-muted mb-4">
    Эта страница доступна пользователям с ролью <strong>USER</strong>.
    Ниже — ваши текущие записи. Чтобы добавить новую, нажмите «Записаться на приём», выберите дату, время, врача и опишите симптомы.
    <span class="d-block mt-2"><small><strong>Дату и время в прошлом выбрать нельзя.</strong> Один слот у врача — около <strong>30 минут</strong> (±15 мин от уже занятого времени); если время пересекается с записью <em>любого</em> пациента, сохранение будет отклонено и вы увидите предупреждение.</small></span>
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
      <td> <a href="/record/${record.id}">
<input type="submit" class="btn btn-outline-danger" value="Удалить"/></a>
</td>
    </tr>
    </#list>
  </tbody>
</table>

<a class="btn btn-primary btn-lg mb-2" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
    Записаться на приём
</a>
<div class="collapse" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">

            <div class="form-group row col-6">
                <label class="col-auto col-form-label">Дата:</label>
                <div class="col-md-4">
                    <input type="date" class="form-control" name="date" min="${minBookingDate}" required/>
                </div>
                 <label class="col-auto col-form-label">Время:</label>
                 <input type="time" name="time" required>
            </div>

  <div class="form-group">
    <h3> <label for="exampleFormControlSelect2">Выберите врача</label> </h3>
    <select name="doctorId" class="form-control" id="exampleFormControlSelect2" required>
                 <#list doctors as doctor>
                  <option name="doctor" value="${doctor.id}">${doctor}</option>
                 </#list>
    </select>
  </div>

            <div class="form-group">
                <input type="text" class="form-control" name="problem" placeholder="Опишите жалобу или цель визита" required>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="form-group">
                <button type="submit" href="/main" class="btn btn-outline-success">Отправить запись</button>
            </div>

          </div>
        </form>
    </div>
</div>
</@c.page>

