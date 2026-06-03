# Реализация слоев

![PCMEF Architecture](../images/06-pcmef-architecture.png)

| Слой | Реализация |
|---|---|
| Presentation | Compose screens, cards, bottom bar |
| Control | RecipeViewModel, RecipeUiState |
| Mediator | RecipeInteractor, Spring services |
| Entity | Kotlin data classes, JPA entities |
| Foundation | Repository, Retrofit, Room, JPA repositories |

## Presentation

Presentation-слой состоит из Compose-экранов. Он показывает данные и принимает действия пользователя, но не должен самостоятельно обращаться к REST API или базе данных. Примеры: `CatalogScreen`, `RecipeDetailScreen`, `ShoppingListScreen`, `ProfileScreen`.

## Control

Control-слой управляет состоянием. В Android это `RecipeViewModel` и `RecipeUiState`. На сервере Control представлен REST-контроллерами, которые принимают HTTP-запросы и передают их сервисам.

## Mediator

Mediator-слой содержит бизнес-сценарии. В Android это `RecipeInteractor`, на backend — сервисы. Здесь решается, что делать после добавления рецепта, как менять статус, как обновлять список покупок.

## Entity

Entity-слой содержит модели предметной области. На Android это Kotlin data classes, на backend — JPA-сущности. Сущности не должны зависеть от UI.

## Foundation

Foundation-слой инкапсулирует инфраструктуру: Room, Retrofit, SharedPreferences, Spring Data JPA, PostgreSQL. Этот слой ближе всего к внешним системам и техническим деталям.
