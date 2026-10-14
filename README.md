# Simple Maven CRUD GUI

A small Java Maven project demonstrating basic CRUD operations using a Swing graphical user interface and in-memory
storage.

The project also includes automated testing, JaCoCo code coverage and SonarQube static code analysis.

SonarQube is not installed directly on the local machine. It is started as a Docker container.

## Technologies

- Java 21
- Maven
- Java Swing
- JUnit 5
- JaCoCo
- SonarQube
- SonarScanner for Maven
- Docker

## Features

- Java Swing GUI
- In-memory storage using `ArrayList`
- Create a person
- Read and display all people in a table
- Update a selected person
- Delete a person
- Delete all records
- Basic input validation
- JUnit tests
- JaCoCo test coverage
- SonarQube static code analysis

All application data is stored in memory and is lost when the application is closed.

## Requirements

Before running the project, make sure the following are installed:

- Java 21
- Maven 3.8 or later
- Docker Desktop

Check the installed versions:

```bash
java -version
```

```bash
mvn -version
```

```bash
docker --version
```

## Project Structure

```text
simple-maven-crud/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/com/example/crud/
    │       ├── Main.java
    │       ├── CrudFrame.java
    │       ├── Person.java
    │       └── PersonRepository.java
    │
    └── test/
        └── java/com/example/crud/
            └── PersonRepositoryTest.java
```

## Build the Project

Compile the project with Maven:

```bash
mvn clean compile
```

## Run the Tests

Run the JUnit tests:

```bash
mvn test
```

## Run the GUI

Run the Swing application using Maven:

```bash
mvn compile exec:java
```

The application can also be started directly from the IDE by running:

```text
com.example.crud.Main
```

## JaCoCo Code Coverage

JaCoCo is configured in `pom.xml` using the Maven plugin:

```xml

<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.15</version>
    <executions>
        <execution>
            <id>prepare-agent</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>

        <execution>
            <id>jacoco-report</id>
            <phase>verify</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

JaCoCo does not need to be installed separately.

Maven downloads and runs the plugin automatically.

To run the tests and generate the JaCoCo coverage report:

```bash
mvn clean verify
```

The `verify` phase:

1. compiles the project;
2. runs the JUnit tests;
3. collects coverage information with JaCoCo;
4. generates the coverage report.

The generated HTML report can be found at:

```text
target/site/jacoco/index.html
```

Open this file in a browser to view the code coverage report.

The XML report used by SonarQube is generated at:

```text
target/site/jacoco/jacoco.xml
```

## SonarQube Maven Plugin

The SonarQube scanner is configured in `pom.xml` using the Maven plugin:

```xml

<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>5.2.0.4988</version>
</plugin>
```

The local SonarQube server is configured as:

```xml

<sonar.host.url>http://localhost:9000</sonar.host.url>
```

The JaCoCo XML report is provided to SonarQube using:

```xml

<sonar.coverage.jacoco.xmlReportPaths>
    ${project.build.directory}/site/jacoco/jacoco.xml
</sonar.coverage.jacoco.xmlReportPaths>
```

## Run SonarQube with Docker

SonarQube is run locally using Docker instead of being installed directly on the operating system.

Make sure Docker Desktop is running first.

### Pull the SonarQube Image

Docker can download the SonarQube image explicitly with:

```bash
docker pull sonarqube:lts-community
```

This step is optional because `docker run` will automatically download the image if it is not already available locally.

### Start the SonarQube Container

Run:

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community
```

This command:

- downloads the SonarQube image if required;
- creates a container named `sonarqube`;
- runs the container in the background;
- maps local port `9000` to SonarQube port `9000`.

Check that the container is running:

```bash
docker ps
```

The SonarQube container should appear in the list.

## Open SonarQube

Open the following address in a browser:

```text
http://localhost:9000
```

For a new local SonarQube installation, the initial credentials are:

```text
Username: admin
Password: admin
```

SonarQube will ask for a new password after the first login.

## Generate a SonarQube Token

A token is required to send Maven analysis results to SonarQube.

In the local SonarQube interface go to:

```text
My Account → Security
```

Generate a new user token and copy it.

Do not store the token in the source code, `pom.xml`, or Git repository.

## Run JaCoCo Before SonarQube Analysis

Before sending the project to SonarQube, generate the compiled code, test results and JaCoCo report:

```bash
mvn clean verify
```

This is important because SonarQube uses the generated JaCoCo XML report to display test coverage.

## Run SonarQube Analysis

After generating the JaCoCo report, run:

```bash
mvn sonar:sonar "-Dsonar.login=YOUR_TOKEN"
```

Replace:

```text
YOUR_TOKEN
```

with the token generated by the local SonarQube server.

For example:

```bash
mvn sonar:sonar "-Dsonar.login=squ_xxxxxxxxxxxxxxxxx"
```

If the analysis is successful, Maven finishes with:

```text
BUILD SUCCESS
```

## View SonarQube Results

After the analysis completes, open:

```text
http://localhost:9000
```

Select the project to view the analysis results.

SonarQube can report:

- Bugs
- Vulnerabilities
- Security Hotspots
- Code Smells
- Test Coverage
- Duplicated Code
- Maintainability issues
- Reliability issues
- Security issues
- Quality Gate status

JaCoCo coverage results are also imported into SonarQube.

## Typical Analysis Workflow

The normal workflow for analysing the project is:

```text
Java source code
        |
        v
JUnit tests
        |
        v
JaCoCo coverage
        |
        v
mvn clean verify
        |
        v
JaCoCo XML report
        |
        v
mvn sonar:sonar
        |
        v
SonarQube Docker container
        |
        v
http://localhost:9000
```

In practice, the main commands are:

```bash
mvn clean verify
```

followed by:

```bash
mvn sonar:sonar "-Dsonar.login=YOUR_TOKEN"
```

## Docker Commands

Check running containers:

```bash
docker ps
```

Check all containers, including stopped containers:

```bash
docker ps -a
```

Stop SonarQube:

```bash
docker stop sonarqube
```

Start the existing SonarQube container again:

```bash
docker start sonarqube
```

Restart SonarQube:

```bash
docker restart sonarqube
```

View SonarQube container logs:

```bash
docker logs sonarqube
```

Remove the SonarQube container:

```bash
docker rm -f sonarqube
```

If the container is removed, it can be created again with:

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community
```

## Maven Analysis Workflow

A complete local analysis can therefore be performed with:

```bash
mvn clean verify
```

```bash
docker start sonarqube
```

If the container has not been created yet:

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community
```

Then run:

```bash
mvn sonar:sonar "-Dsonar.login=YOUR_TOKEN"
```

Finally, open:

```text
http://localhost:9000
```

to review the SonarQube analysis.

## Notes

- No database is used.
- All CRUD data is stored in memory.
- JaCoCo is installed and executed through Maven.
- SonarScanner is executed through Maven.
- SonarQube itself runs inside Docker.
- No local SonarQube installation is required.
- Generated Maven and JaCoCo files are stored under `target/` and should not be committed to Git.
- SonarQube authentication tokens should never be committed to Git.