group = "io.github.code13.spring"

dependencies {
    implementation("org.springframework:spring-core")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-beans")
    implementation("org.springframework:spring-aop")
    implementation("org.aspectj:aspectjweaver")
    implementation("org.springframework:spring-jdbc")
    implementation("com.h2database:h2")
    implementation("org.springframework:spring-web")

    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.zaxxer:HikariCP")
    implementation("org.springframework.boot:spring-boot")
    implementation("com.mysql:mysql-connector-j")
    implementation("org.apache.commons:commons-lang3")

    // https://mvnrepository.com/artifact/com.github.stuxuhai/jpinyin
    implementation("com.github.stuxuhai:jpinyin:1.1.8")

    implementation("org.apache.httpcomponents.client5:httpclient5")
    implementation("org.apache.httpcomponents.client5:httpclient5-fluent")

    // slf4j2 log4j2
    implementation("org.slf4j:slf4j-api")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl")
    implementation("org.apache.logging.log4j:log4j-api")
    implementation("org.apache.logging.log4j:log4j-core")

    compileOnly("jakarta.servlet:jakarta.servlet-api")

    implementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<ProcessResources>() {
    from("src/main/java") {
        include("**/*.sql")
    }
}
