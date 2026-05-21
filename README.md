# Flora ERP System

## Про проєкт (Головна цінність)
**Flora ERP System** — це сучасна мікросервісна система управління підприємством (ERP), розроблена для оптимізації процесів у сфері вирощування та продажу рослин. 

Головна цінність цього проєкту полягає не лише у бізнес-логіці, а й у **демонстрації зрілих архітектурних рішень для побудови розподілених систем**. Проєкт підтверджує глибоке розуміння та практичне застосування таких концепцій:
- **Надійна асинхронна комунікація** між мікросервісами за допомогою RabbitMQ та патерну **Transactional Outbox**, що гарантує цілісність даних і відсутність втрати подій (Event-Driven Architecture).
- **Відмовостійкість (Resilience)** завдяки інтеграції Resilience4j (Circuit Breaker, Retry) для безпечної взаємодії між сервісами.
- **Безпека та управління конфігураціями** за допомогою HashiCorp Vault та Spring Cloud Config.
- **Інфраструктура як код (IaC)** — повна контейнеризація баз даних, брокерів повідомлень та самих сервісів за допомогою єдиного Docker Compose файлу.

## Архітектура мікросервісів
Система розподілена на незалежні бізнес-домени та інфраструктурні компоненти:
- **API Gateway (`api-gateway`)**: Єдина точка входу для всіх клієнтських запитів. Відповідає за централізовану маршрутизацію, CORS та захист від перевантажень (Rate Limiting за допомогою Bucket4j).
- **Service Discovery (`eureka-server`)**: Реєстр сервісів (Spring Cloud Netflix Eureka) для їх динамічного виявлення у мережі.
- **Config Server (`config-server`)**: Централізоване управління конфігураційними файлами сервісів із безпечним зберіганням секретів у HashiCorp Vault.
- **Production Service (`production-service`)**: Домен вирощування рослин. Управляє партіями (batches), статусом здоров'я та відстеженням змін (Change Logs).
- **Inventory Service (`inventory-service`)**: Складський облік. Управління доступними залишками та резерваціями рослин під замовлення.
- **Sales Service (`sales-service`)**: Обробка клієнтських замовлень, управління даними клієнтів та агрегація інформації про продажі.

## Стек технологій
- **Мова та фреймворки:** Java 17, Spring Boot, Spring Data JPA, Spring Web (RestClient, WebClient)
- **Хмарна інфраструктура:** Spring Cloud (Gateway, Netflix Eureka, Config Server)
- **Бази даних:** MariaDB (Model-per-service підхід)
- **Міграції БД:** Flyway
- **Асинхронна інтеграція:** RabbitMQ (Spring AMQP)
- **Архітектурні патерни:** Microservices, Event-Driven Architecture (EDA), Transactional Outbox, Saga, API Gateway
- **Безпека та Секрети:** HashiCorp Vault, Spring Security, JWT (Resource Server)
- **DevOps & Контейнеризація:** Docker, Docker Compose
- **Моніторинг (Observability):** Spring Boot Actuator, Prometheus
- **Документація API:** Swagger / OpenAPI 3.0

## Як запустити локально
Увесь проєкт розгортається однією командою завдяки налаштованому середовищу Docker Compose, яке автоматично підніме бази даних MariaDB, RabbitMQ, Vault та всі мікросервіси.

```bash
# 1. Клонувати репозиторій
git clone [https://github.com/VladdHoryn/flora-erp-system.git](https://github.com/VladdHoryn/flora-erp-system.git)
cd flora-erp-system

# 2. Зібрати всі сервіси (артефакти)
mvn clean package -DskipTests

# 3. Підняти інфраструктуру та мікросервіси
docker-compose up --build -d
