# Ewallet

Сервис управления электронными кошельками. 
Обратите внимание, есть ещё экспериментальные ветки `balance-update-with-delay` и `reddison-balance-cache` 
можно выжать даже больше 1000 RPS на кошелёк, но работает через кэш, 
я бы не стал делать так для валюты в прод

## Требования

- Docker + Docker Compose (Docker Desktop будет достаточно)
- Java 17 и Maven 3.9.x (опционально, для разработки)

## Настройка

1. Создайте `.env` файл в корне проекта:
    ```properties
    SERVER_PORT=порт_приложения
    PG_PORT=порт_доступа_к_postgress
    DB_NAME=ваше_db_name
    DB_USERNAME=ваш_pg_username
    DB_PASSWORD=ваш_pg_password
    ```

2. Быстрый запуск для с `docker-compose` (Docker-образ будет собран из `Dockerfile`, если отсутствует):
    ```bash
    docker compose up --wait -d
    ```

## Swagger UI

После запуска сервиса доступен Swagger UI по адресу (не забудьте подставить свои host и port): <http://localhost:8080/swagger-ui/index.html>

## Актуатор

После запуска сервиса доступен actuator по адресу (не забудьте подставить свои host и port): <http://localhost:8080/actuator>

## Нагрузочный тест
![img.png](load-test.png)
Дополнительные параметры можно настраивать без пересборки контейнеров,
добавлением необходимых переменных окружения в `docker-compose.yaml`, см. `application.yaml`
и конфигурированием в самом `docker-compose.yaml`.