plugins {
    kotlin("jvm") version "2.2.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.choco-solver:choco-solver:4.10.14")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
    compilerOptions{
        freeCompilerArgs.add("-Xnested-type-aliases")
    }
}

application {
    mainClass.set("MainKt")
}