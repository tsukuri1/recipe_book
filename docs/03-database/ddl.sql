-- DDL-скрипт для учебной версии backend «Ладушки».
-- PostgreSQL используется как основная серверная база данных.
-- Hibernate может создавать таблицы автоматически, но DDL нужен для пояснительной записки
-- и демонстрации структуры данных на этапе проектирования БД.

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'USER'
);

CREATE TABLE recipe (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(180) NOT NULL,
    description TEXT,
    image_uri TEXT,
    cooking_time_minutes INTEGER NOT NULL DEFAULT 0,
    portions INTEGER NOT NULL DEFAULT 1,
    calories INTEGER NOT NULL DEFAULT 0,
    protein INTEGER NOT NULL DEFAULT 0,
    fat INTEGER NOT NULL DEFAULT 0,
    carbs INTEGER NOT NULL DEFAULT 0,
    category VARCHAR(40),
    difficulty VARCHAR(40),
    status VARCHAR(40) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE recipe_ingredients (
    recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    amount VARCHAR(40),
    unit VARCHAR(40)
);

CREATE TABLE recipe_steps (
    recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
    step_order INTEGER NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE shopping_list_item (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    checked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_recipe_title ON recipe(title);
CREATE INDEX idx_recipe_category ON recipe(category);
CREATE INDEX idx_recipe_status ON recipe(status);

-- Пояснение:
-- 1. app_user хранит пользователей и роли USER/ADMIN.
-- 2. recipe хранит основную карточку рецепта и статус модерации.
-- 3. recipe_ingredients и recipe_steps являются зависимыми таблицами рецепта.
-- 4. shopping_list_item хранит чек-лист покупок.
-- 5. Индексы ускоряют поиск по названию, категории и статусу модерации.
