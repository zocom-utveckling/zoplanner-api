# 🚀 ZoPlanner Demo Guide

> **Komplett guide för att starta hela ZoPlanner-systemet med Docker**  

---

## 📚 Relaterade Repositories och Dokumentation

### Repositories

| Repository | Beskrivning | Länk |
|-----------|-------------|------|
| **zoplanner-api** | CRUD API mot databasen (Java/Spring Boot) | [GitHub](https://github.com/zocom-utveckling/zoplanner-api) |
| **zoplanner-service** | Backend-tjänst (.NET) | [GitHub](https://github.com/zocom-utveckling/zoplanner-service) |
| **zoplanner-frontend** | Användargränssnitt (Frontend) | [GitHub](https://github.com/zocom-utveckling/zoplanner-frontend) |

### Befintlig Dokumentation

- 📖 [README.md](./README.md) - Detaljerad guide för utvecklare (databas setup, IntelliJ, Swagger, CI/CD)
- 🐳 [DOCKER_INSTRUCTIONS.md](./webapi/DOCKER_INSTRUCTIONS.md) - Detaljerade Docker-kommandon och nätverksarkitektur

---

## 🏗️ Systemöversikt

ZoPlanner består av **4 Docker-containrar** som kommunicerar med varandra:

```
┌─────────────────────────────────────────────────────────────────┐
│              Docker Network: zoplanner (bridge)                  │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │ PostgreSQL   │  │ Spring Boot  │  │  .NET API    │          │
│  │ (Databas)    │◄─┤ (zoplanner-  │◄─┤ (zoplanner-  │          │
│  │              │  │  api)        │  │  service)    │          │
│  │ Port: 5432   │  │ Port: 8080   │  │ Port: 5027   │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│                           ▲                  ▲                  │
│                           │                  │                  │
│                    ┌──────────────┐          │                  │
│                    │   Frontend   │──────────┘                  │
│                    │ (React/Vue)  │                             │
│                    │ Port: 3000   │                             │
│                    └──────────────┘                             │
└─────────────────────────────────────────────────────────────────┘
```

| Container | Beskrivning | Port |
|-----------|-------------|------|
| **zoplanner-database** | PostgreSQL databas | `localhost:5432` |
| **zoplanner** | Spring Boot API (Java) | `localhost:8080` |
| **zoplanner-dotnet** | .NET Service | `localhost:5027` |
| **zoplanner-frontend** | Frontend applikation | `localhost:3000` |

---

## ✅ Förutsättningar - Vad behöver jag installera?

Innan du börjar, installera följande program:

### 1. Docker Desktop (KRÄVS)
- 📥 **Ladda ner:** [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)
- ✅ Installera och starta Docker Desktop
- 🔍 Verifiera installation: Öppna terminal och kör `docker --version`
- ☝️ Man behöver skapa ett konto eller logga in

### 2. Git (KRÄVS)
- 📥 **Ladda ner:** [https://git-scm.com/downloads](https://git-scm.com/downloads)
- ✅ Installera Git
- 🔍 Verifiera installation: Öppna terminal och kör `git --version`

### 3. Postgres 18 (KRÄVS)
- 📥 **Ladda ner:** [https://www.postgresql.org/download/](https://www.postgresql.org/download)
- ✅ Installera Postgres Server och pgAdmin 4
<img width="571" height="452" alt="image" src="https://github.com/user-attachments/assets/1cfc99e1-689e-46ab-b99c-142a95d170a5" />

- 🖊️Skriv ner och kom ihåg ditt användarnamn och lösenord. Standardanvändarnamnet är *postgres* och vi använder lösenordet *test123* för projektet
<img width="585" height="551" alt="image" src="https://github.com/user-attachments/assets/7b8ef6b6-ab19-47e5-b51b-bff9e9f903e0" />

- 🔑Man måste också känna till porten för att databasen – det är bättre om den är densamma för alla: 5432
<img width="607" height="551" alt="image" src="https://github.com/user-attachments/assets/6d2b3e2f-51db-4590-a95e-33698426e1b7" />

### 4. Skapa databas (KRÄVS)

För tillfället rekommenderar vi att ni använder den gamla versionen av databasen, eftersom det är den som fungerar med koden i `dev`. Vi föreslår att ni använder `zoplanner-old` som namn på den gamla databasen för att undvika förvirring i framtiden. Servern namn kan vara `zoplanner`, eftersom man kan ha flera databas på samma servern. Vi meddelar er när systemet har byggts om för att fungera med den nya databasen. Då måste man upprepa detta steg igen.

Skript för att skapa gammal version ov databas (📢beskrivningen i länken i punkt 7 använder ett annat skript för den nya versionen av databasen)

```
-- Create customers table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    city VARCHAR(255)
);

-- Create classes table
CREATE TABLE classes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    customer_id BIGINT REFERENCES customers(id)
);

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    city VARCHAR(255),
    name VARCHAR(255)
);

-- Create assignments table
CREATE TABLE assignments (
    id BIGSERIAL PRIMARY KEY,
    course_name VARCHAR(255),
    consultant_id BIGINT REFERENCES users(id),
    date_start DATE,
    date_end DATE,
    class_id BIGINT REFERENCES classes(id)   
);

-- Create sessions table
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    assignment_id BIGINT REFERENCES assignments(id),
    time_start TIMESTAMP,
    time_end TIMESTAMP
);
```

Följ länken för att hitta instruktioner om hur man skapar en databas via pgAdmin 4: [https://github.com/zocom-utveckling/zoplanner-api?tab=readme-ov-file#skapa-databas](https://github.com/zocom-utveckling/zoplanner-api?tab=readme-ov-file#skapa-databas)

### 5. Skapa databas (valfritt, man kan ange data senare via Swagger, men det tar längre tid)

```
-- ============================
-- CUSTOMERS (Yrkeshögskolor)
-- ============================
INSERT INTO customers (name, city) VALUES
('Nackademin', 'Stockholm'),
('Lexicon YH', 'Göteborg'),
('Jensens YH', 'Malmö'),
('EC Utbildning', 'Stockholm'),
('Medieinstitutet', 'Stockholm');

-- ============================
-- CLASSES (programming tracks)
-- ============================
INSERT INTO classes (name, customer_id) VALUES
('Java Developer 2025', 1),
('Fullstack .NET 2025', 1),
('Systemutvecklare Java', 2),
('Frontendutvecklare React', 3),
('Python Developer 2025', 4),
('DevOps Engineer 2025', 5);

-- ============================
-- USERS (consultants/instructors)
-- ============================
INSERT INTO users (username, password, role, city, name) VALUES
('anna.smith', 'pass', 'CONSULTANT', 'Stockholm', 'Anna Smith'),
('mikael.lund', 'pass', 'CONSULTANT', 'Göteborg', 'Mikael Lund'),
('sara.pettersson', 'pass', 'CONSULTANT', 'Malmö', 'Sara Pettersson'),
('john.nilsson', 'pass', 'CONSULTANT', 'Stockholm', 'John Nilsson'),
('lisa.karlsson', 'pass', 'CONSULTANT', 'Stockholm', 'Lisa Karlsson');

-- ============================
-- ASSIGNMENTS (courses taught)
-- ============================
INSERT INTO assignments (course_name, consultant_id, date_start, date_end, class_id) VALUES
('Java Basics',                1, '2025-02-01', '2025-03-01', 1),
('Object-Oriented Programming',1, '2025-03-05', '2025-04-20', 1),
('.NET Core Introduction',     2, '2025-02-10', '2025-03-10', 2),
('React Advanced',             3, '2025-01-20', '2025-02-28', 4),
('Python for Data Analysis',   4, '2025-02-01', '2025-03-15', 5),
('CI/CD Pipelines',            5, '2025-02-15', '2025-03-10', 6);

-- ============================
-- SESSIONS (daily schedule)
-- Format: each assignment has multiple teaching days
-- Example time: 09:00–16:00
-- ============================

-- Java Basics (assignment 1) — daily Mon–Fri for 5 days
INSERT INTO sessions (assignment_id, time_start, time_end) VALUES
(1, '2025-02-03 09:00', '2025-02-03 16:00'),
(1, '2025-02-04 09:00', '2025-02-04 16:00'),
(1, '2025-02-05 09:00', '2025-02-05 16:00'),
(1, '2025-02-06 09:00', '2025-02-06 16:00'),
(1, '2025-02-07 09:00', '2025-02-07 16:00');

-- OOP (assignment 2) — 3 sessions per week
INSERT INTO sessions (assignment_id, time_start, time_end) VALUES
(2, '2025-03-05 09:00', '2025-03-05 16:00'),
(2, '2025-03-07 09:00', '2025-03-07 16:00'),
(2, '2025-03-10 09:00', '2025-03-10 16:00'),
(2, '2025-03-12 09:00', '2025-03-12 16:00'),
(2, '2025-03-14 09:00', '2025-03-14 16:00');

-- .NET Core (assignment 3) — daily for 1 week
INSERT INTO sessions (assignment_id, time_start, time_end) VALUES
(3, '2025-02-10 09:00', '2025-02-10 16:00'),
(3, '2025-02-11 09:00', '2025-02-11 16:00'),
(3, '2025-02-12 09:00', '2025-02-12 16:00'),
(3, '2025-02-13 09:00', '2025-02-13 16:00'),
(3, '2025-02-14 09:00', '2025-02-14 16:00');

-- React Advanced (assignment 4) — 2 times per week
INSERT INTO sessions (assignment_id, time_start, time_end) VALUES
(4, '2025-01-21 09:00', '2025-01-21 16:00'),
(4, '2025-01-23 09:00', '2025-01-23 16:00'),
(4, '2025-01-28 09:00', '2025-01-28 16:00'),
(4, '2025-01-30 09:00', '2025-01-30 16:00'),
(4, '2025-02-04 09:00', '2025-02-04 16:00'),
(4, '2025-02-06 09:00', '2025-02-06 16:00');

-- Python for Data Analysis (assignment 5) — Mon/Wed/Fri
INSERT INTO sessions (assignment_id, time_start, time_end) VALUES
(5, '2025-02-03 09:00', '2025-02-03 16:00'),
(5, '2025-02-05 09:00', '2025-02-05 16:00'),
(5, '2025-02-07 09:00', '2025-02-07 16:00'),
(5, '2025-02-10 09:00', '2025-02-10 16:00'),
(5, '2025-02-12 09:00', '2025-02-12 16:00'),
(5, '2025-02-14 09:00', '2025-02-14 16:00');

-- CI/CD Pipelines (assignment 6) — daily two weeks
INSERT INTO sessions (assignment_id, time_start, time_end) VALUES
(6, '2025-02-17 09:00', '2025-02-17 16:00'),
(6, '2025-02-18 09:00', '2025-02-18 16:00'),
(6, '2025-02-19 09:00', '2025-02-19 16:00'),
(6, '2025-02-20 09:00', '2025-02-20 16:00'),
(6, '2025-02-21 09:00', '2025-02-21 16:00'),
(6, '2025-02-24 09:00', '2025-02-24 16:00'),
(6, '2025-02-25 09:00', '2025-02-25 16:00'),
(6, '2025-02-26 09:00', '2025-02-26 16:00'),
(6, '2025-02-27 09:00', '2025-02-27 16:00'),
(6, '2025-02-28 09:00', '2025-02-28 16:00');
```
Kontrollera att uppgifterna har fyllts i korrekt med hjälp av förfrågningar som

```
SELECT * FROM table-name;
```

### 6. För utveckling (men man kan starta hela systemet utan det)
- **Java JDK 21+** - Om du vill bygga Spring Boot lokalt
- **Maven** - Om du vill bygga projektet lokalt
- **IntelliJ IDEA** - Rekommenderad IDE för Java-utveckling - våra inskruktioner [https://github.com/zocom-utveckling/zoplanner-api/blob/dev/README.md#hur-man-laddar-ner-och-k%C3%B6r-repot-i-intellij-idea](https://github.com/zocom-utveckling/zoplanner-api/blob/dev/README.md#hur-man-laddar-ner-och-k%C3%B6r-repot-i-intellij-idea)
- **Visual Studio Code** - För .Net och React

---

## 🎯 Snabbstart - Starta systemet (Steg-för-steg)

### Steg 1: Klona alla repositories

Öppna en terminal (CMD, PowerShell, eller Terminal på Mac) och kör:

```bash
# Skapa en mapp för projektet
mkdir ZoPlanner
cd ZoPlanner

# Klona alla tre repositories
# När man kör det här kommandot öppnas ett fönster där man behöver logga in på GitHub. Följ instruktionerna. 
# Man måste logga in med ett konto som har åtkomst till alla repo som man klonar.

git clone https://github.com/zocom-utveckling/zoplanner-api.git
git clone https://github.com/zocom-utveckling/zoplanner-service.git
git clone https://github.com/zocom-utveckling/zoplanner-frontend.git
```

### Steg 2: Skapa miljövariabler (.env fil)

Navigera till `zoplanner-api/webapi` mappen och skapa en fil som heter `.env`:

```bash
cd zoplanner-api/webapi
```

Skapa filen `.env` med följande innehåll (använd Notepad eller VS Code):

```env
HOST=zoplanner-database
PORT=5432
POSTGRES_USER=<your_username>
POSTGRES_PASSWORD=<your_password>
POSTGRES_DB=zoplanner
```

> ⚠️ **VIKTIGT:** Byt ut `<your_username>` och `<your_password>` till dina egna valfria värden. Kommittera ALDRIG denna fil till Git!

### Steg 3: Konfigurera sökvägar i docker-compose.yml

Öppna filen `zoplanner-api/webapi/docker-compose.yml` och uppdatera sökvägarna till .NET-tjänsten och frontend:

```yaml
# Ändra denna rad för .NET-tjänsten:
context: ./path/to/dotnet-service
# Till din faktiska sökväg, t.ex. (om repos ligger i samma mapp):
context: ../../zoplanner-service/zoplannerservice

# Ändra denna rad för frontend:
context: ./path/to/frontend
# Till din faktiska sökväg, t.ex. (om repos ligger i samma mapp):
context: ../../zoplanner-frontend
```

> 💡 **Tips:** Sökvägarna beror på var du har klonat repositories. Om alla tre repos ligger i samma mapp (`ZoPlanner/`), använd `../../` för att gå två nivåer upp från `webapi/`-mappen.

### Steg 4: Bygg Spring Boot JAR-fil (krävs första gången)

```bash
# Se till att du är i zoplanner-api/webapi mappen
cd zoplanner-api/webapi

# Bygg JAR-filen (skippar tester för snabbare build)
mvn clean package -DskipTests
```

> 💡 **Tips:** Om du inte har Maven installerat kan du använda `./mvnw clean package -DskipTests` istället (använder inbyggda Maven Wrapper).

### Steg 5: Starta alla containers

```bash
# Starta alla 4 containers (från zoplanner-api/webapi mappen)
docker-compose up -d --build
```

> 💡 **Alternativt:** Du kan kombinera build och start i ett kommando:  
> `mvn clean package -DskipTests && docker-compose up -d --build`

### Steg 6: Verifiera att allt fungerar

- 🌐 Öppna **Frontend**: [http://localhost:3000](http://localhost:3000)
- 🔌 Öppna **Spring Boot API (Swagger)**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- 🔌 Öppna **.NET Service**: [http://localhost:5027/api/Customer](http://localhost:5027/api/Customer)

---

## 📋 Snabbreferens - Vanliga kommandon

| Åtgärd | Kommando |
|--------|----------|
| **Starta alla containers** | `docker-compose up -d` |
| **Starta med rebuild** | `docker-compose up -d --build` |
| **Stoppa alla containers** | `docker-compose down` |
| **Stoppa och radera data** | `docker-compose down -v` |
| **Se status** | `docker-compose ps` |
| **Se loggar** | `docker-compose logs -f` |
| **Starta om alla** | `docker-compose restart` |

### Starta enskilda containers

```bash
# Endast databasen
docker-compose up -d zoplanner-database

# Endast Spring Boot API
docker-compose up -d app

# Endast .NET Service
docker-compose up -d dotnet-service

# Endast Frontend
docker-compose up -d frontend
```

---

## 🔧 Felsökning

### "Docker is not running"
- ✅ Starta Docker Desktop och vänta tills ikonen blir grön

### "Port already in use"
- ✅ Stäng andra program som använder portarna (5432, 8080, 5027, 3000)
- ✅ Eller kör `docker-compose down` om tidigare containers körs

### "Cannot find .env file"
- ✅ Skapa `.env` filen enligt instruktionerna i Steg 2

### "Build failed"
- ✅ Kontrollera att du har rätt sökvägar i `docker-compose.yml`
- ✅ Kontrollera loggarna med `docker-compose logs`

### Se detaljerade loggar för specifik container
```bash
docker-compose logs -f app          # Spring Boot
docker-compose logs -f dotnet-service  # .NET
docker-compose logs -f zoplanner-database  # Databas
docker-compose logs -f frontend     # Frontend
```

---

## 📖 Mer information

För mer detaljerad information, se:

- **Utvecklarguide:** [README.md](./README.md)
  - IntelliJ IDEA setup
  - Environment variables i IntelliJ
  - Swagger UI användning
  - Databasschema och SQL
  - CI/CD med GitHub Actions

- **Docker-kommandon:** [DOCKER_INSTRUCTIONS.md](./webapi/DOCKER_INSTRUCTIONS.md)
  - Detaljerade Docker-kommandon
  - Nätverksarkitektur
  - Backup och restore
  - Troubleshooting

---

## 🆘 Behöver du hjälp?

Om du stöter på problem:

1. 📖 Läs igenom felsökningsavsnittet ovan
2. 📋 Kontrollera loggarna med `docker-compose logs -f`
3. 💬 Kontakta utvecklingsteamet

---

*Senast uppdaterad: November 2025*
