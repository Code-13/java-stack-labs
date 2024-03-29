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

pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "java-stack-labs"

include("dependencies")

// javase
include("javase:java-annotation")
include("javase:java-annotation-processor")
include("javase:java-base")
include("javase:java-concurrent")
include("javase:java-enums")
include("javase:java-javaagent")
include("javase:java-nio")
include("javase:java-reflect")
include("javase:java-spi")
include("javase:java-time")

include("javase:jdk-new-api-features")
findProject(":javase:jdk-new-api-features")?.name = "jdk-new-api-features"

// libs
include("prettier-libs")
include("prettier-libs:vavr")
findProject(":prettier-libs:vavr")?.name = "vavr"
include("prettier-libs:jsqlparser")
findProject(":prettier-libs:jsqlparser")?.name = "jsqlparser"
include("prettier-libs:okhttp3")
findProject(":prettier-libs:okhttp3")?.name = "okhttp3"
include("prettier-libs:zxing")
findProject(":prettier-libs:zxing")?.name = "zxing"
include("prettier-libs:spatial4j")
findProject(":prettier-libs:spatial4j")?.name = "spatial4j"
include("prettier-libs:log4j2")
findProject(":prettier-libs:log4j2")?.name = "log4j2"
include("prettier-libs:logback")
findProject(":prettier-libs:logback")?.name = "logback"
include("prettier-libs:redisson")
findProject(":prettier-libs:redisson")?.name = "redisson"
include("prettier-libs:javafaker")
findProject(":prettier-libs:javafaker")?.name = "javafaker"
include("prettier-libs:protostuff")
findProject(":prettier-libs:protostuff")?.name = "protostuff"
include("prettier-libs:objenesis")
findProject(":prettier-libs:objenesis")?.name = "objenesis"
include("prettier-libs:easyexcel")
findProject(":prettier-libs:easyexcel")?.name = "easyexcel"
include("prettier-libs:nimbus-jose-jwt")
findProject(":prettier-libs:nimbus-jose-jwt")?.name = "nimbus-jose-jwt"
include("prettier-libs:itextpdf")
findProject(":prettier-libs:itextpdf")?.name = "itextpdf"
include("prettier-libs:cglib")
findProject(":prettier-libs:cglib")?.name = "cglib"
include("prettier-libs:javassist")
findProject(":prettier-libs:javassist")?.name = "javassist"
include("prettier-libs:bytebuddy")
findProject(":prettier-libs:bytebuddy")?.name = "bytebuddy"
include("prettier-libs:jackson")
findProject(":prettier-libs:jackson")?.name = "jackson"
include("prettier-libs:caffeine")
findProject(":prettier-libs:caffeine")?.name = "caffeine"
include("prettier-libs:json-path")
findProject(":prettier-libs:json-path")?.name = "json-path"
include("prettier-libs:datafaker")
findProject(":prettier-libs:datafaker")?.name = "datafaker"
include("prettier-libs:pdfbox")
findProject(":prettier-libs:pdfbox")?.name = "pdfbox"
include("prettier-libs:jsoup")
findProject(":prettier-libs:jsoup")?.name = "jsoup"
include("prettier-libs:apache-httpclient5")
findProject(":prettier-libs:apache-httpclient5")?.name = "apache-httpclient5"
include("prettier-libs:ip2region")
findProject(":prettier-libs:ip2region")?.name = "ip2region"
include("prettier-libs:alibaba-druid")
findProject(":prettier-libs:alibaba-druid")?.name = "alibaba-druid"
include("prettier-libs:apache-poi")
findProject(":prettier-libs:apache-poi")?.name = "apache-poi"

// jvm
include("jvm")
include("jvm:mat")
findProject(":jvm:mat")?.name = "mat"

// books
include("books")
include("books:modern-java-in-action")
findProject(":books:modern-java-in-action")?.name = "modern-java-in-action"
include("books:java-concurrency-in-practice")
findProject(":books:java-concurrency-in-practice")?.name = "java-concurrency-in-practice"
include("books:understanding-the-jvm-3edition")
findProject(":books:understanding-the-jvm-3edition")?.name = "understanding-the-jvm-3edition"
include("books:effective-java-3edition")
findProject(":books:effective-java-3edition")?.name = "effective-java-3edition"

