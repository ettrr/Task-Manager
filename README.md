## Task Manager

Web приложение для составления оптимального расписания задач

на основе жадного алгоритма:
https://neerc.ifmo.ru/wiki/index.php?title=1sumu

Для начала использования приложения перейдите по адресу:
``http://localhost:8080/api/register``

Используемые технологии:

- ``cats``, для работы с эффектами
- ``sttp-client4``, http сервер
- ``sttp-tapir``, описание endpoint
- ``doobie``, работа с базой данных
- ``tofu``, логирование
- ``flyway``, миграция

## 1. Запуск приложения

Для первоначального запуска приложения выполните следующую команду:

```bash
docker-compose up --build
```

## 2. Создание базы данных вручную

Если база данных не была создана автоматически, выполните следующие шаги:

Подключитесь к контейнеру с PostgreSQL:

```bash
docker exec -it postgres_db psql -U postgres
```

Создайте базу данных:

```bash
CREATE DATABASE my_database;
```

Пересоберите приложение:

```bash
docker-compose down
docker-compose up --build
```

## 4. Дальнейший запуск

Для дальнейшего запуска приложения используйте команду:

```bash
docker-compose up
```
# Task-Planner
