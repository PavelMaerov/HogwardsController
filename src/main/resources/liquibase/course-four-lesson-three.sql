-- liquibase formatted sql

-- changeset pavel:1
-- Индекс для поиска по имени студента.
CREATE INDEX student_name_index ON student (name);

-- changeset pavel:2
-- Индекс для поиска по названию и цвету факультета.
CREATE INDEX faculty_name_color_index ON faculty (name, color);
