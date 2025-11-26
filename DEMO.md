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




- 🔍 Verifiera installation: Öppna terminal och kör `psql --version`

### 4. Valfritt (för utveckling)
- **Java JDK 21+** - Om du vill bygga Spring Boot lokalt
- **Maven** - Om du vill bygga projektet lokalt
- **IntelliJ IDEA** - Rekommenderad IDE för Java-utveckling
- **pgAdmin 4** - För databashantering

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
