# Диаграмма зависимостей

![Component Diagram](../images/07-component-diagram.png)

Зависимости направлены сверху вниз: UI обращается к ViewModel, ViewModel к Mediator, Mediator к Repository, Repository к API/Room. Backend повторяет ту же логику через Controllers, Services, Entities и Repositories.

## Правила зависимостей

1. `presentation` не обращается напрямую к Retrofit или Room.
2. `control` управляет состоянием, но не хранит SQL-запросы и HTTP-детали.
3. `mediator` описывает бизнес-сценарии и вызывает Repository.
4. `entity` не зависит от UI, Retrofit или Android-компонентов.
5. `foundation` инкапсулирует API, локальную БД и хранилища.

## Проверка отсутствия циклов

На уровне пакетов зависимости направлены в одну сторону: `presentation -> control -> mediator -> foundation/entity`. На backend аналогично: `control -> mediator -> entity/foundation`. Репозитории не вызывают сервисы, а сервисы не знают о Compose-экранах, поэтому циклические зависимости не возникают.

## Практический эффект

Такое разделение позволяет заменить `FakeRecipeRepository` на `CachedRecipeRepository` без переписывания экранов. UI продолжает работать с тем же ViewModel и Interactor, а источник данных меняется внутри Foundation-слоя.
