import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30" // or kotlin("multiplatform") or any other kotlin plugin
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.31"
    application
}

allOpen {
    annotation("AllOpen")
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
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.5.31")
    val kotlin_version = "1.5.30"
    val ktor_version = "1.6.3"

    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("org.slf4j:slf4j-api:1.7.32")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("junit:junit:4.13.1")
    implementation("com.xebialabs.restito:restito:0.9.4")
    implementation("org.json:json:20210307")


    testImplementation("org.mockito:mockito-core:2.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.assertj:assertj-core:3.6.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
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