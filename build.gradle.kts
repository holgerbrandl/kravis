plugins {
    kotlin("jvm") version "2.2.10"
    `maven-publish`
    signing

    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("org.jetbrains.kotlin.jupyter.api") version "0.16.0-742"
}



group = "com.github.holgerbrandl"
version = "1.1.2"


repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
//    compileOnly("org.jetbrains.kotlin:kotlin-reflect:2.0.21")

    api("org.jetbrains.kotlinx:dataframe-core:1.0.0-Beta4")
    api("com.github.holgerbrandl:kdfutils:1.6.0")
    api("org.apache.commons:commons-math3:3.6.1")

    implementation("org.rosuda.REngine:REngine:2.1.0")
    implementation("org.rosuda.REngine:Rserve:1.8.1")

    testImplementation("junit:junit:4.13.1")
    testImplementation("io.kotest:kotest-assertions-core:5.0.3")
    testImplementation("org.jetbrains.kotlin:kotlin-script-runtime:2.2.10")
}

tasks.processJupyterApiResources {
    libraryProducers = listOf("kravis.device.jupyter.JupyterIntegration")
}

tasks.javadoc {
    exclude("**/PlotResultPanel.java")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("kravis")
                description.set("kravis is a Kotlin wrapper around ggplot2 to enable data visualization on the JVM")
                url.set("https://github.com/holgerbrandl/kravis")
                licenses {
                    license {
                        name.set("BSD-2")
                        url.set("https://github.com/holgerbrandl/kravis/blob/master/LICENSE.txt")
                    }
                }
                developers {
                    developer {
                        id.set("holgerbrandl")
                        name.set("Holger Brandl")
                        email.set("holgerbrandl@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/holgerbrandl/kravis.git")
                    developerConnection.set("scm:git:ssh://github.com/holgerbrandl/kravis.git")
                    url.set("https://github.com/holgerbrandl/kravis.git")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        // see https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#configuration
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

kotlin {
    jvmToolchain(21)
}

