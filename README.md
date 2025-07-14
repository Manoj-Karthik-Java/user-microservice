To upload a newly created project from your local machine to GitHub, follow these steps:
git init

Add all files in the project directory: 
git add .

Commit the added files: 
git commit -m "Initial commit"

Link your local repository to the GitHub repository: 
git remote add origin https://github.com/your-username/your-repo-name.git

Verify the remote repository: 
git remote -v

Push your local repository to GitHub: 
giy push -u origin master

If your default branch is named "main" instead of "master," use: 
git push -u origin main

To be a client of spring cloud config this application should add the below dependencies

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>

**bootstrap.properties File**
Please double-check that the bootstrap.properties file has the following configuration properties:

spring.cloud.config.uri=http://localhost:8012
and optionally:
spring.cloud.config.name=users-ws
Where:
The spring.cloud.config.uri property should point to the Spring Cloud Config Server. If the server runs on a different host or port, update this property accordingly.
The spring.cloud.config.name property indicates the name of the configuration file (in the Spring Cloud Config Server) that the Microservice will fetch properties from. If you set this to users-ws, the Microservice will request configuration properties from users-ws.properties (and application.properties) stored in the Config Server.
If the users-ws.properties files is not found in the Config Server, then, Spring Cloud Config server will share configuration properties from the application.properties file only.