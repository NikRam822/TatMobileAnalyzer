# TatMobileAnalyzer

![Logo](https://github.com/NikRam822/TatMobileAnalyzer/blob/master/docs/media/Logo.svg) 

## What is TatMobileAnalyzer?
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



## Build

### with Docker

Below are the instructions for running `docker-compose`:

1. Ensure that you have Docker and `docker-compose` installed on your system.

2. Create two `.env` files:
   - In the root folder create `.env` file with next content:
   ```text
   GITHUB_ACCESS_TOKEN=<your_github_access_token>
   DB_USER=some_user
   DB_PASSWORD=some_password
   DB_DATABASE=some_database
   DB_URL=jdbc:postgresql://postgres_db:5432/some_database
   FRONTEND_HOST=https://host.com/
   ```
   - Replace `<your_github_access_token>` to your github access token
   - Go to `./tat-mobile-front` and create `.env` file with next content:
   ```text
   VUE_APP_HOST_ADDRESS=http://java_app:8080/patch/statistic
   ```

3. Run Docker Compose:
   - in the root folder run `docker-compose up -d` which will build and run your containers in detached mode.

4. Verify:
   - Check if your services are up and running `docker-compose ps`
   - Access your application via the specified port (e.g., http://localhost:8080/swagger-ui.html for backend, 
   and http://localhost:3000/ for frontend).

### without Docker, locally
<details>

1. Ensure that you have `jdk-17` with `maven` for building projects, `npm` and 
latest `postgres` database installed on your system.
2. Create two `.env` files:
   - In the root folder create `.env` file with next content:
   ```text
   GITHUB_ACCESS_TOKEN=<your_github_access_token>
   DB_USER=some_user
   DB_PASSWORD=some_password
   DB_DATABASE=some_database
   DB_URL=jdbc:postgresql://localhost:5432/some_database
   ```
   - Replace `<your_github_access_token>` to your github access token. `DB_USER`, `DB_PASSWORD`,
   `DB_DATABASE`, and `DB_URL` should be real one and relevant for your `postgres` database. 
   Note! The database should be created before running the backend.
   - Go to `./tat-mobile-front` and create `.env` file with next content:
   ```text
   VUE_APP_HOST_ADDRESS=http://localhost:8080/patch/statistic
   ```
3. Build jar file `mvn clean package`. The builder will generate jar file in target directory: 
`target/TatMobileAnalyzer-0.0.1-SNAPSHOT.jar`
4. Go to target `cd target` and run backend `java -jar TatMobileAnalyzer-0.0.1-SNAPSHOT.jar`.
5. The backend will be available by this link `http://localhost:8080/swagger-ui.html`.
6. Run `cd tat-mobile-front && npm install`. It will download all dependencies.
7. Start frontend `npm run dev`. The website will be available by this link `http://localhost:3000`.
</details>

## Other

### TatMobileAnalyzer uses
1) Spring boot (version 2.7.4)
2) PostgreSQL
3) Swagger2 (version 2.7.0)
4) Javadoc (version 3.0.0)

### Links

- [Tat Mobile Specification](./docs/SPECIFICATION.md)
- [GitHub Workflow Rules](./docs/WORKFLOW.md)
- [Figma Prototype](https://www.figma.com/file/OE0pQL3mn4wlcng6o10AK3/ServiceDesign?type=design&node-id=264%3A1251&mode=design&t=XG83iR2C8fcI6gXF-1)
- [Toggle Report](https://docs.google.com/document/d/1K6aZL5N1QduP5FQHXYm_HEbrKH27j5Ctd6gg3J50kU0/edit#heading=h.sv8ku27k1igh)
- [Other Artifacts](https://drive.google.com/drive/folders/1RKxz5tozCQmqkwWamR1bCjz847XtSwqx?usp=sharing)

### License
The project is licensed under the [LICENCE](https://github.com/NikRam822/TatMobileAnalyzer/blob/master/LICENCE)

