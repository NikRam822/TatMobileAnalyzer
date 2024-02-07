# TatMobileAnalyzer project start
Spring: 2.7.0

Pg

Swagger

Javadoc

# SWAGGER
You can use a swagger to send requests to the server.

After starting the server, go to the page:
```
http://localhost:8080/swagger-ui.html
````

# Javadoc

You can use a Javadoc

1) cd TatMobileAnalyzer
2) mvn javadoc:javadoc
3) Open target/site/apidocs/index.html

# Configuration

Before starting the project it is necessary to set up the configuration

1) Create env.properties file in src/main/resources folder
2) Specify the following parameters in the file:

   - GITHUB_ACCESS_TOKEN=your_github_access_token
   - DB_URL=your_database_url
   - DB_USER=your_database_username
   - DB_PASSWORD=your_database_password


