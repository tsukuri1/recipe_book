# Мобильное приложение для ведения кулинарных рецептов «Ладушки»

**Автор:** Силина Оксана Романовна 
**Группа:** ПИЖ-б-о-23-2 

## Описание проекта

«Ладушки» — мобильное приложение для домашней кухни. Пользователь может хранить рецепты, искать блюда по названию и категории, добавлять собственные рецепты с фотографиями, ингредиентами, граммовкой и КБЖУ, а также формировать список покупок из выбранного рецепта.

Система поддерживает роли пользователя и администратора. Пользователь создаёт и редактирует рецепты, работает с избранным, покупками, профилем и настройками. Администратор модерирует рецепты, просматривает статистику и обрабатывает жалобы.

## Траектория выполнения

- [x] **Мобильная разработка** (Android Native + Spring Boot)
- [ ] Десктоп
- [ ] Веб-разработка
- [ ] Enterprise

## Технологический стек

| Компонент | Технология |
|---|---|
| **Backend** | Java 17, Spring Boot 3, PostgreSQL, JPA/Hibernate |
| **Мобильный клиент** | Kotlin, Jetpack Compose, Navigation Compose, ViewModel, StateFlow |
| **Локальное хранение** | Room, SharedPreferences |
| **API** | REST, OpenAPI 3.0, Swagger UI |
| **Безопасность** | JWT, Spring Security, BCrypt |
| **Сетевой слой** | Retrofit, OkHttp |
| **Сборка** | Maven, Gradle Wrapper 8.7 |
| **Тестирование** | JUnit 5, Mockito, JaCoCo |
| **Документация** | Markdown, PlantUML, PNG-диаграммы |
| **Инструменты** | Git, Postman, Android Studio |

## Требования к окружению

| Требование | Версия |
|---|---|
| Java JDK | 17+ |
| PostgreSQL | 14+ |
| Maven | 3.8+ |
| Android Studio | 2024+ |
| Android SDK | API 26+ |
| Gradle | используется wrapper 8.7 |
| Git | 2.30+ |

## Установка и запуск

### 1. Клонирование репозитория

```bash
git clone https://github.com/lookitsssonya/course-project.git
cd course-project
```

### 2. Запуск backend

```bash
cd server
mvn spring-boot:run
```

Сервер запускается на `http://localhost:8080`.

Полезные адреса:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- REST API base URL: `http://localhost:8080/api`

### 3. Запуск мобильного клиента

1. Открыть папку [app/](app/) в Android Studio.
2. Дождаться Gradle Sync.
3. Подключить устройство или запустить эмулятор.
4. Запустить модуль `app`.

Для Android-эмулятора backend обычно доступен по адресу `http://10.0.2.2:8080/api/`.

## Тестовые аккаунты

| Роль | Email | Пароль | Возможности |
|---|---|---|---|
| Пользователь | `user@ladushki.app` | `1234` | Каталог, избранное, покупки, профиль, настройки |
| Администратор | `admin@ladushki.app` | `1234` | Все функции пользователя, модерация рецептов, жалобы |

## API Endpoints

Базовый URL: `http://localhost:8080/api`

| Метод | Эндпоинт | Описание | Доступ |
|---|---|---|---|
| POST | `/api/auth/register` | Регистрация пользователя | PUBLIC |
| POST | `/api/auth/login` | Вход в систему | PUBLIC |
| GET | `/api/users/me` | Получение профиля | USER |
| GET | `/api/recipes` | Список рецептов | USER |
| GET | `/api/recipes/{id}` | Детали рецепта | USER |
| GET | `/api/recipes/search` | Поиск рецептов | USER |
| POST | `/api/recipes` | Создание рецепта | USER |
| PUT | `/api/recipes/{id}` | Редактирование рецепта | OWNER/ADMIN |
| DELETE | `/api/recipes/{id}` | Удаление рецепта | OWNER/ADMIN |
| POST | `/api/favorites/{recipeId}` | Добавить в избранное | USER |
| DELETE | `/api/favorites/{recipeId}` | Убрать из избранного | USER |
| GET | `/api/shopping-list` | Получить список покупок | USER |
| POST | `/api/shopping-list/from-recipe/{recipeId}` | Добавить ингредиенты рецепта в покупки | USER |
| PATCH | `/api/shopping-list/items/{itemId}` | Отметить продукт купленным | USER |
| DELETE | `/api/shopping-list/completed` | Очистить купленные продукты | USER |
| DELETE | `/api/shopping-list` | Полностью очистить покупки | USER |
| GET | `/api/settings` | Получить настройки | USER |
| PUT | `/api/settings` | Сохранить настройки | USER |
| GET | `/api/admin/stats` | Статистика администратора | ADMIN |
| GET | `/api/admin/recipes/pending` | Рецепты на проверке | ADMIN |
| PATCH | `/api/admin/recipes/{id}/approve` | Одобрить рецепт | ADMIN |
| PATCH | `/api/admin/recipes/{id}/reject` | Отклонить рецепт | ADMIN |
| GET | `/api/admin/complaints` | Жалобы пользователей | ADMIN |
| PATCH | `/api/admin/complaints/{id}/review` | Взять жалобу в работу | ADMIN |
| PATCH | `/api/admin/complaints/{id}/resolve` | Закрыть жалобу | ADMIN |

