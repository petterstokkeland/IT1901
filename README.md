# 💰 Money Spender 💰

[![Eclipse Che](https://img.shields.io/badge/Eclipse%20Che-525C86.svg?style=for-the-badge&logo=Eclipse-Che&logoColor=white)](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2358/gr2358?new)
[![VS Code](https://img.shields.io/badge/VSCode-525C86.svg?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)](vscode://vscode.git/clone?url=https%3A%2F%2Fgitlab.stud.idi.ntnu.no%2Fit1901%2Fgroups-2023%2Fgr2358%2Fgr2358.git)
[![Issue Board](https://img.shields.io/badge/Issue%20Board-525C86.svg?style=for-the-badge&logo=GitLab&logoColor=white)](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2358/gr2358/-/boards)
[![Issues](https://img.shields.io/badge/Issues-525C86.svg?style=for-the-badge&logo=GitLab&logoColor=white)](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2358/gr2358/-/issues)
[![Merge Requests](https://img.shields.io/badge/Merge%20Requests-525C86.svg?style=for-the-badge&logo=GitLab&logoColor=white)](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2358/gr2358/-/merge_requests)

## Table of Contents

- [Introduction](#introduction)
- [Modules](#modules)
- [Diagrams](#diagrams)
- [Project Folder Structure](#project-folder-structure)
- [System Requirements](#system-requirements)
- [Running the project with Eclipse Che](#running-the-project-with-eclipse-che)
- [Running the project with VS Code](#running-the-project-with-vs-code)

## Introduction

Money Spender is a multifaceted application built on a four-module structure, aligning with a three-tier architecture that distinctively separates core logic, user interface (UI), data storage, and server communication. Managed by Maven, the project ensures a smooth integration of these modules, facilitating effective collaboration between the core logic, UI, data handling, and server interactions. This strategic arrangement of modules in Money Spender offers users an all-encompassing tool for efficient financial management.

## Modules

Money Spender is designed following a robust three-tier architecture:

- **[Core:](money-spender/core/readme.md)** This layer houses the foundational logic and data structures critical to the application's functionality.

- **[Restapi](money-spender/server/readme.md)** Managing data storage and user authentication, this layer plays a pivotal role in securely storing and retrieving essential information.

- **[Persistence:](money-spender/persistence/readme.md)** Managing data storage and user authentication, this layer plays a pivotal role in securely storing and retrieving essential information.

- **[User Interface (UI):](money-spender/ui/readme.md)** Responsible for the user-facing components and interactions, the UI layer ensures a seamless and intuitive user experience.

## Diagrams

You can access comprehensive diagrams illustrating the project's architecture by navigating to the following directory: gr2358/docs/diagrams. These diagrams offer valuable insights into the interplay among various components and the data flow within the system. Below are direct links to view these diagrams:

- [Architecture diagram](docs/diagrams/ArchitectureDiagram.png)
- [Class diagram for Core](docs/diagrams/ClassDiagramCore.png)
- [Class diagram for Persistence](docs/diagrams/ClassDiagramPersistence.png)
- [Class diagram for Restapi](docs/diagrams/ClassDiagramRestapi.png)
- [Class diagram for UI](docs/diagrams/ClassDiagramUi.png)
- [Package diagram](docs/diagrams/PackageDiagram.png)
- [Sequence diagram](docs/diagrams/SequenceDiagramUi.png)

## Project Folder Structure

- **Core Logic Code:**

  - Location: `gr2358/money-spender/core/src/main/java/core`
  - Testing: `gr2358/money-spender/core/src/test/java/core`

- **Restapi:**
  - Location: `gr2358/money-spender/restapi/src/main/java/restapi`
  - Testing: `gr2358/money-spender/restapi/src/test/java/restapi`

- **Persistence:**
  - Location: `gr2358/money-spender/persistence/src/main/java/persistence`
  - Testing: `gr2358/money-spender/persistence/src/test/java/persistence`

- **User Interface:**
  - Location: `gr2358/money-spender/ui/src/main/java/ui`
  - Testing: `gr2358/money-spender/ui/src/test/java/ui`

## Building with Maven

Our project employs Maven, a robust build automation tool, to manage dependencies and streamline the build process. Specifically, the following Maven plugins are utilized:

- [**maven-compiler-plugin:**](https://maven.apache.org/plugins/maven-compiler-plugin/) This plugin manages the compilation of Java source code, ensuring that it is compatible with the specified Java version.

- [**maven-surefire-plugin:**](https://maven.apache.org/surefire/maven-surefire-plugin/) This plugin is responsible for executing unit tests, aiding in validating the correctness of our codebase.

- [**javafx-maven-plugin:**](https://github.com/openjfx/javafx-maven-plugin) To facilitate the use of JavaFX within the project, this plugin provides essential configuration and support for JavaFX applications.

- [**junit**:](https://junit.org/junit5/docs/current/user-guide/) JUnit is a unit testing framework for Java. It is used to write and run repeatable tests.

- [**jacoco-maven-plugin:**](https://github.com/jacoco/jacoco) We implemented jacoco to generate code coverage reports. This helps us to see how much of our code is covered by tests and find areas that need better testing. You can find the reports in the folder target/site/jacoco in every module after building the project or `mvn verify`.

- [**maven-checkstyle-plugin:**](https://checkstyle.sourceforge.io) We implemented Checkstyle for ensure that our code follows a set of coding standards. This helps us to write code that is easier to read and maintain. The standard we use is the eclipse-java-google-style.xml. We added this file in config foldere in money-spender folder. When we run the command mvn verify, checkstyle will check if our code follows the rules in this file. We will get warnings and violations if our code does not follow the rules.

- [**spotbugs-maven-plugin:**](https://spotbugs.github.io) We implemented SpotBugs to our project to help us catch mistakes and problems in our code. When we run the `mvn verify` command, SpotBugs checks our code for errors. If it finds any, it tells us about them during the building process. This makes our code more reliable and secure by spotting common mistakes and security problems.

- [**spring-boot-maven-plugin:**](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/) The Spring Boot Maven Plugin provides Spring Boot support in Apache Maven. It allows us to run Spring Boot applications, generate build information and start your Spring Boot application prior to running integration tests.

## System Requirements

Before running this project, please ensure that you have the following softwares and versions installed on your system:

- **Java:** We recommend using `Java version 17.0.8-tem` for optimal compatibility and performance. You can check your Java version by running the following command in your terminal:

```shell
java --version
```

- **Maven:** Make sure you have `Maven version 3.9.4` or a higher version installed to effectively manage dependencies and facilitate the building process. You can check your Maven version by running the following command in your terminal:

```shell
mvn -version
```

## Running the project with Eclipse Che

[![Eclipse Che](https://img.shields.io/badge/Eclipse%20Che-525C86.svg?style=for-the-badge&logo=Eclipse-Che&logoColor=white)](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2358/gr2358?new)

1 Open the project in `Eclipse Che` by clicking on the badge above.

2 Navigate to `endpoints` in the bottom left corner in Eclipse Che to view the server logs.

3 Open public and copy the link to the port with name: `6080-tcp-desktop-ui (6080/http)`.

4 Open a new tab in your browser and paste the link + press enter. The virtual machine should now start.

5 Go back to `Eclipse Che`.

6 Open the `terminal`.

7 Run the following command:

```shell
cd money-spender/
```

8 Install dependencies and compile:

```shell
mvn clean install -DskipTests
```

9 Start the restapi server:

```shell
mvn spring-boot:run -f restapi/pom.xml
```

10 Open a new terminal

11 Run the following command:

```shell
cd money-spender/
```

12 Test the application:

```shell
mvn test
```

13 Run the application:

```shell
mvn javafx:run -f ui/pom.xml
```

14 Run spotbugs and checkstyle:

```shell
mvn verify
```

Make sure you are in the correct directory (`gr2358/money-spender/`) before running these commands to ensure they execute correctly.

## Running the project with VS Code

[![VS Code](https://img.shields.io/badge/VSCode-525C86.svg?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)](vscode://vscode.git/clone?url=https%3A%2F%2Fgitlab.stud.idi.ntnu.no%2Fit1901%2Fgroups-2023%2Fgr2358%2Fgr2358.git)

1 Press the badge above to clone the project to `VS Code`.

2 Select a folder to clone the project to.

3 Open the folder in `VS Code`.

4 Open the `terminal`.

5 Run the following commands:

```shell
cd money-spender/
```

6 Install dependencies and compile:

```shell
mvn clean install -DskipTests
```

7 Start the restapi server:

```shell
mvn spring-boot:run -f restapi/pom.xml
```

8 Open a new terminal

9 Run the following command:

```shell
cd money-spender/
```

10 Test the application:

```shell
mvn test
```

11 Run the application:

```shell
mvn javafx:run -f ui/pom.xml
```

12 Run spotbugs and checkstyle:

```shell
mvn verify
```

Make sure you are in the correct directory (`gr2358/money-spender/`) before running these commands to ensure they execute correctly.

## Shippable product

Please make sure you have compiled with maven.

1 Navigate to ui folder

```shell
cd ui
```

2 Use this command inside ui folder

```shell
mvn javafx:jlink -f ./pom.xml
```

3 Use this command inside ui folder

```shell
mvn jpackage:jpackage -f ./pom.xml
```

4 drag the application to your local desktop or other folder.

5 Before you can run the application from desktop, you need to start the `spring-boot server`: Follow this [guide](../gr2358/money-spender/restapi/readme.md#getting-started)