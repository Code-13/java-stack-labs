dependencies {
    implementation("com.easy-query:sql-mysql")
    implementation("com.easy-query:sql-api-proxy")
    annotationProcessor("com.easy-query:sql-processor:2.0.13")

    //runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2")
    implementation("com.zaxxer:HikariCP")

}
