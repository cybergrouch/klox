import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    kotlin("jvm") version "1.5.31"
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.jmailen.kotlinter") version "3.7.0"
}

group = "org.lange"
version = "1.0-SNAPSHOT"

var koin_version = "3.1.4"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Koin Core features
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.github.microutils:kotlin-logging:2.0.8")
    implementation("org.slf4j:slf4j-simple:1.7.32")

    testImplementation(kotlin("test"))
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit5:$koin_version")


}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow-klox")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "org.lange.interpreters.klox.KLox"))
        }
    }

    named<LintTask>("lintKotlinMain") {
        exclude("**/generated/*.kt")
    }

    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "11"
    }
}

application {
    mainClass.set("org.lange.interpreters.klox.MainKt")
}