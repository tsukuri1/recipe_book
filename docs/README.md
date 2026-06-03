# Документация курсового проекта «Ладушки»

Вся документация по курсовому проекту хранится в папке `docs/` и оформлена по структуре учебного репозитория: каждый этап вынесен в отдельный каталог, изображения лежат в `docs/images/`, а ключевые артефакты доступны по прямым ссылкам.

## Структура документации

| Папка | Артефакты |
|---|---|
| [00-project-charter/](00-project-charter/) | [Паспорт проекта](00-project-charter/project-charter.md)<br>[IDEF0 контекст](00-project-charter/context-diagram.md)<br>[BUC диаграмма](00-project-charter/buc-diagram.md)<br>[Модель бизнес-классов](00-project-charter/business-class-model.md)<br>[Матрица стейкхолдеров](00-project-charter/stakeholder-matrix.md)<br>[SWOT-анализ](00-project-charter/swot-analysis.md)<br>[ROI](00-project-charter/roi-calculation.md)<br>[Глоссарий](00-project-charter/glossary.md) |
| [01-requirements/](01-requirements/) | [Use Case диаграмма](01-requirements/use-case-diagram.md)<br>[Спецификация прецедентов](01-requirements/use-case-specifications.md)<br>[Domain Model](01-requirements/domain-model.md)<br>[Требования](01-requirements/requirements.md)<br>[Трассировка требований](01-requirements/requirements-tracing.md)<br>[Расширенный глоссарий](01-requirements/expanded-glossary.md) |
| [02-architecture/](02-architecture/) | [PCMEF диаграмма](02-architecture/pcmef-diagram.md)<br>[Адаптация PCMEF для mobile](02-architecture/pcmef-mobile.md)<br>[ADR](02-architecture/adr.md)<br>[Интерфейсы](02-architecture/interfaces.md)<br>[Диаграмма зависимостей](02-architecture/dependency-diagram.md) |
| [03-database/](03-database/) | [ER-диаграмма](03-database/er-diagram.md)<br>[DDL-скрипт](03-database/ddl.sql)<br>[ORM-стратегия](03-database/orm.md) |
| [03-tables/](03-tables/) | [Таблицы для пояснительной записки](03-tables/course-tables.md) |
| [04-detailed-design/](04-detailed-design/) | [Диаграммы последовательности](04-detailed-design/sequence-diagrams.md)<br>[Спецификации методов](04-detailed-design/methods-specifications.md)<br>[Диаграмма классов проектирования](04-detailed-design/design-classes-diagram.md)<br>[PlantUML-исходники](04-detailed-design/plantuml-diagrams.puml) |
| [05-implementation/](05-implementation/) | [Структура проекта](05-implementation/code-structure.md)<br>[Реализация слоёв](05-implementation/implementation-layers.md)<br>[Паттерны проектирования](05-implementation/design-patterns.md) |
| [06-testing/](06-testing/) | [Тест-план](06-testing/test-plan.md)<br>[Отчёт JaCoCo](06-testing/jacoco-report.md) |
| [07-refactoring/](07-refactoring/) | [Запахи кода](07-refactoring/code-smells.md)<br>[Data Mapper](07-refactoring/data-mapper.md)<br>[Identity Map](07-refactoring/identity-map.md)<br>[Обновлённые тесты](07-refactoring/updated-tests.md) |
| [08-ui/](08-ui/) | [UI-концепция](08-ui/ui-concept.md)<br>[Скриншоты и навигация](08-ui/screenshots.md) |
| [09-api/](09-api/) | [REST endpoints](09-api/openapi-endpoints.md)<br>[Проверка API](09-api/api-test.md) |
| [10-deployment/](10-deployment/) | [Развёртывание и администрирование](10-deployment/README.md) |
| [11-guides/](11-guides/) | [Техническое задание](11-guides/technical-specification.md)<br>[Руководство пользователя](11-guides/user-guide.md)<br>[Руководство администратора](11-guides/admin-guide.md) |
| [12-project-management/](12-project-management/) | [WBS](12-project-management/wbs.md)<br>[Диаграмма Ганта](12-project-management/gantt.md)<br>[Оценка COCOMO](12-project-management/cocomo.md) |
| [13-final-report/](13-final-report/) | [Пояснительная записка](13-final-report/explanatory-note-ladushki.docx)<br>[Описание итоговых материалов](13-final-report/README.md) |
| [images/](images/) | [Галерея диаграмм](images/README.md) |

