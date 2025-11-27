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
┌─────────────────────────────┐
│   Spring Boot-app           │
│   (zoplanner)               │
│   Port: 8080                │
└─────────────┬───────────────┘
              │ beroende av
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

### 3. Maven och Java JDK 21
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

# Spara filen → stäng Notepad.

```

> ⚠️ **VIKTIGT:** Kommittera ALDRIG denna fil till Git!

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
```

> 💡 **Tips:** Sökvägarna beror på var du har klonat repositories. Om alla tre repos ligger i samma mapp (`ZoPlanner/`), använd `../../` för att gå två nivåer upp från `webapi/`-mappen.

### Steg 4: Bygg Spring Boot JAR-fil (krävs första gången)

```bash
# Se till att du är i zoplanner-api/webapi mappen
cd zoplanner-api/webapi

# Bygg JAR-filen (skippar tester för snabbare build)
mvn clean package -DskipTests
```

### Steg 5: Starta alla containers - Starta Docker Desctop innan du ska använda en kommando!

```bash
# Starta alla 4 containers (från zoplanner-api/webapi mappen)
docker-compose up -d --build
```

### Steg 6: Verifiera att allt fungerar

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

## 🆘 Behöver du hjälp?

Om du stöter på problem:

1. 📖 Läs igenom felsökningsavsnittet ovan
2. 📋 Kontrollera loggarna med `docker-compose logs -f`
3. 💬 Kontakta utvecklingsteamet i discord

---

*Senast uppdaterad: November 2025*
