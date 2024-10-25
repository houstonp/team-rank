
# TeamRank

### Overview
TeamRank is a Scala-based application designed to parse game results, calculate points, and rank teams based on their performance. The application processes an input file containing game results and outputs the team rankings to a specified file.

### Table of Contents
- [Specification](#specification)
- [Features](#features)
- [Technologies](#technologies)
- [Requirements](#requirements)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

### Specification
- The document describing the specification of this project is located at [BE_Coding_Test](docs/BE_Coding_Test.pdf)

### Features
- Parses game results from a text file.
- Calculates points based on wins, losses, and draws.
- Outputs a ranked list of teams to an output file.
- Comprehensive test suite for validation.

### Technologies
- **Scala 2.13.15**
- **JDK 21**
- **Gradle** for build and dependency management
- **JUnit and ScalaTest** for testing
- **Docker** for containerized application setup

### Requirements (for running run.sh or run-tests.sh)
- **Docker**: Needed for running the application or tests in a Docker container.

### Setup and Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/houstonp/teamrank.git
   cd teamrank
   ```

2. **Set Permissions for Scripts**:
   Make sure the provided scripts are executable:
   ```bash
   chmod +x run
   chmod +x run-tests
   ```

### Running the Application
To run the application and generate team rankings, provide the input and output file paths. You can use either absolute or relative paths:<br/>

**(Please note that it may take a while to run the first time as the Docker image needs to be built)**
```bash
./bin/run.sh <input file path> <output file path>
```
Example: (Uses a sample games.txt present under the "example" folder)
```bash
./bin/run.sh example/games.txt example/rankings.txt
```

### Running Tests
To execute the test suite and view detailed output:
```bash
./bin/run-tests.sh
```

### Project Structure
- `src/main/scala/com/spand/teamrank`: Contains application code including parsing, ranking, and output logic.
- `src/test/scala/com/spand/teamrank`: Contains test cases for the application's core functionality.
- `Dockerfile`: Defines the Docker image setup for running the application in a container.
- `build.gradle.kts`: Manages dependencies, build tasks, and application configuration.