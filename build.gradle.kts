plugins {
    kotlin("multiplatform") version "2.1.20-RC"

    id("maven-publish")
}

val kotlinWrapperVersion = project.property("version.wrappers") as String
val reactVersion = project.property("version.react") as String
val buildNumber = System.getenv("GITHUB_RUN_NUMBER")?.toInt()?.plus(1) ?: "local"
group = "net.cyberflame"
version = "$reactVersion-$kotlinWrapperVersion+build.$buildNumber"

val isReleasedVersion = !project.version.toString().endsWith("-SNAPSHOT")

repositories {
    mavenCentral()
}

fun versionOf(name: String, isWrapper: Boolean = true): String {
    val artifact = project.property("version.$name") as String

    return if (isWrapper) {
        "$kotlinWrapperVersion-$artifact"
    } else {
        artifact
    }
}

kotlin {
    // withSourcesJar(publish = false)
    js(IR) {
        nodejs {
            testTask {
                useMocha()
            }
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:${versionOf("react")}")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:${versionOf("react")}")
                implementation(npm("react-test-renderer", versionOf("react", isWrapper = false)))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

publishing {
    publications {
         tasks.withType<GenerateModuleMetadata>().configureEach {
            enabled = false
         }
         create<MavenPublication>("main") {

            artifactId = "kotlin-react-test"

            artifact(tasks.getByName<Jar>("jsJar")) {
                classifier = ""
            }

            pom {
                name.set("Kotlin API for React Test Renderer")
                description.set(
                    "Kotlin wrapper for React Test Renderer, which can be used " +
                            "to unit test React components in a Kotlin/JS project."
                )
                url.set("https://github.com/CyberFlameGO/kotlin-react-test")

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("cyberflame")
                        name.set("CyberFlame")
                        email.set("cyberflameu@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/CyberFlameGO/kotlin-react-test.git")
                    developerConnection.set("scm:git:git@github.com:CyberFlameGO/kotlin-react-test.git")
                    url.set("https://github.com/CyberFlameGO/kotlin-react-test")
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
