# Zoplanner-api
## Hur man ladder ner och kör repot i IntelliJ IDEA
1. * Installera Java JDK 21
   * Installera IntelliJ IDEA (Community eller Ultimate)
2.  Logga in på github i IntelliJ IDEA
   File -> Settings

<img width="1438" height="907" alt="image" src="https://github.com/user-attachments/assets/4c8681a3-1917-42b2-a30d-29090f7ded80" />

Version controrl

<img width="980" height="739" alt="image" src="https://github.com/user-attachments/assets/5b60b172-2cb3-41cf-a52f-a634135fe6b9" />

Github

<img width="981" height="735" alt="image" src="https://github.com/user-attachments/assets/13efde85-ebd9-4944-b36d-d5d7b03014e1" />

Logga in(Recomenderar via token)

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
3. Skriv ditt andvändarnamn, lösenord and port i detta formatet

```USERNAME=YOURUSERNAME;PASSWORD=YOURPASSWORD;PORT=YOURPORTNUMBER```
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

3. För att testa endpoint, oppna endpoit > klicka på knappen ”Try  it out” > lägg test data om det hehövs > klicka på knappen ”Execute” och du ska få ett svar 

<img width="1517" height="676" alt="Image" src="https://github.com/user-attachments/assets/4ecf942a-2617-4cb5-a77c-c5fb2d25b409" />

<img width="1448" height="693" alt="Image" src="https://github.com/user-attachments/assets/fdc76ebe-96fd-45bb-b864-3b8003cfb687" />

<img width="1141" height="675" alt="Image" src="https://github.com/user-attachments/assets/704b5a74-4de6-4a71-8b73-b7e9f32e9da1" />

## Skapa databas
Rekommenderat program pgAdmin 4

Välj PostgreSQL 18
<img width="1362" height="1150" alt="image" src="https://github.com/user-attachments/assets/1558f3b6-5860-4d18-b31e-40999fba9930" />
Öppna Query Tool
<img width="1355" height="1154" alt="image" src="https://github.com/user-attachments/assets/f63e9d92-8fd2-4d41-b14b-6039652fef33" />


1. Skapa databas

```
CREATE DATABASE zoplanner;
```
2. Byt till den nya databasen
<img width="1355" height="1153" alt="image" src="https://github.com/user-attachments/assets/d4691335-832d-48a1-9291-dbd6e8c8cecb" />

3. Klistra in koden i rutan och tryck på F5 eller "exicute script"
```
-- Create customers table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    city VARCHAR(255)
);

-- Create class table
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

-- Create assignment table
CREATE TABLE assignments (
    id BIGSERIAL PRIMARY KEY,
    course_name VARCHAR(255),
    consultant_id BIGINT REFERENCES users(id),
    date_start DATE,
    date_end DATE,
    class_id BIGINT REFERENCES classes(id)
);

-- Create schedule table
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    time_start TIMESTAMP,
    time_end TIMESTAMP,
    assignment_id BIGINT REFERENCES assignments(id)
);
```
<img width="1664" height="1232" alt="image" src="https://github.com/user-attachments/assets/7784d7c2-444e-4158-8e5e-44a970e883d9" />


