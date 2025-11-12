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

<img width="697" height="551" alt="image" src="https://github.com/user-attachments/assets/866de2b7-e738-48ac-9e1f-3005532aa661" />

   
Öppna Query Tool

<img width="587" height="599" alt="image" src="https://github.com/user-attachments/assets/a3a44eb9-9f04-4b2c-963a-485f2b23a4ea" />



5. Skapa databasen om den inte redan finns

```
CREATE DATABASE zoplanner;
```
6. Byt till den nya databasen
<img width="1355" height="1153" alt="image" src="https://github.com/user-attachments/assets/d4691335-832d-48a1-9291-dbd6e8c8cecb" />

7. Klistra in koden i rutan och tryck på F5 eller "Execute script"
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
    customer_id BIGINT,
    CONSTRAINT class_customer_id_fkey 
        FOREIGN KEY (customer_id) 
        REFERENCES customers(id) 
        ON DELETE CASCADE
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
    consultant_id BIGINT,
    date_start DATE,
    date_end DATE,
    class_id BIGINT,
    CONSTRAINT assignment_consultant_id_fkey 
        FOREIGN KEY (consultant_id) 
        REFERENCES users(id) 
        ON DELETE SET NULL,
    CONSTRAINT assignment_class_id_fkey 
        FOREIGN KEY (class_id) 
        REFERENCES classes(id) 
        ON DELETE CASCADE
);

-- Create sessions table
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    time_start TIMESTAMP,
    time_end TIMESTAMP,
    assignment_id BIGINT,
    CONSTRAINT schedule_assignment_id_fkey 
        FOREIGN KEY (assignment_id) 
        REFERENCES assignments(id) 
        ON DELETE CASCADE
);
```
<img width="1664" height="1232" alt="image" src="https://github.com/user-attachments/assets/7784d7c2-444e-4158-8e5e-44a970e883d9" />


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

  


