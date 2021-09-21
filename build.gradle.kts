import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//plugins {
////    kotlin("jvm") version "1.4.10"
//
//}

plugins {
    kotlin("jvm") version "1.5.30" // or kotlin("multiplatform") or any other kotlin plugin
    kotlin("plugin.serialization") version "1.5.30"
    application
}

group = "me.admin"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    implementation("io.ktor:ktor-server-netty:1.4.0")
    implementation("io.ktor:ktor-client-cio:1.4.0")
    implementation("io.ktor:ktor-html-builder:1.4.0")
    implementation("io.ktor:ktor-client-serialization:1.3.2-1.4-M2")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha5")

//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-jvm:0.20.0-1.4-M2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "ServerKt"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}