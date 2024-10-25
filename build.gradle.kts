plugins {
    scala
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "com.spand.teamrank"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.15")

    testImplementation("org.scalatest:scalatest_2.13:3.2.19")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
    testRuntimeOnly("org.scalatestplus:junit-5-10_2.13:3.2.19.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed") // Log details of passed, skipped, and failed tests
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL // Show full exception details
        showStandardStreams = true // Print output from tests to the console
    }

    // Add a test listener to summarize results after all tests run
    addTestListener(object : TestListener {
        override fun afterSuite(desc: TestDescriptor, result: TestResult) {
            // Only print summary for the root suite
            if (desc.parent == null) {
                println(
                        """
                    |------------------------------
                    | Test Results:
                    |   Tests run: ${result.testCount}
                    |   Passed: ${result.successfulTestCount}
                    |   Skipped: ${result.skippedTestCount}
                    |   Failed: ${result.failedTestCount}
                    |------------------------------
                    """.trimMargin()
                )
            }
        }

        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
    })
}

// Allow passing arguments to the run task
application {
    mainClass.set("com.spand.teamrank.Application")  // Make sure this matches your main class
}

tasks.named<JavaExec>("run") {
    // Pass command-line arguments to the Scala app via the Gradle `run` task
    args = project.findProperty("args")?.toString()?.split(",") ?: listOf()
}
