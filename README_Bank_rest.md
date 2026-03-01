# Система управления банковскими картами

Spring Boot приложение для управления банковскими картами с JWT аутентификацией, ролевой моделью и управлением
пользователями. Предусмотрены 2 роли - ADMIN и USER.

Аутентификация - получение JWT токена post-запросом на http://localhost:8080/api/auth/login 
с телом запроса {"username":"admin","password":"admin"}

Полученный токен действует 1 час и используется (Authorization: Bearer ...) для всех запросов

Пользователи:
Получить всех пользователей
GET /api/users

Получить пользователя по ID
GET /api/users/{id}

Создать нового пользователя
Post /api/users

Обновить пользователя
PUT /api/users/{id}

Удалить пользователя (каскадно с картами)
DELETE  /api/users/{id}

Карты:
Получить все карты (ADMIN) / свои карты (USER)
GET /api/cards

Создать карту для пользователя (ADMIN)
POST /api/cards?userId={id}

Изменить статус карты
PUT /api/cards/{id}/status?status={status}

Удалить карту
DELETE /api/cards/{id}

Перевод денег между картами
POST /api/cards/transfer
