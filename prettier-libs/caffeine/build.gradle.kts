dependencies {
    testImplementation("com.github.ben-manes.caffeine:caffeine")

    // https://mvnrepository.com/artifact/com.google.guava/guava-testlib
    testImplementation("com.google.guava:guava-testlib") {
        exclude(module = "junit")
    }
}