# TatMobileAnalyzer

![Logo](https://github.com/NikRam822/TatMobileAnalyzer/blob/master/docs/media/Logo.svg) 

# What is TatMobileAnalyzer?
TatMobileAnalyzer is a comprehensive solution for analysing the work of developers within one or several projects. This solution is aimed primarily at managers and product owners and is an tool that provides statistics on project contributors. 
For more information visit our [Software Requirement Specification document](docs/SPECIFICATION.md). 

## Metrics represented in our system
<details>
<summary>Click to see more</summary>

Currently, the following metrics are available:
### Quantitative metrics:
 - Number of developer commits over a given time period
 - Percentage of developer commits compared to the total number of commits over a period of time
 - Number of add and del developer commits over a period of time
 - Percentage of developer add and del commits over total number of add and del commits over a period of time
 - How much the codebase has grown over the time period including developer commits
 - How many add and del lines of code a developer writes on average per day
 - Number of add and del commits for each developer over a period of time (detailed information on commits)
 - Information on commits per developer (add and del by files)

Planned metrics:
### Metrics requiring qualitative analysis of the codebase:
 - Number of syntactically unique lines of code over a period of time
 - Number of duplicated lines of code for a certain period of time 
 - Cyclomatic complexity
</details>

# Development

## TatMobileAnalyzer uses
1) Spring boot (version 2.7.4)
2) PostgreSQL
3) Swagger2 (version 2.7.0)
4) Javadoc (version 3.0.0)

## Build

Below are the instructions for running `docker-compose` with changes to `application.properties` and `env.properties`:

1. Ensure that you have Docker and `docker-compose` installed on your system.

2. Edit Configuration Files:
   - Locate the `env.properties` file in your project directory.
   - Adjust variables like database URLs, secret keys, or any other environment-specific values:
   ```
   GITHUB_ACCESS_TOKEN=YOUR_GITHUB_TOKEN
   
   DB_URL=jdbc:postgresql://postgres_db:5432/some_database
   DB_USER=your_user
   DB_PASSWORD=your_password
   ````
   - If you change these variables, do not forget changing `docker-compose.yml` accordingly.
   - Then change `application.properties` file by adding this line:
   ```
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

3. Run Docker Compose:
   - Navigate to your project directory where the `docker-compose.yml` file is located.
   - Run `docker-compose up -d` which should start your containers in detached mode.

4. Verify:
   - Check if your services are up and running `docker-compose ps`
   - Access your application via the specified port (e.g., http://localhost:8080/swagger-ui.html).

## SWAGGER
You can use a swagger to send requests to the server.

After starting the server, go to the page:
```
http://localhost:8080/swagger-ui.html
````

## Javadoc

You can use a Javadoc

1) cd TatMobileAnalyzer
2) mvn javadoc:javadoc
3) Open target/site/apidocs/index.html

## Configuration

Before starting the project it is necessary to set up the configuration

1) Create env.properties file in src/main/resources folder
2) Specify the following parameters in the file:

   - GITHUB_ACCESS_TOKEN=your_github_access_token
   - DB_URL=your_database_url
   - DB_USER=your_database_username
   - DB_PASSWORD=your_database_password

#### Example env.properties
```
GITHUB_ACCESS_TOKEN=hgj_SfDFhdh2617fgxfGChcFGt3

DB_URL=jdbc:postgresql://localhost:5433/postgres
DB_USER=admin
DB_PASSWORD=secret
````

## Other links

- [Tat Mobile Specification](./docs/SPECIFICATION.md)
- [GitHub Workflow Rules](./docs/WORKFLOW.md)
- [Figma Prototype](https://www.figma.com/file/OE0pQL3mn4wlcng6o10AK3/ServiceDesign?type=design&node-id=264%3A1251&mode=design&t=XG83iR2C8fcI6gXF-1)
- [Toggle Report](https://docs.google.com/document/d/1K6aZL5N1QduP5FQHXYm_HEbrKH27j5Ctd6gg3J50kU0/edit#heading=h.sv8ku27k1igh)
- [Other Artifacts](https://drive.google.com/drive/folders/1RKxz5tozCQmqkwWamR1bCjz847XtSwqx?usp=sharing)


# License
The project is licensed under the [LICENCE](https://github.com/NikRam822/TatMobileAnalyzer/blob/master/LICENCE)

