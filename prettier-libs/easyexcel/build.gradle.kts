// 1. 需要在 idea 中 使用 modify run configuration 将 JDK 设为 JDK8
// 2. 需要在 idea 中 将该模块的 Language Level 设为 8

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    //https://www.yuque.com/easyexcel
    testImplementation("com.alibaba:easyexcel")
    testImplementation("ch.qos.logback:logback-classic")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.apache.commons:commons-lang3")
}
