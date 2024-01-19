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

    implementation("org.junit.jupiter:junit-jupiter")

    compileOnly("jakarta.servlet:jakarta.servlet-api")
}
