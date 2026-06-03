# Развертывание

![Deployment Diagram](../images/08-deployment-diagram.png)

1. Создать базу PostgreSQL `ladushki`.
2. Настроить `server/src/main/resources/application.yml`.
3. Запустить сервер: `cd server && mvn spring-boot:run`.
4. Открыть `app` в Android Studio.
5. Для эмулятора использовать `http://10.0.2.2:8080/api/`.
6. После входа или регистрации клиент получает JWT и отправляет его в заголовке `Authorization: Bearer <token>`.

## Подробная инструкция

### 1. PostgreSQL

Перед запуском backend нужно создать базу данных:

```sql
CREATE DATABASE ladushki;
```

Параметры подключения находятся в `server/src/main/resources/application.yml`. По умолчанию используются `username: postgres` и `password: postgres`.

### 2. Backend

Backend запускается командой:

```bash
cd server
mvn spring-boot:run
```

После запуска нужно открыть Swagger UI: `http://localhost:8080/swagger-ui.html`. Если страница открывается, сервер и OpenAPI настроены корректно.

### 3. Android

В Android Studio открывается папка `app`. Если используется эмулятор, адрес backend должен быть `http://10.0.2.2:8080/api/`. Для физического телефона нужно использовать IP компьютера в локальной сети.

## Возможные проблемы

| Проблема | Решение |
|---|---|
| Backend не подключается к БД | Проверить имя базы, логин и пароль в `application.yml` |
| Android не видит сервер | Использовать `10.0.2.2` для эмулятора или IP компьютера для телефона |
| Swagger не открывается | Проверить, что сервер запущен на порту 8080 |
| Запросы возвращают 401 | Сначала выполнить вход и передать JWT |

## Вывод

Развёртывание состоит из трёх частей: база PostgreSQL, Spring Boot backend и Android-клиент. Самая частая ошибка — использовать `localhost` внутри эмулятора вместо `10.0.2.2`.