// frameworks
include("prettier-frameworks")
include("prettier-frameworks:netty4")
findProject(":prettier-frameworks:netty4")?.name = "netty4"
include("prettier-frameworks:mybatis-plus")
findProject(":prettier-frameworks:mybatis-plus")?.name = "mybatis-plus"
include("prettier-frameworks:open-feign")
findProject(":prettier-frameworks:open-feign")?.name = "open-feign"
include("prettier-frameworks:easy-query")
findProject(":prettier-frameworks:easy-query")?.name = "easy-query"

// spring
include("spring")
include("spring:spring-framework")
findProject(":spring:spring-framework")?.name = "spring-framework"
include("spring:spring-data")
findProject(":spring:spring-data")?.name = "spring-data"
include("spring:spring-security")
findProject(":spring:spring-security")?.name = "spring-security"
include("spring:spring-security-oauth2")
findProject(":spring:spring-security-oauth2")?.name = "spring-security-oauth2"
include("spring:spring-security-oauth2:authorization-server")
findProject(":spring:spring-security-oauth2:authorization-server")?.name = "authorization-server"
include("spring:spring-security-oauth2:authorization-server-web")
findProject(":spring:spring-security-oauth2:authorization-server-web")?.name =
    "authorization-server-web"
include("spring:spring-security-oauth2:oauth2-client")
findProject(":spring:spring-security-oauth2:oauth2-client")?.name = "oauth2-client"
include("spring:spring-security-oauth2:resource-server")
findProject(":spring:spring-security-oauth2:resource-server")?.name = "resource-server"
include("spring:spring-kafka")
findProject(":spring:spring-kafka")?.name = "spring-kafka"
include("spring:spring-asciidoctor-backends")
findProject(":spring:spring-asciidoctor-backends")?.name = "spring-asciidoctor-backends"

// spring boot
include("spring:spring-boot")
findProject(":spring:spring-boot")?.name = "spring-boot"
include("spring:spring-boot:custom-aop-annotation-with-spel")
findProject(":spring:spring-boot:custom-aop-annotation-with-spel")?.name =
    "custom-aop-annotation-with-spel"
include("spring:spring-boot:dynamic-register-request-mapping-handler-mapping")
findProject(":spring:spring-boot:dynamic-register-request-mapping-handler-mapping")?.name =
    "dynamic-register-request-mapping-handler-mapping"
include("spring:spring-boot:custom-404-error")
findProject(":spring:spring-boot:custom-404-error")?.name = "custom-404-error"
include("spring:spring-boot:caffeine-redis-multi-level-cache")
findProject(":spring:spring-boot:caffeine-redis-multi-level-cache")?.name =
    "caffeine-redis-multi-level-cache"
include("spring:spring-boot:jasypt-spring-boot")
findProject(":spring:spring-boot:jasypt-spring-boot")?.name = "jasypt-spring-boot"
include("spring:spring-boot:spring-boot-websocket")
findProject(":spring:spring-boot:spring-boot-websocket")?.name = "spring-boot-websocket"

// columns
include("columns")
include("columns:concurrency-kings")
findProject(":columns:concurrency-kings")?.name = "concurrency-kings"
include("columns:bugstack")
include("columns:bugstack:small-spring")
findProject(":columns:bugstack:small-spring")?.name = "small-spring"

// tests-frameworks
include("tests-frameworks")
include("tests-frameworks:jmh")
findProject(":tests-frameworks:jmh")?.name = "jmh"
include("tests-frameworks:junit5")
findProject(":tests-frameworks:junit5")?.name = "junit5"
include("tests-frameworks:mockito")
findProject(":tests-frameworks:mockito")?.name = "mockito"

// jakartaee
include("jakartaee")
include("jakartaee:jakarta-bean-validation")
findProject(":jakartaee:jakarta-bean-validation")?.name = "jakarta-bean-validation"

include("design-pattern")
