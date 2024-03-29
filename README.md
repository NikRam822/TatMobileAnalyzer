# TatMobileAnalyzer

![Logo](https://github.com/NikRam822/TatMobileAnalyzer/blob/master/docs/media/Logo.svg) 

# What is TatMobileAnalyzer?
TatMobileAnalyzer is a comprehensive solution for analysing the work of developers within one or several projects. This solution is aimed primarily at managers and product owners and is an tool that provides statistics on project contributors. 
For more information visit our [Software Requirement Specification document](docs/SPECIFICATION.md). 

## Meteics represented in our system
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

# Practice Areas

We have prepared presentations for each practice area, focusing primarily on our activities, tools, and artifacts:

### [Context & requirements](https://docs.google.com/presentation/d/10LQMEnqjGyXT6bjqqwgGLAqfKdvsYcUmTSZ4ySaXE68/edit#slide=id.g2bf4626b8f0_2_45):
![context and reqs](./docs/media/context_and_reqs.png)

### [Planning & Tracking](https://docs.google.com/presentation/d/1RxE21MRGgMtd_o3Fw0LY9T-XuaTw2wiYoiNQLlVQlvc/edit#slide=id.g2c145daf49c_0_0)
![planning and tracking](./docs/media/planning_and_tracking.png)

### [Quality](https://docs.google.com/presentation/d/1sNMJkVtir0d8Xq8AJtGDbrwqm9qHnfdOJVQe_QdA53w/edit#slide=id.g2c145daf49c_0_0)
![quality](./docs/media/quality.png)

### [Architectural Design](https://docs.google.com/presentation/d/16PEuTRjsOlHsv0-apAT97AWRIrPep4iuqRi0tT9V_aY/edit#slide=id.g2c145daf49c_0_0)
![architecture](./docs/media/architecture.png)

### [Risk management](https://docs.google.com/presentation/d/1OFszMaOglWX0m5Tn667dteWtcb8elPTsDsy8pZfYMdM/edit#slide=id.g2c16c5cf016_0_0)
![risk](./docs/media/risk.png)

### [Configuration Management](https://docs.google.com/presentation/d/1p8p3ND9ekFV0kQYP5uz2qg-jBIxjGlojWWQ-190fW6c/edit#slide=id.g2bf447eed3b_3_45)
![configuration](./docs/media/configuration.png)

# Development

## TatMobileAnalyzer uses
1) Spring boot (version 2.7.4)
2) PostgreSQL
3) Swagger2 (version 2.7.0)
4) Javadoc (version 3.0.0)

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

