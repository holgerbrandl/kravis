plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
    signing

    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("org.jetbrains.kotlin.jupyter.api") version "0.12.0-285"
}



group = "com.github.holgerbrandl"
version = "1.0.1"


repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
//    compileOnly("org.jetbrains.kotlin:kotlin-reflect:2.0.21")

    api("org.jetbrains.kotlinx:dataframe-core:0.14.1")
    api("com.github.holgerbrandl:kdfutils:1.4.3")
    api("org.apache.commons:commons-math3:3.6.1")

    implementation("org.rosuda.REngine:REngine:2.1.0")
    implementation("org.rosuda.REngine:Rserve:1.8.1")

    testImplementation("junit:junit:4.13.1")
    testImplementation("io.kotest:kotest-assertions-core:5.0.3")
    testImplementation("org.jetbrains.kotlin:kotlin-script-runtime:2.0.21")
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
        sonatype()
//        {
//            snapshotRepositoryUrl.set(uri(project.findProperty("sonatypeStagingProfileId") ?: "not_defined"))
//            username.set(project.findProperty("ossrhUsername")?.toString() ?: "not_defined")
//            password.set(project.findProperty("ossrhPassword")?.toString() ?: "not_defined")
//        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

kotlin {
    jvmToolchain(11)
}

