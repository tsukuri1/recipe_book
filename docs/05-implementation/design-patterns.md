# Использованные паттерны

![DTO Mapping Diagram](../images/17-dto-mapping-diagram.png)

| Паттерн | Где используется |
|---|---|
| Repository | `RecipeRepository` |
| Data Mapper | `DtoMappers.kt` |
| MVVM | Compose + ViewModel + StateFlow |
| Facade | `RecipeInteractor` для сценариев приложения |

## Repository

Паттерн Repository отделяет источник данных от бизнес-логики. UI и Interactor работают с интерфейсом `RecipeRepository`, а конкретная реализация может быть fake, network+cache или полностью локальной.

## Data Mapper

DTO, cache entities и доменные модели имеют разные задачи. DTO отражает формат REST API, cache entity отражает формат Room, а доменная модель удобна для UI и бизнес-логики. `DtoMappers.kt` отделяет эти уровни и уменьшает связанность.

## MVVM

MVVM применяется в Android-клиенте. Compose-экраны подписываются на `RecipeUiState`, ViewModel меняет состояние, а пользовательские действия вызывают методы ViewModel. Это уменьшает количество ручного управления UI.

## Facade / Interactor

`RecipeInteractor` скрывает детали репозитория и предоставляет сценарии высокого уровня. Это делает ViewModel компактнее и ближе к Control-слою PCMEF.

## Dependency Inversion

Зависимость от интерфейса `RecipeRepository` позволяет заменить реализацию без переписывания UI. Это соответствует SOLID и помогает тестировать бизнес-сценарии.
