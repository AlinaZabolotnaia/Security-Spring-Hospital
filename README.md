# Hospital — запись к врачу

Веб-приложение на **Spring Boot**: регистрация и вход пользователей, запись на приём, панель администратора. UI на **FreeMarker**, данные в **PostgreSQL**.

## Стек

- Java **8**, Spring Boot **2.0**, Spring Data JPA, Spring Security  
- PostgreSQL, Lombok  
- Шаблоны: `src/main/resources/templates/*.ftl`

Сборка и тесты: `mvn clean package`, `mvn test`. Для компиляции с Lombok лучше использовать **JDK 8**, как в `pom.xml`.

## Запуск

1. Создайте БД PostgreSQL и пропишите URL, логин и пароль в `src/main/resources/application.properties`.  
2. Схема создаётся Hibernate (`spring.jpa.hibernate.ddl-auto=update`).  
3. Запуск: `mvn spring-boot:run` или запуск класса `org.main.Application` из IDE.

При старте создаётся учётная запись администратора (см. `AdminUserInitializer`: по умолчанию логин/пароль **admin** / **admin** — смените в продакшене).

## Роли и доступ

| Роль   | Назначение |
|--------|------------|
| **USER**  | Главная, `/main` — свои записи, создание записи к врачу, удаление своей записи |
| **ADMIN** | `/admin` — все записи (редактирование/удаление), CRUD врачей |

Регистрация через `/registration` выдаёт только роль **USER**.

## Основные URL

- `/`, `/login`, `/registration` — публично  
- `/main`, `/record/{id}` — только **USER**  
- `/admin/**` — только **ADMIN**  
- REST под префиксом `/api` (например `/api/me`, `/api/doctors`, `/api/admin/records`) — по правилам Security: неаутентифицированный клиент получает **401**, при недостаточных правах — **403**

## Бизнес-логика записи

- Запись в **прошлом** запрещена.  
- У одного врача слот занят, если новое время попадает в окно **±15 минут** от уже существующей записи **любого** пациента (проверка на сервере).

## Врачи и специализации

Специализации заданы перечислением `SPECIALIZATION` (enum). Админ создаёт и редактирует врачей в разделе «Врачи».

## Тесты и Postman

- Модульные тесты: `src/test/java/...` (Mockito).  
- Коллекция для проверки RBAC: `postman/Hospital-RBAC.postman_collection.json` (импорт в Postman).

## Структура (кратко)

- `controllers` — MVC и REST  
- `services` — логика записей, врачей, пользователей  
- `entities` — `User`, `Doctor`, `Record`, роли, enum специализаций  
- `config` — Security, MVC, стартовый админ  