## Соответствие методичке

| Требование | Где раскрыто |
|---|---|
| Инициация и бизнес-анализ | [00-project-charter/](00-project-charter/) |
| Требования и Use Case | [01-requirements/](01-requirements/) |
| Архитектура PCMEF | [02-architecture/](02-architecture/) |
| Проектирование БД | [03-database/](03-database/) |
| Детальное проектирование | [04-detailed-design/](04-detailed-design/) |
| Реализация ядра | [05-implementation/](05-implementation/) |
| Тестирование и покрытие | [06-testing/](06-testing/) |
| Рефакторинг | [07-refactoring/](07-refactoring/) |
| UI мобильного приложения | [08-ui/](08-ui/) |
| REST API и OpenAPI | [09-api/](09-api/) |
| Развёртывание | [10-deployment/](10-deployment/) |
| Руководства | [11-guides/](11-guides/) |
| Управление проектом | [12-project-management/](12-project-management/) |
| Пояснительная записка | [13-final-report/](13-final-report/) |

## Диаграммы

Все изображения лежат в [images/](images/) и имеют GitHub-friendly имена.

| N | Диаграмма | Файл |
|---|---|---|
| 1 | Use Case Diagram | [01-use-case-diagram.png](images/01-use-case-diagram.png) |
| 2 | Domain Model | [02-domain-model.png](images/02-domain-model.png) |
| 3 | ER Diagram | [03-er-diagram.png](images/03-er-diagram.png) |
| 4 | Activity: добавление рецепта | [04-activity-add-recipe.png](images/04-activity-add-recipe.png) |
| 5 | Activity: список покупок | [05-activity-shopping-list.png](images/05-activity-shopping-list.png) |
| 6 | PCMEF Architecture | [06-pcmef-architecture.png](images/06-pcmef-architecture.png) |
| 7 | Component Diagram | [07-component-diagram.png](images/07-component-diagram.png) |
| 8 | Deployment Diagram | [08-deployment-diagram.png](images/08-deployment-diagram.png) |
| 9 | Sequence: login | [09-login-sequence.png](images/09-login-sequence.png) |
| 10 | Sequence: добавление и модерация рецепта | [10-add-and-moderate-recipe-sequence.png](images/10-add-and-moderate-recipe-sequence.png) |
| 11 | Sequence: список покупок | [11-shopping-list-sequence.png](images/11-shopping-list-sequence.png) |
| 12 | Sequence: поиск | [12-search-sequence.png](images/12-search-sequence.png) |
| 13 | Sequence: смена аватара | [13-change-avatar-sequence.png](images/13-change-avatar-sequence.png) |
| 14 | Sequence: сохранение настроек | [14-save-settings-sequence.png](images/14-save-settings-sequence.png) |
| 15 | State Diagram: рецепт | [15-recipe-state-diagram.png](images/15-recipe-state-diagram.png) |
| 16 | Local Storage Diagram | [16-local-storage-diagram.png](images/16-local-storage-diagram.png) |
| 17 | DTO Mapping Diagram | [17-dto-mapping-diagram.png](images/17-dto-mapping-diagram.png) |
| 18 | Sequence: админ-модерация | [18-admin-moderation-sequence.png](images/18-admin-moderation-sequence.png) |
| 19 | Testing Scheme | [19-testing-scheme.png](images/19-testing-scheme.png) |
| 20 | REST Controllers Diagram | [20-rest-controllers-diagram.png](images/20-rest-controllers-diagram.png) |
| 21 | Navigation Diagram | [21-navigation-diagram.png](images/21-navigation-diagram.png) |
| 22 | Android Layers Diagram | [22-android-layers-diagram.png](images/22-android-layers-diagram.png) |

## Быстрый предпросмотр

### Use Case Diagram

![Use Case Diagram](images/01-use-case-diagram.png)

### PCMEF Architecture

![PCMEF Architecture](images/06-pcmef-architecture.png)

### Navigation Diagram

![Navigation Diagram](images/21-navigation-diagram.png)

## Связанные разделы

- [Корневой README](../README.md)
- [Android-клиент](../app/README.md)
- [Spring Boot backend](../server/README.md)
- [Итоговая пояснительная записка](13-final-report/explanatory-note-ladushki.docx)
