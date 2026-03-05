# 🚀 ZoPlanner: Starta 5 containrar Guide

> **Komplett guide för att starta hela ZoPlanner-systemet med Docker**  

---

## 📚 Relaterade Repositories och Dokumentation

### Repositories

| Repository | Beskrivning | Länk |
|-----------|-------------|------|
| **zoplanner-api** | CRUD API mot databasen (Java/Spring Boot) | [GitHub](https://github.com/zocom-utveckling/zoplanner-api) |
| **zoplanner-service** | Backend-tjänst (.NET) | [GitHub](https://github.com/zocom-utveckling/zoplanner-service) |
| **zoplanner-frontend** | Användargränssnitt (Frontend) | [GitHub](https://github.com/zocom-utveckling/zoplanner-frontend) |
| **zoplanner-notificationservice** | Notifikationsservice (Java) | [GitHub](https://github.com/zocom-utveckling/zoplanner-notificationservice) |

### Befintlig Dokumentation

- 📖 [README.md](./README.md) - Detaljerad guide för utvecklare (databas setup, IntelliJ, Swagger, CI/CD)
- 🐳 [DOCKER_INSTRUCTIONS.md](./webapi/DOCKER_INSTRUCTIONS.md) - Detaljerade Docker-kommandon och nätverksarkitektur
- [HUR KOPPLAS SPRING BOOT OCH >NET](https://github.com/zocom-utveckling/zoplanner-service/blob/dev/README.md)

---

## 🏗️ Systemöversikt

ZoPlanner består av **5 Docker-containrar** som kommunicerar med varandra:

```
┌─────────────────────────────┐
│         Frontend            │
│   (zoplanner-frontend)      │
│   Port: 3000                │
└─────────────┬───────────────┘
              │ beroende av
              ▼
┌─────────────────────────────┐
│       .NET-tjänst           │
│  (zoplanner-dotnet)         │
│  Port: 5027                 │
│  SpringApi__BaseUrl ->      │
│  http://zoplanner:8080/api  │
└─────────────┬───────────────┘
              │ beroende av
              ▼
┌─────────────────────────────┐     ┌─────────────────────────────┐
│   Spring Boot-app           │     │  Notification Service       │
│   (zoplanner)               │     │  (zoplanner-notification)   │
│   Port: 8080                │     │  Port: 8082                 │
└─────────────┬───────────────┘     └─────────────┬───────────────┘
              │ beroende av                       │ beroende av
              │                                   │
              └──────────────┬────────────────────┘
                             ▼
              ┌─────────────────────────────┐
              │     PostgreSQL-databas      │
              │   (zoplanner-database)      │
              │   Port: 5432                │
              └─────────────────────────────┘

Alla containrar är anslutna till samma nätverk `zoplanner` (bridge), så de kan kommunicera med varandra via container-namn.

```

| Container | Beskrivning | Port |
|-----------|-------------|------|
| **zoplanner-database** | PostgreSQL databas | `localhost:5432` |
| **zoplanner** | Spring Boot API (Java) | `localhost:8080` |
| **zoplanner-dotnet** | .NET Service | `localhost:5027` |
| **zoplanner-frontend** | Frontend applikation | `localhost:3000` |
| **zoplanner-notificationservice** | Notification Service | `localhost:8082` |

---

## ✅ Förutsättningar - Vad behöver jag installera?

Innan du börjar, installera följande program:

### 1. Docker Desktop (KRÄVS)
- 📥 **Ladda ner:** [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)
- ✅ Installera och starta Docker Desktop
- 🔍 Verifiera installation: Öppna terminal och kör `docker --version`
- ☝️ Man behöver skapa ett konto eller logga in
> Om du skapar ett konto för första gången måste du verifiera din e-postadress med hjälp av länken som skickas till din e-postadress, annars kommer du att få problem med att starta applikationen.

### 2. Git (KRÄVS)
- 📥 **Ladda ner:** [https://git-scm.com/downloads](https://git-scm.com/downloads)
- ✅ Installera Git
- 🔍 Verifiera installation: Öppna terminal och kör `git --version`

### 3. Maven och Java JDK 21 (KRÄVS)
Windows
1. Öppna CMD som administratör

2. Starta PowerShell från CMD (eller starta PowerShell direct utan CMD)

```
powershell
```

Nu kan du köra PowerShell-kommandon direkt i CMD.

3. Tillåt att skript körs

```
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser -Force
```
Detta behövs för att kunna installera Scoop.

4. Installera Scoop

```
iwr -useb get.scoop.sh | iex
```

Scoop installerar sig själv och uppdaterar PATH automatiskt.

5. Installera Maven

```
scoop install maven
```
<img width="438" height="43" alt="image" src="https://github.com/user-attachments/assets/5918feb5-6245-4b09-8a2e-ef44f9350cc4" />

6. Lägg till Java-bucket

```
scoop bucket add java
```

7. Installera Java 21

```
scoop install openjdk21
```

Detta installerar Java 21 och sätter JAVA_HOME automatiskt.

8. Kontrollera Java

```
java -version
```
Du ska se något som:

<img width="638" height="114" alt="image" src="https://github.com/user-attachments/assets/f011a3b5-c6ac-478d-b7a8-d0e90aa6732a" />


9. Kontrollera Maven

```
mvn -v
```

Du ska se något som

<img width="936" height="79" alt="image" src="https://github.com/user-attachments/assets/e3b2c0cd-18ba-4a60-850f-85cb9c828d2a" />


### 7. För utveckling (men man kan starta hela systemet utan det)
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

# Klona alla fyra repositories
# När man kör det här kommandot öppnas ett fönster där man behöver logga in på GitHub. Följ instruktionerna. 
# Man måste logga in med ett konto som har åtkomst till alla repo som man klonar.

git clone https://github.com/zocom-utveckling/zoplanner-api.git
git clone https://github.com/zocom-utveckling/zoplanner-service.git
git clone https://github.com/zocom-utveckling/zoplanner-frontend.git
git clone https://github.com/zocom-utveckling/zoplanner-notificationservice
```

### Steg 2: Skapa miljövariabler (.env fil)

Navigera till `zoplanner-api/webapi` mappen och skapa en fil som heter `.env`:

```bash
cd zoplanner-api/webapi
```

### Skapa filen `.env` 

Windows

```
# Alternativ A – via PowerShell
New-Item -Path .env -ItemType File

# Alternativ B – via vanlig CMD
type nul > .env

# Kontrollera innehållet
notepad .env

# Notepad öppnas – klistra in följande:

HOST=zoplanner-database
PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=test123
POSTGRES_DB=zoplanner
AWS_ACCESS_KEY_ID=your_actual_access_key_id
AWS_SECRET_ACCESS_KEY=your_actual_secret_access_key
AWS_REGION=eu-north-1
SQS_QUEUE_URL=https://sqs.eu-north-1.amazonaws.com/your-account-id/zoplanner-notifications

# Spara filen → stäng Notepad.

```

> ⚠️ **VIKTIGT:** Kommittera ALDRIG denna fil till Git!

### (OBS, följande steg behövs ej om du följt guiden rakt av, gå direkt till steg 4)
### Steg 3: Konfigurera sökvägar i docker-compose.yml 

Öppna filen `zoplanner-api/webapi/docker-compose.yml` och uppdatera sökvägarna till .NET-tjänsten och frontend:

```yaml
notepad docker-compose.yml

# Ändra denna rad för .NET-tjänsten:
context: ./path/to/dotnet-service
# Till din faktiska sökväg, t.ex. (om repos ligger i samma mapp):
context: ../../zoplanner-service/zoplannerservice

# Ändra denna rad för frontend:
context: ./path/to/frontend
# Till din faktiska sökväg, t.ex. (om repos ligger i samma mapp):
context: ../../zoplanner-frontend

# Ändra denna rad för notification-service tjänsten:
context: .path/to/zoplanner-notificationservice 
# Till din faktiska sökväg, t.ex. (om repos ligger i samma mapp):
context: ../../zoplanner-notificationservice
```

> 💡 **Tips:** Sökvägarna beror på var du har klonat repositories. Om alla tre repos ligger i samma mapp (`ZoPlanner/`), använd `../../` för att gå två nivåer upp från `webapi/`-mappen.


### Steg 4: Starta alla containers - Starta Docker Desctop innan du ska använda en kommando!

```bash
# Starta alla 5 containers (från zoplanner-api/webapi mappen)
docker-compose up -d --build
```

### Steg 5: Verifiera att allt fungerar

- 🌐 Öppna **Frontend**: [http://localhost:3000](http://localhost:3000)
- 🔌 Öppna **Spring Boot API (Swagger)**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- 🔌 Öppna **.NET Service**: [http://localhost:5027/swagger/index.html](http://localhost:5027/swagger/index.html)

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
| **Lägg till data manuelt i databasen** | `docker exec -it din-container-namn psql -U postgres -d zoplanner -c "skriv sql kodan här"` |
| **Lägg till data manuelt från fil** | ` Kopiera SQL-filen till containern docker cp uppdatering.sql din-container-namn:/tmp/`
| **Kör filen** | ` Kör SQL-filen docker exec -it din-container-namn psql -U postgres -d ditt-db-namn -f /tmp/uppdatering.sql`
 
# Kör SQL-filen
docker exec -it din-container-namn psql -U postgres -d ditt-db-namn -f /tmp/uppdatering.sql` |

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

# Endast Notification service
docker-compose up -d notification-service
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

## 🆘 Behöver du hjälp?

Om du stöter på problem:

1. 📖 Läs igenom felsökningsavsnittet ovan
2. 📋 Kontrollera loggarna med `docker-compose logs -f`
3. 💬 Kontakta utvecklingsteamet i discord


### OBS, utdaterad data. Det finns fler tabeller och kolumner i databasen idag. Databasen skapas automatiskt via Java CRUD API-koden om det inte finns en databas på datorn. För att uppdatera databasen krävs i nuläget att man manuellt tart bort den gamla från PgAdmin.

---

```
-- ============================
-- TESTDATA TILL DATABASEN( - 20 POSTER PER TABELL
-- ============================

-- 1. USERS (40 st - 20 för managers, 20 för consultants)
INSERT INTO users (username, password, name, role) VALUES
-- Managers (1-20)
('anna.svensson', '$2a$10$hash1', 'Anna Svensson', 'MANAGER'),
('karin.nilsson', '$2a$10$hash2', 'Karin Nilsson', 'MANAGER'),
('gustav.olsson', '$2a$10$hash3', 'Gustav Olsson', 'MANAGER'),
('sara.lindberg', '$2a$10$hash4', 'Sara Lindberg', 'MANAGER'),
('linda.lund', '$2a$10$hash5', 'Linda Lund', 'MANAGER'),
('manager.one', '$2a$10$hash6', 'Manager Ett', 'MANAGER'),
('manager.two', '$2a$10$hash7', 'Manager Två', 'MANAGER'),
('manager.three', '$2a$10$hash8', 'Manager Tre', 'MANAGER'),
('manager.four', '$2a$10$hash9', 'Manager Fyra', 'MANAGER'),
('manager.five', '$2a$10$hash10', 'Manager Fem', 'MANAGER'),
('manager.six', '$2a$10$hash11', 'Manager Sex', 'MANAGER'),
('manager.seven', '$2a$10$hash12', 'Manager Sju', 'MANAGER'),
('manager.eight', '$2a$10$hash13', 'Manager Åtta', 'MANAGER'),
('manager.nine', '$2a$10$hash14', 'Manager Nio', 'MANAGER'),
('manager.ten', '$2a$10$hash15', 'Manager Tio', 'MANAGER'),
('manager.eleven', '$2a$10$hash16', 'Manager Elva', 'MANAGER'),
('manager.twelve', '$2a$10$hash17', 'Manager Tolv', 'MANAGER'),
('manager.thirteen', '$2a$10$hash18', 'Manager Tretton', 'MANAGER'),
('manager.fourteen', '$2a$10$hash19', 'Manager Fjorton', 'MANAGER'),
('manager.fifteen', '$2a$10$hash20', 'Manager Femton', 'MANAGER'),
-- Consultants (21-40)
('erik.johansson', '$2a$10$hash21', 'Erik Johansson', 'CONSULTANT'),
('lars.karlsson', '$2a$10$hash22', 'Lars Karlsson', 'CONSULTANT'),
('per.eriksson', '$2a$10$hash23', 'Per Eriksson', 'CONSULTANT'),
('sofia.larsson', '$2a$10$hash24', 'Sofia Larsson', 'CONSULTANT'),
('emma.persson', '$2a$10$hash25', 'Emma Persson', 'CONSULTANT'),
('johan.bergman', '$2a$10$hash26', 'Johan Bergman', 'CONSULTANT'),
('mikael.hansson', '$2a$10$hash27', 'Mikael Hansson', 'CONSULTANT'),
('david.berg', '$2a$10$hash28', 'David Berg', 'CONSULTANT'),
('helen.holm', '$2a$10$hash29', 'Helen Holm', 'CONSULTANT'),
('peter.strand', '$2a$10$hash30', 'Peter Strand', 'CONSULTANT'),
('thomas.berg', '$2a$10$hash31', 'Thomas Berg', 'CONSULTANT'),
('camilla.hult', '$2a$10$hash32', 'Camilla Hult', 'CONSULTANT'),
('anders.wall', '$2a$10$hash33', 'Anders Wall', 'CONSULTANT'),
('consultant.one', '$2a$10$hash34', 'Konsult Ett', 'CONSULTANT'),
('consultant.two', '$2a$10$hash35', 'Konsult Två', 'CONSULTANT'),
('consultant.three', '$2a$10$hash36', 'Konsult Tre', 'CONSULTANT'),
('consultant.four', '$2a$10$hash37', 'Konsult Fyra', 'CONSULTANT'),
('consultant.five', '$2a$10$hash38', 'Konsult Fem', 'CONSULTANT'),
('consultant.six', '$2a$10$hash39', 'Konsult Sex', 'CONSULTANT'),
('consultant.seven', '$2a$10$hash40', 'Konsult Sju', 'CONSULTANT');

-- 2. MANAGERS (20 st)
INSERT INTO managers (user_id) VALUES
(1), (2), (3), (4), (5), (6), (7), (8), (9), (10),
(11), (12), (13), (14), (15), (16), (17), (18), (19), (20);

-- 3. CONSULTANTS (20 st)
INSERT INTO consultants (user_id, manager_id, city) VALUES
(21, 1, 'Stockholm'),
(22, 2, 'Göteborg'),
(23, 3, 'Malmö'),
(24, 4, 'Uppsala'),
(25, 5, 'Lund'),
(26, 6, 'Linköping'),
(27, 7, 'Örebro'),
(28, 8, 'Västerås'),
(29, 9, 'Helsingborg'),
(30, 10, 'Norrköping'),
(31, 11, 'Jönköping'),
(32, 12, 'Umeå'),
(33, 13, 'Gävle'),
(34, 14, 'Borås'),
(35, 15, 'Eskilstuna'),
(36, 16, 'Karlstad'),
(37, 17, 'Sundsvall'),
(38, 18, 'Luleå'),
(39, 19, 'Trollhättan'),
(40, 20, 'Växjö');

-- 4. CUSTOMERS (20 st)
INSERT INTO customers (name, city, manager_id) VALUES
('Volvo Group', 'Göteborg', 1),
('Ericsson AB', 'Stockholm', 2),
('H&M Hennes & Mauritz', 'Stockholm', 3),
('IKEA Sweden', 'Älmhult', 4),
('Spotify AB', 'Stockholm', 5),
('Skanska AB', 'Stockholm', 6),
('Scania CV', 'Södertälje', 7),
('ABB Sweden', 'Västerås', 8),
('SEB Bank', 'Stockholm', 9),
('Nordea Bank', 'Stockholm', 10),
('Telia Company', 'Stockholm', 11),
('Vattenfall AB', 'Solna', 12),
('SCA Hygiene', 'Stockholm', 13),
('SKF Group', 'Göteborg', 14),
('Electrolux AB', 'Stockholm', 15),
('Sandvik AB', 'Stockholm', 16),
('AstraZeneca', 'Göteborg', 17),
('Volvo Cars', 'Göteborg', 18),
('SSAB Steel', 'Stockholm', 19),
('Saab AB', 'Linköping', 20);

-- 5. CLASSES (20 st)
INSERT INTO classes (name, customer_id) VALUES
('Backend Development 2024', 1),
('Frontend Specialist Training', 2),
('Cloud Architecture Course', 3),
('DevOps Fundamentals', 4),
('Data Science Bootcamp', 5),
('Cybersecurity Essential', 6),
('Mobile App Development', 7),
('AI & Machine Learning', 8),
('Full Stack Development', 9),
('Database Administration', 10),
('System Integration', 11),
('Agile Project Management', 12),
('UI/UX Design Principles', 13),
('Microservices Architecture', 14),
('React Native Development', 15),
('Kubernetes Training', 16),
('Python Programming', 17),
('Java Enterprise Edition', 18),
('Software Testing', 19),
('Network Administration', 20);

-- 6. COURSES (20 st)
INSERT INTO courses (name, class_id, date_start, date_end) VALUES
('Backend Development Sprint 1', 1, '2024-01-15', '2024-03-15'),
('Frontend Basics', 2, '2024-02-01', '2024-04-01'),
('AWS Cloud Foundations', 3, '2024-01-10', '2024-02-28'),
('DevOps Pipeline Setup', 4, '2024-03-01', '2024-05-01'),
('Data Analysis with Python', 5, '2024-02-15', '2024-04-30'),
('Security Best Practices', 6, '2024-01-20', '2024-03-20'),
('iOS Development', 7, '2024-04-01', '2024-06-30'),
('Machine Learning Basics', 8, '2024-01-05', '2024-03-05'),
('Full Stack MERN', 9, '2024-02-10', '2024-05-10'),
('PostgreSQL Advanced', 10, '2024-03-15', '2024-05-15'),
('REST API Integration', 11, '2024-01-25', '2024-03-25'),
('Scrum Master Training', 12, '2024-02-20', '2024-04-20'),
('Design Thinking Workshop', 13, '2024-04-05', '2024-06-05'),
('Microservices with Spring', 14, '2024-01-30', '2024-04-30'),
('React Native Essentials', 15, '2024-03-20', '2024-06-20'),
('K8s Deployment Strategies', 16, '2024-02-05', '2024-04-05'),
('Python for Data Science', 17, '2024-01-12', '2024-03-12'),
('Java Spring Boot', 18, '2024-03-10', '2024-06-10'),
('Automated Testing', 19, '2024-02-25', '2024-05-25'),
('Network Security', 20, '2024-04-15', '2024-07-15');

-- 7. ASSIGNMENTS (20 st)
INSERT INTO assignments (course_id, consultant_id, date_start, date_end) VALUES
(1, 1, '2024-01-15', '2024-03-15'),
(2, 2, '2024-02-01', '2024-04-01'),
(3, 3, '2024-01-10', '2024-02-28'),
(4, 4, '2024-03-01', '2024-05-01'),
(5, 5, '2024-02-15', '2024-04-30'),
(6, 6, '2024-01-20', '2024-03-20'),
(7, 7, '2024-04-01', '2024-06-30'),
(8, 8, '2024-01-05', '2024-03-05'),
(9, 9, '2024-02-10', '2024-05-10'),
(10, 10, '2024-03-15', '2024-05-15'),
(11, 11, '2024-01-25', '2024-03-25'),
(12, 12, '2024-02-20', '2024-04-20'),
(13, 13, '2024-04-05', '2024-06-05'),
(14, 14, '2024-01-30', '2024-04-30'),
(15, 15, '2024-03-20', '2024-06-20'),
(16, 16, '2024-02-05', '2024-04-05'),
(17, 17, '2024-01-12', '2024-03-12'),
(18, 18, '2024-03-10', '2024-06-10'),
(19, 19, '2024-02-25', '2024-05-25'),
(20, 20, '2024-04-15', '2024-07-15');

-- 8. SESSIONS (20 st)
INSERT INTO sessions (assignment_id, time_start, time_end, location, comment) VALUES
(1, '2024-01-15 09:00:00', '2024-01-15 17:00:00', 'ONSITE', 'Första kursdagen'),
(2, '2024-02-01 10:00:00', '2024-02-01 16:00:00', 'REMOTE', 'Online intro'),
(3, '2024-01-10 08:30:00', '2024-01-10 16:30:00', 'HYBRID', 'Blandad session'),
(4, '2024-03-01 09:00:00', '2024-03-01 17:00:00', 'ONSITE', 'Praktisk workshop'),
(5, '2024-02-15 13:00:00', '2024-02-15 17:00:00', 'REMOTE', 'Eftermiddagssession'),
(6, '2024-01-20 09:00:00', '2024-01-20 12:00:00', 'ONSITE', 'Förmiddagsföreläsning'),
(7, '2024-04-01 10:00:00', '2024-04-01 15:00:00', 'REMOTE', 'Distansundervisning'),
(8, '2024-01-05 09:00:00', '2024-01-05 17:00:00', 'HYBRID', 'Kickoff meeting'),
(9, '2024-02-10 08:00:00', '2024-02-10 16:00:00', 'ONSITE', 'Heldagsworkshop'),
(10, '2024-03-15 14:00:00', '2024-03-15 18:00:00', 'REMOTE', 'Kvällssession'),
(11, '2024-01-25 09:00:00', '2024-01-25 12:00:00', 'ONSITE', 'API-genomgång'),
(12, '2024-02-20 10:00:00', '2024-02-20 15:00:00', 'HYBRID', 'Scrum training'),
(13, '2024-04-05 09:30:00', '2024-04-05 16:30:00', 'ONSITE', 'Design workshop'),
(14, '2024-01-30 09:00:00', '2024-01-30 17:00:00', 'REMOTE', 'Microservices intro'),
(15, '2024-03-20 10:00:00', '2024-03-20 16:00:00', 'HYBRID', 'React Native basics'),
(16, '2024-02-05 08:00:00', '2024-02-05 17:00:00', 'ONSITE', 'Kubernetes deep dive'),
(17, '2024-01-12 09:00:00', '2024-01-12 15:00:00', 'REMOTE', 'Python fundamentals'),
(18, '2024-03-10 10:00:00', '2024-03-10 17:00:00', 'ONSITE', 'Spring Boot hands-on'),
(19, '2024-02-25 09:00:00', '2024-02-25 16:00:00', 'HYBRID', 'Testing strategies'),
(20, '2024-04-15 09:00:00', '2024-04-15 17:00:00', 'ONSITE', 'Network security lab');

-- 9. CONSULTANT_STATUSES (20 st)
INSERT INTO consultant_statuses (consultant_id, status, date_start, date_end, comment) VALUES
(1, 'BUSY', '2024-01-15', '2024-03-15', 'På uppdrag hos Volvo'),
(2, 'AVAILABLE', '2024-04-01', '2024-04-30', 'Ledig för nya uppdrag'),
(3, 'VACATION', '2024-07-01', '2024-07-21', 'Sommarsemester'),
(4, 'BUSY', '2024-03-01', '2024-05-01', 'Devops projekt'),
(5, 'STUDYING', '2024-05-01', '2024-05-15', 'Certifiering AWS'),
(6, 'BUSY', '2024-01-20', '2024-03-20', 'Säkerhetskurs'),
(7, 'AVAILABLE', '2024-02-01', '2024-03-01', 'Väntar på uppdrag'),
(8, 'SICK', '2024-02-10', '2024-02-15', 'Sjukskriven'),
(9, 'BUSY', '2024-02-10', '2024-05-10', 'Full stack utveckling'),
(10, 'VACATION', '2024-12-20', '2025-01-06', 'Julledighet'),
(11, 'BUSY', '2024-01-25', '2024-03-25', 'Integrationsprojekt'),
(12, 'AVAILABLE', '2024-05-01', '2024-06-01', 'Söker nytt uppdrag'),
(13, 'BUSY', '2024-04-05', '2024-06-05', 'UX design workshop'),
(14, 'STUDYING', '2024-03-01', '2024-03-15', 'Spring Boot kurs'),
(15, 'BUSY', '2024-03-20', '2024-06-20', 'React Native projekt'),
(16, 'VACATION', '2024-08-01', '2024-08-14', 'Sommarsemester'),
(17, 'BUSY', '2024-01-12', '2024-03-12', 'Data science projekt'),
(18, 'AVAILABLE', '2024-06-15', '2024-07-15', 'Mellan uppdrag'),
(19, 'BUSY', '2024-02-25', '2024-05-25', 'Testing projekt'),
(20, 'UNAVAILABLE', '2024-11-01', '2024-11-30', 'Föräldraledighet');
```

