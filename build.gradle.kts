/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    `kotlin-dsl`
    id("org.springframework.boot") version "3.2.4" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("com.github.node-gradle.node") version "7.0.2" apply false
    id("io.freefair.lombok") version "8.4" apply false
    id("org.checkerframework") version "0.6.37" apply false
}

allprojects {
    group = "io.github.code13"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.aliyun.com/repository/public");
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/spring");
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/spring-plugin");
        }
        maven {
            url = uri("https://repo.spring.io/release");
        }
        maven {
            url = uri("https://repo.spring.io/milestone");
        }
        mavenCentral()
    }
}

configure(subprojects.filter { project -> project != project(":dependencies") }) {

//  apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "idea")
    //apply(plugin="maven-publish")
    //apply(plugin="org.springframework.boot")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "org.checkerframework")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        withSourcesJar()
        withJavadocJar()
    }

    val processResources = tasks.withType<ProcessResources>() {
        from("src/main/java") {
            include("**/*.xml", "**/*.json", "**/*.yml", "**/*.yaml")
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
        options.forkOptions.jvmArgs?.add("--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED")
        options.forkOptions.jvmArgs?.add("--add-opens=java.base/java.lang=ALL-UNNAMED")
        dependsOn(processResources)
    }

    tasks.withType<Test>() {
        useJUnitPlatform()
        testLogging.showExceptions = true
    }

    tasks.withType<Javadoc>() {
        afterEvaluate {
            options {
                encoding("UTF-8")
                charset("UTF-8")
            }
        }
    }

    tasks.withType<Jar>() {
        into("META-INF/") {
            from(rootProject.file("LICENSE"))
        }
        afterEvaluate {
            manifest {
                attributes(
                    mapOf(
                        "Implementation-Title" to archiveBaseName,
                        "Implementation-Version" to archiveVersion,
                        "Built-Gradle" to gradle.gradleVersion,
                        "Build-OS" to System.getProperty("os.name"),
                        "Build-BY" to System.getProperty("user.name"),
                        "Build-Jdk" to System.getProperty("java.version"),
                        "Build-Timestamp" to LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                );
            }
        }
    }

    configurations {
        "implementation" {
            resolutionStrategy.failOnVersionConflict()
        }
    }

    dependencies {
        "api"(platform(project(":dependencies")))

        "compileOnly"("org.slf4j:slf4j-api")
        "testImplementation"("org.slf4j:slf4j-api")

        "testImplementation"("org.junit.jupiter:junit-jupiter-api")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
    }

}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
