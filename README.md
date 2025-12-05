# Zoplanner-api

## Innehåll
- [Förutsättningar](#förutsättningar)
- [Starta databasen och API:t i Docker](#hur-man-startar-databasen-och-zoplanner-api-i-docker)
- [Köra projektet i IntelliJ IDEA](#hur-man-laddar-ner-och-kör-repot-i-intellij-idea)
- [Skapa environment variables i IntelliJ](#hur-man-skapar-environment-variables-i-intellij)
- [Kontrollera endpoints med Swagger](#hur-man-kontrollerar-endpoints-med-swagger)
- [Skapa databas i pgAdmin](#skapa-databas)
- [Skapa Docker-image för databasen](#hur-man-skapar-docker-image-för-bara-databasen)



## Förutsättningar
- Java JDK 21+
- Maven
- Docker Desktop
- IntelliJ IDEA (Community eller Ultimate)
- pgAdmin 4 (valfritt för databashantering)


## Hur man startar databasen och zoplanner-api i docker
1. * Installera Docker Desktop
   * Installera IntelliJ IDEA (eller annan valfri editor)
2. Navigera till YOURPATHHERE\IdeaProjects\zoplanner-api\webapi och skapa en ny fil som heter ```.env```
VARNING commita ALDRIG .env-filer till Git den borde redan vara i gitignore

<img width="1076" height="961" alt="image" src="https://github.com/user-attachments/assets/e07878d8-f57f-4d4f-83cf-34e9e250216f" />

3. Öppna filen och fyll i fälten
```
HOST=zoplanner-database
PORT=5432
POSTGRES_USER=andvändarnmantilldatabasen
POSTGRES_PASSWORD=lösenordtilldatabasen
POSTGRES_DB=databasnamnet
```

<img width="422" height="156" alt="image" src="https://github.com/user-attachments/assets/8d38298d-2edd-412b-9b86-1bf4464c9476" />

4. Skapa en .jar-fil med maven

<img width="1432" height="905" alt="image" src="https://github.com/user-attachments/assets/d6f93bc8-b963-4085-9d33-ef2051121703" />

5. Kör kommandot
```
mvn clean package
```
<img width="669" height="82" alt="image" src="https://github.com/user-attachments/assets/df1ab276-a50d-41b3-8047-9834547f7dc5" />

6. Nu borde det finnas en .jar fil i target 

<img width="664" height="661" alt="image" src="https://github.com/user-attachments/assets/4a003488-100d-490d-8225-1b89454f8cd0" />

7. Starta en terminal och navigera till YOURPATHHERE\IdeaProjects\zoplanner-api\webapi om du gör det i IntelliJ så borde det redan vara rätt path

<img width="1428" height="1018" alt="image" src="https://github.com/user-attachments/assets/26eed979-f7de-4cdc-8d68-89432c24f979" />

8. Kör kommandot
```
docker-compose up --build
```
Om du redan har en container med samma namn eller om du vill stänga ner den du har gör du det med 
```
docker-compose down
```
9. Om allt har gått rätt så borde du kunna se 2 containers i docker.desktop

<img width="1419" height="496" alt="image" src="https://github.com/user-attachments/assets/6d0d90cc-f69a-4718-8b8f-b0eba5652953" />

## Hur man laddar ner och kör repot i IntelliJ IDEA
1. * Installera Java JDK 21
   * Installera IntelliJ IDEA (Community eller Ultimate)
2. Logga in på github i IntelliJ IDEA
   File -> Settings

<img width="1438" height="907" alt="image" src="https://github.com/user-attachments/assets/4c8681a3-1917-42b2-a30d-29090f7ded80" />

Version controrl

<img width="980" height="739" alt="image" src="https://github.com/user-attachments/assets/5b60b172-2cb3-41cf-a52f-a634135fe6b9" />

Github

<img width="981" height="735" alt="image" src="https://github.com/user-attachments/assets/13efde85-ebd9-4944-b36d-d5d7b03014e1" />

Logga in(Rekommenderar via token)

<img width="981" height="734" alt="image" src="https://github.com/user-attachments/assets/a435f2dd-e644-4181-b82a-efd165a69417" />

3. Clona repot

<img width="1443" height="910" alt="image" src="https://github.com/user-attachments/assets/7061eaa8-6b68-47b0-a289-4118970776d2" />

Tryck på "github Dit namn"

<img width="805" height="653" alt="image" src="https://github.com/user-attachments/assets/ee338342-d4b0-4c14-bff5-e6dccc6dbaa7" />

Här borde du se dina repos

<img width="799" height="650" alt="image" src="https://github.com/user-attachments/assets/49c60f3f-4923-4998-8abd-13fdbeab7e49" />

Tryck på clone

4. Följ guiden Hur man skapar environment variables i intellij

5. Tryck på run 'webapiApplication'
<img width="1437" height="987" alt="image" src="https://github.com/user-attachments/assets/03f50868-2f29-4ede-a9ab-29e0438538fd" />

## Hur man skapar environment variables i intellij



1. Välj Main menu > Run > Edit configurations
<img width="1466" height="636" alt="506558492-bffb6066-5b8f-4a8f-a430-c30235578e59" src="https://github.com/user-attachments/assets/7a95a2bd-10b5-41cf-932d-4d3b9f281470" />
2. Välj Modify options > Environment variables
<img width="1128" height="833" alt="506559109-5e120a4e-900a-4156-89d5-0848a0fc4939" src="https://github.com/user-attachments/assets/58ea081c-f05c-45a7-876d-44c1a7887a1a" />
3. Skriv ditt användarnamn, lösenord and port i detta formatet

```
POSTGRES_USER=YOURUSERNAME;POSTGRES_PASSWORD=YOURPASSWORD;PORT=YOURPORTNUMBER
```
<img width="818" height="705" alt="506559798-a0cb1111-d894-4d2f-aa65-9fae0b1a306a" src="https://github.com/user-attachments/assets/f8ea0858-9386-42e7-b1ae-760b38284cdb" />

## Hur man kontrollerar endpoints med Swagger

1. Filen `pom.xml` bör innehålla en beroende för anslutning till **Swagger UI** – den ingår redan i projektet, som ni kan ladda ner från dev. Beroenden ser ut så
```
<dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.8.9</version>
 </dependency>
```
2. För att se alla endpoints, starta Spring Boot app och navigera till http://localhost:8080/swagger-ui.html

<img width="1880" height="821" alt="Image" src="https://github.com/user-attachments/assets/5c9acb4a-76a4-4e51-a4f5-84f29ccaecf8" />

3. För att testa endpoint, öppna endpoint > klicka på knappen ”Try  it out” > lägg test data om det behövs > klicka på knappen ”Execute” och du ska få ett svar 

<img width="1517" height="676" alt="Image" src="https://github.com/user-attachments/assets/4ecf942a-2617-4cb5-a77c-c5fb2d25b409" />

<img width="1448" height="693" alt="Image" src="https://github.com/user-attachments/assets/fdc76ebe-96fd-45bb-b864-3b8003cfb687" />

<img width="1141" height="675" alt="Image" src="https://github.com/user-attachments/assets/704b5a74-4de6-4a71-8b73-b7e9f32e9da1" />

## Skapa databas
Rekommenderat program pgAdmin 4

1. Lägg till databasen
   
<img width="623" height="305" alt="image" src="https://github.com/user-attachments/assets/9d468722-2f28-45f6-ba2d-ee11eea655c9" />

2. Välj ett namn (Rekommenderar zoplanner)

<img width="704" height="545" alt="image" src="https://github.com/user-attachments/assets/3dd7c15e-697c-4a66-92c7-eef3e5aee77d" />
   
4. Skriv in samma användarnamn/lösen som du skrev i din .env fil

<img width="712" height="562" alt="image" src="https://github.com/user-attachments/assets/e4d92465-b3db-449e-b4fc-6bc55f993757" />

5. Skapa databas

<img width="620" height="500" alt="image" src="https://github.com/user-attachments/assets/fa0864a9-98ed-47da-99d6-97b3e8e5dd20" />

<img width="699" height="550" alt="image" src="https://github.com/user-attachments/assets/ebebc67b-b90a-47b1-bad1-59d6dd329fbd" />

   
Öppna Query Tool

<img width="587" height="599" alt="image" src="https://github.com/user-attachments/assets/a3a44eb9-9f04-4b2c-963a-485f2b23a4ea" />



5. Skapa databasen om den inte redan finns

```
CREATE DATABASE zoplanner;
```
6. Byt till den nya databasen
<img width="1355" height="1153" alt="image" src="https://github.com/user-attachments/assets/d4691335-832d-48a1-9291-dbd6e8c8cecb" />

7. Klistra in koden i rutan och tryck på F5 eller "Execute script"
> We are designing a scheduling system where:

> Managers can manage customers, classes, courses, and consultants. Consultants teach specific courses for certain customers/classes on specific days/times. A consultant works in one city and can also work remotely. Consultants have availability statuses (available, busy, vacation, sick, etc.). Managers can only see their own customers, classes, courses, and consultants. PostgreSQL Database Schema for Scheduling System

> All table creation queries are in the correct order

> Includes ENUMs, users, managers, consultants, customers, classes, courses, assignments, sessions, consultant_status

<img width="1339" height="1158" alt="zoplannerdb" src="https://github.com/user-attachments/assets/af4c8763-4427-4c39-8018-33363911a2f3" />


```
-- ============================
-- 1. ENUMS
-- ============================

CREATE TYPE user_role AS ENUM ('MANAGER', 'CONSULTANT', 'BOTH');
CREATE TYPE consultant_status_type AS ENUM ('AVAILABLE', 'BUSY', 'VACATION', 'SICK', 'STUDYING', 'UNAVAILABLE');
CREATE TYPE session_location AS ENUM ('ONSITE', 'REMOTE', 'HYBRID');

-- ============================
-- 2. USERS
-- ============================

CREATE TABLE users (
id BIGSERIAL PRIMARY KEY,
username VARCHAR(255) UNIQUE NOT NULL,
password VARCHAR(255) NOT NULL,
name VARCHAR(255) NOT NULL,
role user_role NOT NULL DEFAULT 'CONSULTANT'
);

-- ============================
-- 3. MANAGERS
-- ============================

CREATE TABLE managers (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT UNIQUE NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================
-- 4. CONSULTANTS
-- ============================

CREATE TABLE consultants (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT UNIQUE NOT NULL,
manager_id BIGINT NULL,
city VARCHAR(255) NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (manager_id) REFERENCES managers(id) ON DELETE SET NULL
);

-- ============================
-- 5. CUSTOMERS
-- ============================

CREATE TABLE customers (
id BIGSERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
city VARCHAR(255) NOT NULL,
manager_id BIGINT NULL,
FOREIGN KEY (manager_id) REFERENCES managers(id) ON DELETE SET NULL
);

-- ============================
-- 6. CLASSES
-- ============================

CREATE TABLE classes (
id BIGSERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
customer_id BIGINT NOT NULL,
FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- ============================
-- 7. COURSES
-- ============================

CREATE TABLE courses (
id BIGSERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL,
class_id BIGINT NOT NULL,
date_start DATE NULL,
date_end DATE NULL,
FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
);

-- ============================
-- 8. ASSIGNMENTS
-- ============================

CREATE TABLE assignments (
id BIGSERIAL PRIMARY KEY,
course_id BIGINT NULL,
consultant_id BIGINT,
date_start DATE NULL,
date_end DATE NULL,
FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
FOREIGN KEY (consultant_id) REFERENCES consultants(id) ON DELETE SET NULL
);

-- ============================
-- 9. SESSIONS
-- ============================

CREATE TABLE sessions (
id BIGSERIAL PRIMARY KEY,
assignment_id BIGINT NOT NULL,
time_start TIMESTAMP NULL,
time_end TIMESTAMP NULL,
location session_location DEFAULT 'ONSITE',
comment TEXT,
FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE
);

-- ============================
-- 10. CONSULTANT STATUS
-- ============================

CREATE TABLE consultant_statuses (
id BIGSERIAL PRIMARY KEY,
consultant_id BIGINT NOT NULL,
status consultant_status_type NOT NULL,
date_start DATE NOT NULL,
date_end DATE NOT NULL,
comment TEXT,
FOREIGN KEY (consultant_id) REFERENCES consultants(id) ON DELETE CASCADE
);
```

### Summary of Cascade Logic
- Users -> Managers: ON DELETE CASCADE
Deleting a user automatically deletes the corresponding manager.

- Managers -> Consultants: ON DELETE CASCADE
If a manager is deleted, consultants are preserved but manager_id becomes NULL.

- Managers -> Customers: ON DELETE CASCADE
If a manager is deleted, customers are preserved but manager_id becomes NULL.

- Users -> Consultants: ON DELETE CASCADE
Deleting a user also deletes the consultant.

- Courses -> Assignments: ON DELETE CASCADE
Deleting a course deletes all assignments linked to it.

- Assignments -> Sessions: ON DELETE CASCADE
Deleting an assignment deletes all its sessions.

- Consultants -> Assignments: ON DELETE SET NULL
If a consultant is deleted, assignments are preserved but consultant_id becomes NULL.

- Consultants -> Consultant_Status: ON DELETE CASCADE
Deleting a consultant removes all their status records.

- Customers -> Classes -> Courses hierarchy: ON DELETE CASCADE
Deleting a customer deletes their classes, and in turn, all courses linked to classes.

<img width="1664" height="1232" alt="image" src="https://github.com/user-attachments/assets/7784d7c2-444e-4158-8e5e-44a970e883d9" />

Se skapade tabeller
<img width="421" height="533" alt="image" src="https://github.com/user-attachments/assets/ea25a6b9-0c71-4bae-accb-976da8c1ec18" />


## Hur man skapar docker image för bara databasen
1. Installera Docker Desktop
2. Navigera till mappen YOURPATHHERE/zoplanner-api/webapi/database
3. Skapa en fil som heter .env

<img width="740" height="225" alt="image" src="https://github.com/user-attachments/assets/6a946323-1995-4054-a421-9531765f8047" />

4. Öppna filen med en texteditor(Rekommenderar vscode)
5. Klistra in koden och byt ut användarnamn och lösen
```
POSTGRES_USER=YOURUSERNAMEHERE
POSTGRES_PASSWORD=YOURPASSWORDHERE
POSTGRES_DB=zoplanner
```

6. Starta en kommandotolk

<img width="975" height="506" alt="image" src="https://github.com/user-attachments/assets/abf8fdc2-c764-4816-ab37-00682bf79914" />


7. Navigera till database mappen (exemplet är i windows)

<img width="976" height="114" alt="image" src="https://github.com/user-attachments/assets/ca39567b-e359-4470-bb58-424a64cc6454" />

8. Säkerställ att filerna är där

<img width="979" height="512" alt="image" src="https://github.com/user-attachments/assets/f5e6c0c2-2b56-43e3-af51-68b3abc5fa0f" />

9. Kör kommandot ```docker-compose up -d```

<img width="980" height="512" alt="image" src="https://github.com/user-attachments/assets/23723d2b-ffd9-4086-8ab1-58ec393ce019" />

# HUR SKA IMPLEMENTERA CI/CD MED GITHUB ACTIONS 

*Guiden för att sätta upp CI/CD Pipeline för zoplanner-api (Manuell med Shell kommand)*

1. Navigera till projekt och skapa workflow

```
cd ~/zoplanner-api
```
2. Skapa `.github/workflow –` 

          ```
          mkdir  -p .github/workflow
          ```
          
3. Skapa `integration.yml` 

   ```
   touch .github/workflows/integration.yml
   ```
4.

<img width="374" height="152" alt="image" src="https://github.com/user-attachments/assets/2a95ee3c-a6a2-4bc3-a19c-45265223e96b" />
              

5. Öppna `.github/workflows/integration.yml` och skriva denna configuration   

<img width="478" height="691" alt="image" src="https://github.com/user-attachments/assets/dab3df3c-a72b-4cd0-a7b9-cf0323ebab5e" />
<img width="486" height="493" alt="image" src="https://github.com/user-attachments/assets/a8345eb5-f258-4e91-9fd2-ff2818994ac7" />
<img width="466" height="315" alt="image" src="https://github.com/user-attachments/assets/7310a33d-0822-4a25-9b33-12d9f9f751b9" />

6. Skapa en ny branch (testa CI/CD till denna branch innan merger med dev och main)

   ```
   --- git checkout -b CI-build-test
   ```

7. Commita workflow filen

```
# Stage workflow-filen
  --- git add .github/workflows/integration.yml

Git commit -m “Add CI/CD workflow with Github Actions
```

8. Pusha till GitHub

```
          --- git push origin CI-build-test
```

<img width="602" height="192" alt="image" src="https://github.com/user-attachments/assets/758c83f0-41c6-4c2b-a227-8c96b0aec83a" />

9. Gå till zoplanner-api repository

10. Klicka på  “Actions” tab

<img width="602" height="133" alt="image" src="https://github.com/user-attachments/assets/3e5dd248-00ad-42ff-8aeb-c9471886ea1e" />

11. Här du kan se att workflow körs 

När workflow körs

<img width="602" height="199" alt="image" src="https://github.com/user-attachments/assets/7bdad17c-e671-4f5d-b23b-4eed48d37f3d" />                    


När Workflow Lyckades:

<img width="602" height="188" alt="image" src="https://github.com/user-attachments/assets/6928feba-ee1c-4e4f-b09c-a86dbc121009" />
<img width="602" height="504" alt="image" src="https://github.com/user-attachments/assets/ff3120c9-6405-4a4e-b911-8d0afbe5457a" />

När workflow failade: 

<img width="602" height="355" alt="image" src="https://github.com/user-attachments/assets/1df068a2-ea9a-4706-9ed2-d4df6e291039" />
<img width="602" height="494" alt="image" src="https://github.com/user-attachments/assets/8db5b002-f6fa-40ee-9f81-6f2a50b5922f" />
<img width="602" height="330" alt="image" src="https://github.com/user-attachments/assets/bc5fa55a-5fff-474f-947b-822d29edf6b3" />
<img width="602" height="291" alt="image" src="https://github.com/user-attachments/assets/b3c79988-b8c3-4969-9970-6378ee55a770" />

Se **Publish test results**:

<img width="602" height="263" alt="image" src="https://github.com/user-attachments/assets/7504af3c-e178-4cd7-bb36-99422f6f0278" />

Klicka på URL-> ladda ner zip fil (test results)-> extracthera filen lokalt

<img width="602" height="198" alt="image" src="https://github.com/user-attachments/assets/2b1d518b-d8a0-4e8f-886e-6fd0817f15f3" />

Öppna TEST*.xml för att se detaljerade rapporter

<img width="602" height="515" alt="image" src="https://github.com/user-attachments/assets/0b4488a9-6106-45be-8651-ac492d258ec8" />

.XML --

<img width="602" height="543" alt="image" src="https://github.com/user-attachments/assets/4fb181f8-e096-4919-b597-f083a8118047" />

Workflow-Konfiguration Förklarat:

Triggers- När körs workflow??

<img width="585" height="143" alt="image" src="https://github.com/user-attachments/assets/abdf3d2d-80f9-468a-9054-4ce17cb20be6" />

Environmental Variables: 

<img width="496" height="142" alt="image" src="https://github.com/user-attachments/assets/4d9db2cd-584f-4836-8771-ae08710f382f" />

Build-steg förklarning:

<img width="602" height="595" alt="image" src="https://github.com/user-attachments/assets/c08fbcc5-4d57-43f8-a795-dd4163d9885e" />
<img width="602" height="127" alt="image" src="https://github.com/user-attachments/assets/b0d3db8f-d3be-429a-9000-ab959671e9a9" />


  


