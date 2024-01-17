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

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${properties["springBootVersion"]}")
        classpath("io.spring.gradle:dependency-management-plugin:1.1.4")
        classpath("com.github.node-gradle:gradle-node-plugin:7.0.1")
        classpath("io.freefair.gradle:lombok-plugin:8.4")
        classpath("org.checkerframework:checkerframework-gradle-plugin:0.6.37")
    }
}

plugins {
    `kotlin-dsl`
}

allprojects {
    group = "${properties["GROUPID"]}"
    version = "${properties["VERSION"]}"
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
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        withSourcesJar()
        withJavadocJar()
    }

    val processResources = tasks.withType(ProcessResources::class.java) {
        from("src/main/java") {
            include("**/*.xml", "**/*.json", "**/*.yml", "**/*.yaml")
        }
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
        options.forkOptions.jvmArgs?.add("--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED")
        options.forkOptions.jvmArgs?.add("--add-opens=java.base/java.lang=ALL-UNNAMED")
        dependsOn(processResources)
    }

    tasks.withType(Test::class.java) {
        useJUnitPlatform()
    }

    tasks.withType(Javadoc::class.java) {
        afterEvaluate {
            options {
                encoding("UTF-8")
                charset("UTF-8")
            }
        }
    }

    tasks.withType(Jar::class.java) {
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

    dependencies {
        "api"(platform(project(":dependencies")))

        "compileOnly"("org.slf4j:slf4j-api")
        "testImplementation"("org.slf4j:slf4j-api")

        "testImplementation"("org.junit.jupiter:junit-jupiter-api")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
    }

}
