# Changelog

Все заметные изменения в этом проекте будут документироваться в этом файле.

## [Итерация 1] - 17-11-2025

### Добавлено
- Инициализация Spring Boot 3.5.7 проекта
- Базовая структура пакетов для монолитной backend-архитектуры
- Docker Compose конфигурация для PostgreSQL и Redis
- Конфигурация SpringDoc OpenAPI для автоматической документации
- Настройка Spring Data JPA + Hibernate
- Базовая конфигурация Spring Security

### Конфигурация
- Порт приложения: 8080
- База данных: PostgreSQL 15 на порту 15432
- Кэширование: Redis на порту 16379
- Система сборки: Gradle
- Java версия: 21

### Технические детали
- Созданы основные пакеты: config, controller, dto, entity, repository, service
- Добавлена автоматическая генерация пароля для Spring Security
- Настроена интерактивная документация API через Swagger UI

### Инициализация
- Создан каркас проекта "Платформа для интернет-магазинов"
- Определен стек технологий: Java 21, Spring Boot 3.5.7, PostgreSQL, Redis
- Настроена среда разработки

## [Итерация 2] - 17-11-2025

### Добавлено
- Модуль авторизации с OTP через email и JWT-токены (access + refresh cookie)
- Контроллер `AuthController` с методами `/api/v1/auth/login` и `/api/v1/auth/confirm`
- Конфигурация безопасности: JWT фильтр, кастомные обработчики ошибок, схемы OpenAPI
- Реализация сущности `User`, репозитория и сервисов для OTP, токенов и пользователей
- Хранение OTP в Redis (и in-memory реализация для профиля `test`)
- Глобальный обработчик ошибок и DTO для ответов/ошибок

### Конфигурация
- Новые настройки `security.jwt` и `security.otp` в `application.yml`
- Профиль `test` использует H2 и in-memory OTP хранилище

### Тесты
- Unit-тесты `AuthService`, `OtpService`, `JwtTokenService`
- Обновлен `StorePlatformApplicationTests` с профилем `test`