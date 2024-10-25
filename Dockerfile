# Use the official Gradle image with JDK 21
FROM gradle:jdk21

# Set the Gradle user home to avoid permission issues
ENV GRADLE_USER_HOME=/home/gradle/.gradle

# Install Scala 2.13.15
RUN apt-get update && \
    apt-get install -y curl && \
    curl -LO https://downloads.lightbend.com/scala/2.13.15/scala-2.13.15.deb && \
    apt-get install -y ./scala-2.13.15.deb && \
    rm scala-2.13.15.deb

# Set the working directory for the application
WORKDIR /app

# Copy the project files into the container
COPY . /app

# Run Gradle clean build
RUN gradle clean build --no-daemon --parallel

# Command to run the Scala app
CMD ["gradle", "run"]
