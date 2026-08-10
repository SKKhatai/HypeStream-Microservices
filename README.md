# 🔥 HypeStream — High-Concurrency Sneaker Drop Microservices

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-Event_Streaming-231F20?style=for-the-badge&logo=apachekafka)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker)
![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=for-the-badge&logo=redis)

**A production-grade distributed e-commerce backend solving the three core engineering problems of flash sales — system crashes, data inconsistency, and overselling.**

[View Architecture](#-architecture) · [Tech Stack](#-tech-stack) · [Getting Started](#-getting-started) · [API Reference](#-api-reference) · [Roadmap](#-roadmap)

</div>

---

## 🎯 Problem Statement

Have you ever tried buying a limited sneaker on Nike's drop day and the website crashed? Or clicked "Buy" first but someone else got the last pair?

These are not just bad user experiences — they are **real distributed systems engineering failures**:

| Problem | What Goes Wrong | HypeStream's Solution |
| :--- | :--- | :--- |
| **Traffic Spike** | Single server crashes under 10,000 simultaneous users | Microservices — each service scales independently |
| **Data Inconsistency** | Order placed but stock never deducted (or deducted twice) | Choreographed SAGA Pattern over Apache Kafka |
| **Overselling** | 500 users buy the last pair simultaneously | Optimistic Locking with `@Version` *(In Progress)* |
| **Cascading Failure** | Payment service crash takes down the entire system | Circuit Breaker with Resilience4j *(In Progress)* |

---

## 🏗️ Architecture

```
Client (Mobile / Web)
         │
         ▼
┌─────────────────────┐
│     API Gateway     │  ← Port 8080 — Single entry point for all requests
│  (Spring Cloud GW)  │    Handles routing, load balancing, and (future) JWT security
└─────────────────────┘
         │
         ▼
┌─────────────────────┐
│   Eureka Server     │  ← Port 8761 — Dynamic service registry / phonebook
│  (Service Registry) │    Services discover each other by name, not hardcoded IPs
└─────────────────────┘
         │
         ├─────────────────────────┐
         ▼                         ▼
┌──────────────────┐     ┌──────────────────┐
│ Inventory Service│     │  Order Service   │
│   Port: 8082     │     │   Port: 8081     │
│ DB: hypestream   │     │ DB: hypestream   │
│    _inventory    │     │    _orders       │
└──────────────────┘     └──────────────────┘
         ▲                         │
         │    Apache Kafka         │
         └──── (Async Events) ─────┘
              topic: order-placed
              topic: order-stock-status
```

### The SAGA Pattern Flow

```
1. POST /api/v1/orders { productId: 1, quantity: 2 }
              │
              ▼
2. Order Service → Saves order as [PENDING] in MySQL
              │
              ▼ Kafka: "order-placed"
              │  { orderId: 5, productId: 1, quantity: 2 }
              │
              ▼
3. Inventory Service → Checks stock
     ┌─── Stock OK ───────────────────────────────────┐
     │    Deducts stock in MySQL                      │
     │    Kafka: "order-stock-status"                 │
     │    { orderId: 5, status: "RESERVED" }          │
     └────────────────────────────────────────────────┘
     ┌─── Out of Stock ───────────────────────────────┐
     │    Kafka: "order-stock-status"                 │
     │    { orderId: 5, status: "FAILED" }            │
     └────────────────────────────────────────────────┘
              │
              ▼
4. Order Service → Updates order status
     RESERVED → [CONFIRMED] ✅
     FAILED   → [CANCELLED] ❌
```

---

## 🛠️ Tech Stack

| Category | Technology | Purpose |
| :--- | :--- | :--- |
| **Language** | Java 21 | Core application language |
| **Framework** | Spring Boot 3.x | Microservice development |
| **Service Discovery** | Netflix Eureka | Dynamic IP-based service routing |
| **API Gateway** | Spring Cloud Gateway | Centralized routing & security |
| **Message Broker** | Apache Kafka | Async event-driven communication |
| **Database** | MySQL 8.0 | Persistent data storage (per service) |
| **Cache** | Redis | High-speed catalog caching  |
| **Containerization** | Docker Compose | Local infrastructure orchestration |
| **Build Tool** | Gradle Multi-Project | Unified monorepo build system |
| **Utilities** | Lombok | Boilerplate-free Java code |

---

## 📁 Project Structure

```
hypestream/
├── eureka-server/                    # Service Registry (Port 8761)
├── api-gateway/                      # API Gateway (Port 8080)
├── inventory-service/                # Inventory Management (Port 8082)
│   └── src/main/java/com/hypestream/inventory/
│       ├── model/Product.java            # JPA entity → products table
│       ├── repository/                   # MySQL queries via JPA
│       ├── controller/ProductController  # GET /api/v1/products
│       ├── consumer/OrderConsumer        # Kafka: reads "order-placed"
│       ├── producer/InventoryProducer    # Kafka: writes "order-stock-status"
│       ├── event/                        # Kafka message structures
│       └── seeder/DatabaseSeeder         # Auto-seeds shoe catalog on startup
├── order-service/                    # Order Management (Port 8081)
│   └── src/main/java/com/hypestream/order/
│       ├── model/Order.java              # JPA entity → orders table
│       ├── model/OrderStatus.java        # Enum: PENDING, CONFIRMED, CANCELLED
│       ├── repository/                   # MySQL queries via JPA
│       ├── controller/OrderController    # POST /api/v1/orders
│       ├── producer/OrderProducer        # Kafka: writes "order-placed"
│       ├── consumer/StockStatusConsumer  # Kafka: reads "order-stock-status"
│       └── event/                        # Kafka message structures
├── payment-service/                  # Payment Processing (Planned)
├── docker-compose.yml                # Infrastructure: MySQL, Redis, Kafka, ZooKeeper
└── settings.gradle                   # Gradle multi-project config
```

---

## 🚀 Getting Started

### Prerequisites

Make sure the following are installed on your machine:
- **Java 21** (e.g. Microsoft OpenJDK 21)
- **Docker Desktop** (running)
- **IntelliJ IDEA** (recommended)

### Step 1: Clone the Repository

```bash
git clone https://github.com/SKKhatai/HypeStream-Microservices.git
cd HypeStream-Microservices
```

### Step 2: Start the Infrastructure (Docker)

```bash
docker-compose up -d
```

This starts **4 containers**:
- MySQL on port `3306`
- Redis on port `6379`
- ZooKeeper on port `2181`
- Kafka on port `9092`

### Step 3: Start the Services (in this order)

Start each service from IntelliJ or using Gradle:

```bash
# 1. Start Eureka Server first
cd eureka-server && ./gradlew bootRun

# 2. Start API Gateway
cd api-gateway && ./gradlew bootRun

# 3. Start Inventory Service
cd inventory-service && ./gradlew bootRun

# 4. Start Order Service
cd order-service && ./gradlew bootRun
```

### Step 4: Verify Everything Is Running

Open your browser and visit:
- **Eureka Dashboard:** http://localhost:8761
  - You should see `INVENTORY-SERVICE`, `ORDER-SERVICE`, and `API-GATEWAY` all registered.

---

## 📡 API Reference

All requests go through the **API Gateway on port `8080`**.

### Inventory Service

#### Get All Products (Shoe Catalog)
```http
GET http://localhost:8080/api/v1/products
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Air Jordan 1 Retro High",
    "sku": "AJ1-RETRO-001",
    "price": 150.00,
    "stock": 100
  },
  {
    "id": 2,
    "name": "Nike Dunk Low",
    "sku": "DUNK-LOW-002",
    "price": 110.00,
    "stock": 75
  }
]
```

---

### Order Service

#### Place a New Order
```http
POST http://localhost:8080/api/v1/orders
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "productId": 1,
  "quantity": 2,
  "totalPrice": 300.00,
  "status": "PENDING"
}
```
> ⚡ The status starts as `PENDING`. Within milliseconds, the SAGA pattern kicks in via Kafka and updates it to `CONFIRMED` or `CANCELLED`.

#### Get All Orders
```http
GET http://localhost:8080/api/v1/orders
```

**Response:**
```json
[
  {
    "id": 1,
    "productId": 1,
    "quantity": 2,
    "totalPrice": 300.00,
    "status": "CONFIRMED"
  }
]
```

---

## 🐛 Key Technical Challenges Solved

### 1. Kafka Cross-Service Deserialization (`ClassNotFoundException`)
**Problem:** Spring Kafka embeds the producer's class name (`com.hypestream.order.event.OrderPlacedEvent`) as a header in every message. The consumer in a different service tries to load that class and fails.

**Solution:** Configured consumer to ignore type headers and deserialize directly to local class:
```yaml
spring.json.use.type.headers: "false"
spring.json.value.default.type: "com.hypestream.inventory.event.OrderPlacedEvent"
```

### 2. Eureka `UnknownHostException`
**Problem:** Eureka registered services using the Windows machine hostname instead of the actual network IP, causing routing failures.

**Solution:** Added to all service `application.yml` files:
```yaml
eureka:
  instance:
    prefer-ip-address: true
```

### 3. ZooKeeper JVM Crash on Docker Desktop
**Problem:** Confluent image version `7.3.0` uses an old Java runtime that throws a `NullPointerException` during cgroup v2 parsing on modern Docker Desktop for Windows.

**Solution:** Upgraded both ZooKeeper and Kafka images to version `7.5.0` in `docker-compose.yml`.

### 4. Gradle + Java 21 Build Failure
**Problem:** Old Spring Dependency Management plugin (`1.1.4`) is incompatible with Gradle 8+ on JDK 21.

**Solution:** Upgraded the plugin to version `1.1.6` across all `build.gradle` modules.

---

## 🗺️ Roadmap

| Phase | Feature | Status |
| :--- | :--- | :--- |
| ✅ Phase 1 | Microservices Architecture + Eureka + Gateway | **Completed** |
| ✅ Phase 2 | Inventory Service (CRUD + Seeder) | **Completed** |
| ✅ Phase 3 | Order Service + Kafka Producer | **Completed** |
| ✅ Phase 4 | SAGA Pattern (Kafka Consumer Loop) | **Completed** |
| 🔧 Phase 5 | Redis Caching (Cache-Aside Pattern) | **In Progress** |
| 🔧 Phase 6 | Resilience4j Circuit Breaker | **Planned** |
| 🔧 Phase 7 | Optimistic Locking (Race Condition Fix) | **Planned** |
| 🔧 Phase 8 | JWT Authentication at Gateway | **Planned** |
| 🔧 Phase 9 | AWS Deployment (RDS + ECS + MSK) | **Planned** |

---

## 🎓 Key Concepts Demonstrated

- **Microservices Architecture** — Independent deployment and scaling per service
- **Event-Driven Architecture** — Services communicate via Kafka events, not direct HTTP calls
- **Choreographed SAGA Pattern** — Distributed transaction consistency without 2-phase commit
- **Database-per-Service** — Complete data isolation between microservices
- **API Gateway Pattern** — Centralized routing, security, and cross-cutting concerns
- **Service Discovery** — Dynamic IP resolution using Netflix Eureka
- **Docker Compose Orchestration** — Full local cloud infrastructure setup

---

## 👤 Author

**Sufiyan Khatai**
- GitHub: [@SKKhatai](https://github.com/SKKhatai)

---

<div align="center">
⭐ Star this repository if you found it helpful!
</div>