Полная документация API: [Swagger UI](http://localhost:8080/swagger-ui.html) и [docs/09-api/openapi-endpoints.md](docs/09-api/openapi-endpoints.md).

## Структура документации

Вся документация находится в папке [docs/](docs/):

| Папка | Артефакты |
|---|---|
| [00-project-charter/](docs/00-project-charter/) | Паспорт проекта, IDEF0, BUC, бизнес-классы, стейкхолдеры, SWOT, ROI, глоссарий |
| [01-requirements/](docs/01-requirements/) | Use Case, Domain Model, требования, трассировка |
| [02-architecture/](docs/02-architecture/) | PCMEF, ADR, интерфейсы, зависимости |
| [03-database/](docs/03-database/) | ER-диаграмма, DDL, ORM |
| [03-tables/](docs/03-tables/) | Таблицы для пояснительной записки |
| [04-detailed-design/](docs/04-detailed-design/) | Sequence diagrams, спецификации методов, классы проектирования |
| [05-implementation/](docs/05-implementation/) | Структура проекта, реализация слоёв, паттерны |
| [06-testing/](docs/06-testing/) | Тест-план, JaCoCo |
| [07-refactoring/](docs/07-refactoring/) | Запахи кода, Data Mapper, Identity Map, обновлённые тесты |
| [08-ui/](docs/08-ui/) | UI-концепция, скриншоты, навигация |
| [09-api/](docs/09-api/) | REST endpoints, проверка API |
| [10-deployment/](docs/10-deployment/) | Развёртывание и администрирование |
| [11-guides/](docs/11-guides/) | Техническое задание, руководство пользователя, руководство администратора |
| [12-project-management/](docs/12-project-management/) | WBS, диаграмма Ганта, COCOMO |
| [13-final-report/](docs/13-final-report/) | Пояснительная записка |
| [images/](docs/images/) | PNG-диаграммы |

## Архитектура PCMEF

Система построена на архитектурном паттерне PCMEF (Presentation-Control-Mediator-Entity-Foundation).

| Слой | Расположение | Ответственность |
|---|---|---|
| **Presentation (P)** | Android, Jetpack Compose | UI, отображение, ввод данных |
| **Control (C)** | Android ViewModel, Spring REST Controllers | Управление состоянием и HTTP API |
| **Mediator (M)** | Android Interactor, Spring Services | Бизнес-сценарии, транзакции, правила |
| **Entity (E)** | Kotlin models, JPA entities | Модели предметной области |
| **Foundation (F)** | Repository, Retrofit, Room, Spring Data JPA | Доступ к данным, API-клиенты, локальный кэш |

Ключевые ADR описаны в [docs/02-architecture/adr.md](docs/02-architecture/adr.md):

| ADR | Решение |
|---|---|
| ADR-001 | Клиент-серверная архитектура для мобильной траектории |
| ADR-002 | Использование PCMEF как единой архитектурной основы |
| ADR-003 | Локальное кэширование через Room |
| ADR-004 | JWT + Spring Security для аутентификации |
| ADR-005 | Retrofit + OkHttp для REST-взаимодействия |

## Проверка проекта

Android-клиент:

```bash
cd app
./gradlew :app:assembleDebug
```

Backend:

```bash
cd server
mvn test jacoco:report
```

Фактический результат backend-проверки:

| Метрика | Значение |
|---|---:|
| Tests run | 19 |
| Failures | 0 |
| Errors | 0 |
| Line coverage | 64.95% |
| Instruction coverage | 69.34% |
| Branch coverage | 45.24% |
| Complexity coverage | 52.35% |

## Статистика разработки

### Git метрики

| Метрика | Значение |
|---|---|
| Репозиторий |  |
| Покрытие backend-тестами | 64.95% line coverage |
| Скриншоты GitHub Insights | Добавляются перед финальной сдачей в `docs/images/git-stats-*.png` |

### Метрики проекта

| Метрика | Значение |
|---|---:|
| Основных экранов Android | 9 |
| REST endpoints | 20+ |
| Архитектурных слоёв Android | 5 |
| Архитектурных слоёв backend | 5 |
| PNG-диаграмм | 22 |
| Документационных разделов | 14 |

## Итоговые материалы для сдачи

| Файл | Назначение |
|---|---|
| [docs/13-final-report/explanatory-note-ladushki.docx](docs/13-final-report/explanatory-note-ladushki.docx) | Пояснительная записка |
| [docs/04-detailed-design/plantuml-diagrams.puml](docs/04-detailed-design/plantuml-diagrams.puml) | PlantUML-код диаграмм |
| [docs/03-tables/course-tables.md](docs/03-tables/course-tables.md) | Таблицы для отчёта |
| [docs/images/](docs/images/) | PNG-диаграммы |
| [app/](app/) | Android Studio проект |
| [server/](server/) | Spring Boot backend |

## Лицензия

MIT License. Подробности в файле [LICENSE](LICENSE).

## Полезные ссылки

- [Репозиторий проекта](https://github.com/lookitsssonya/course-project)
- [Документация](docs/)
- [Android-клиент](app/)
- [Spring Boot backend](server/)
- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [API endpoints](docs/09-api/openapi-endpoints.md)
- [Галерея диаграмм](docs/images/README.md)
