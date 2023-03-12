plugins {
    kotlin("js") version "1.8.10"

    id("maven-publish")
}

val kotlinWrapperVersion = project.property("version.wrappers") as String
val reactVersion = project.property("version.react") as String
val buildNumber = System.getenv("GITHUB_RUN_NUMBER")?.toInt()?.plus(7) ?: "local"
group = "io.github.mysticfall"
version = "$reactVersion-$kotlinWrapperVersion+build.$buildNumber"

val isReleasedVersion = !project.version.toString().endsWith("-SNAPSHOT")

repositories {
    mavenCentral()
}

fun versionOf(name: String, isWrapper: Boolean = true): String {
    val artifact = project.property("version.$name") as String

    return if (isWrapper) {
        "$artifact-$kotlinWrapperVersion"
    } else {
        artifact
    }
}

dependencies {
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:${versionOf("react")}")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:${versionOf("react")}")

    implementation(npm("react-test-renderer", versionOf("react", isWrapper = false)))

    testImplementation(kotlin("test-js"))
}

kotlin {
    js(IR) {
        nodejs {
            testTask {
                useMocha()
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("main") {
            from(components["kotlin"])

            if (tasks.names.contains("jsSourcesJar")) {
                artifact(tasks.getByName<Zip>("jsSourcesJar"))
            }

            if (tasks.names.contains("jsIrSourcesJar")) {
                artifact(tasks.getByName<Zip>("jsIrSourcesJar"))
            }

            pom {
                name.set("Kotlin API for React Test Renderer")
                description.set(
                    "Kotlin wrapper for React Test Renderer, which can be used " +
                            "to unit test React components in a Kotlin/JS project."
                )
                url.set("https://github.com/turtton/kotlin-react-test")

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("mysticfall")
                        name.set("Xavier Cho")
                        email.set("mysticfallband@gmail.com")
                    }
                    developer {
                        id.set("turtton")
                        name.set("turtton")
                        email.set("tiny.idea1859@turtton.net")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/turtton/kotlin-react-test.git")
                    developerConnection.set("scm:git:git@github.com:turtton/kotlin-react-test.git")
                    url.set("https://github.com/turtton/kotlin-react-test")
                }
            }
        }
    }

    repositories {
        val targetPath = System.getenv("PUBLISH_PATH")
        if (targetPath != null) {
            maven {
                url = uri(targetPath)
            }
        }
    }
}
