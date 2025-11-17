# Платформа для создания интернет-магазинов

## Документация API
Доступна по ссылке http://localhost:8080/swagger-ui.html

## Модуль авторизации

- `POST /api/v1/auth/login` — принимает `email`, генерирует OTP и отправляет его (в dev-режиме отображается в логах).
- `POST /api/v1/auth/confirm` — принимает `email` и `otp`, выдает access token (в ответе) и refresh token (в http-only cookie).

### Конфигурация

| Переменная | Описание |
| --- | --- |
| `JWT_SECRET` | Секрет подписи JWT (по умолчанию dev-значение, обязательно переопределить для prod). |

Остальные параметры управляются через `application.yml` (`security.jwt`, `security.otp`).

### Требования к среде

- PostgreSQL 15 (`docker-compose up postgres`)
- Redis 7 (`docker-compose up redis`)

Запуск:

```bash
./gradlew bootRun
```
