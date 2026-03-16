# Patrocl — мобильное приложение официанта

Мобильное приложение официанта для ресторанов, интегрированное с POS-системой R-Keeper.

Проект разработан как часть экосистемы **мобильного официанта**, которая включает:
- backend микросервисную платформу
- мобильный клиент для официантов
- административную панель

📦 Backend: https://github.com/dundunts/TurterApp-back

---

# 📱 О проекте

Patrocl — мобильное приложение, позволяющее официантам:

- просматривать столы и заказы
- создавать и редактировать заказы
- добавлять блюда
- отправлять заказы в POS систему
- работать быстрее и удобнее, чем в стандартном интерфейсе R-Keeper

Основная цель проекта — **улучшить UX официантов и ускорить обслуживание гостей**.

---

# 🏗 Архитектура приложения

Проект построен на **Compose Multiplatform** и поддерживает:

- Android
- iOS

Основные архитектурные принципы:

- Clean Architecture
- MVVM
- реактивное взаимодействие с backend API

Слои приложения:
- data
- di
- domain
- presentation
- ui
- utils

---

# ⚙️ Технологический стек

### Основные технологии

- Kotlin
- Compose Multiplatform
- Voyager (navigation)
- Ktor Client
- Koin (DI)

### Работа с данными

- MongoDB Realm
- Multiplatform Settings

### Авторизация

- OIDC
- Keycloak

---

# 🔗 Интеграция с backend

Приложение работает с микросервисной платформой:

- organization-service
- order-service
- source-service
- stoplist-service
- Keycloak (авторизация)

Backend реализован на:

- Spring Boot
- Spring WebFlux
- PostgreSQL
- MongoDB
- Redis
- Kafka

📦 Репозиторий backend:  
https://github.com/dundunts/TurterApp-back

---

# 🚀 Возможности

- управление заказами
- работа со столами
- добавление блюд
- синхронизация с POS системой
- авторизация через OIDC
- офлайн-кеширование данных

---

# 👨‍💻 Автор

Даниил Коновалов  
Java / Kotlin Backend Developer

- Telegram: @turterDun
- Email: DunDunTs@yandex.ru