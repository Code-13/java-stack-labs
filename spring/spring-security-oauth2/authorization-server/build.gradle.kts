plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    //implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    implementation("com.baomidou:mybatis-plus-boot-starter")
    // https://mvnrepository.com/artifact/org.mybatis/mybatis-spring
    implementation("org.mybatis:mybatis-spring:3.0.3")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // see https://github.com/spring-projects/spring-authorization-server/issues/601
    // will remove when upgrade to spring-boot 3 and spring-security 6
    compileOnly("org.springframework.security:spring-security-cas")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("net.sourceforge.htmlunit:htmlunit")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<ProcessResources> {
    dependsOn(":spring:spring-security-oauth2:authorization-server-web:buildWebWithoutBuild")
}
