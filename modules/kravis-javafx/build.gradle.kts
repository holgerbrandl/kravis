import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}


//https://stackoverflow.com/questions/56510154/how-do-i-add-javafx-to-a-gradle-build-file-using-kotlin-dsl
javafx {
    version = "15.0.1"
    modules("javafx.controls", "javafx.swing", "javafx.web")
}

group = "org.github.holgerbrandl"
version = "0.5-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))

    implementation ("no.tornado:tornadofx:1.7.20")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}

application {
    mainClass.set("MainKt")
}