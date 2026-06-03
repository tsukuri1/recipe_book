# ADR

![Deployment Diagram](../images/08-deployment-diagram.png)

| ADR | Решение | Причина |
|---|---|---|
| ADR-001 | Android Native + Jetpack Compose | Соответствие мобильной траектории |
| ADR-002 | Spring Boot backend | Удобный REST API и JPA |
| ADR-003 | PCMEF/MVVM | Четкое разделение ответственности |
| ADR-004 | Room + SharedPreferences | Локальное хранение рецептов и профиля |
| ADR-005 | Retrofit | Стандартная работа с REST API в Android |

## ADR-001. Android Native + Jetpack Compose

Выбрана нативная Android-разработка на Kotlin, потому что методичка допускает Android Native как основной вариант мобильной траектории. Jetpack Compose позволяет описывать интерфейс декларативно и удобно связывать его с состоянием `StateFlow` из `RecipeViewModel`.

## ADR-002. Spring Boot backend

Spring Boot выбран из-за готовой экосистемы для REST API, Spring Data JPA, Spring Security и OpenAPI. Это снижает риск ручной реализации инфраструктуры и позволяет сосредоточиться на предметной области.

## ADR-003. PCMEF + MVVM

На Android используется MVVM, но внутри проекта он сопоставлен с PCMEF. ViewModel выполняет роль Control, Interactor — Mediator, Repository — Foundation. Такой подход сохраняет требования методички и остаётся естественным для Android-разработки.

## ADR-004. Room и SharedPreferences

Room используется для структурированных данных рецептов и покупок, а SharedPreferences — для простых пользовательских данных, таких как имя, аватар и JWT. Разделение снижает сложность и соответствует типу данных.

## ADR-005. JWT + Spring Security

JWT выбран для stateless-аутентификации между мобильным клиентом и backend. Это удобно для REST API: после входа клиент добавляет токен в заголовок `Authorization`, а сервер проверяет подпись и роль пользователя.

## Последствия решений

Архитектура становится достаточно модульной для тестирования. Backend-сервисы тестируются без запуска Android, а клиент может работать с fake-репозиторием для демонстрации UI без сервера.
