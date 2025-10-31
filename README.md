# Zoplanner-api

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
    time_start TIMESTAMP,
    time_end TIMESTAMP,
    assignment_id BIGINT REFERENCES assignments(id)
);
```
<img width="1664" height="1232" alt="image" src="https://github.com/user-attachments/assets/7784d7c2-444e-4158-8e5e-44a970e883d9" />


