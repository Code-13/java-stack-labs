dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.h2database:h2")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    compileOnly("com.nimbusds:oauth2-oidc-sdk:9.35")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.2.4")
}
