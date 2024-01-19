dependencies {
    implementation("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.github.javafaker:javafaker:1.0.2") {
        exclude(module = "snakeyaml")
    }

    implementation("org.yaml:snakeyaml")
}
