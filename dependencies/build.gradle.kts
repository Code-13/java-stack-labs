plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:${properties["springBootVersion"]}"))
    api(platform("org.springframework.cloud:spring-cloud-dependencies:${properties["springCloudVersion"]}"))
    constraints {
        api("org.springframework.security:spring-security-oauth2-authorization-server:0.4.3")

        api("org.checkerframework:checker-qual:3.31.0")

        api("com.baomidou:mybatis-plus-generator:3.5.3.1")
        api("com.baomidou:mybatis-plus-boot-starter:3.5.3.1")
        api("com.baomidou:mybatis-plus-core:3.5.3.1")
        api("com.baomidou:mybatis-plus-extension:3.5.3.1")
        api("com.baomidou:mybatis-plus-extension:3.5.3.1")
        api("com.baomidou:mybatis-plus-annotation:3.5.3.1")

        api("org.mapstruct:mapstruct:1.5.3.Final")
        api("oorg.mapstruct:mapstruct-processor:1.5.3.Final")

        api("com.alibaba:druid-spring-boot-starter:1.2.16")
        api("com.alibaba:druid:1.2.16")

        api("io.vavr:vavr:0.10.4")
        api("io.vavr:vavr-jackson:0.10.3")
        api("io.vavr:vavr-test:0.10.3")
        api("io.vavr:vavr-beanvalidation2:0.10.0")

        api("commons-io:commons-io:2.11.0")
        api("org.apache.commons:commons-collections4:4.4")
        api("commons-beanutils:commons-beanutils:1.9.4")
        api("commons-fileupload:commons-fileupload:1.4")
        api("commons-net:commons-net:3.8.0")

        api("org.apache.pdfbox:pdfbox:2.0.27")
        api("org.apache.pdfbox:fontbox:2.0.27")
        api("org.apache.pdfbox:preflight:2.0.27")
        api("org.apache.pdfbox:pdfbox-tools:2.0.27")

        api("org.jsoup:jsoup:1.15.4")
        api("org.reflections:reflections:0.10.2")
        api("com.googlecode.libphonenumber:libphonenumber:8.13.7")
        api("org.locationtech.spatial4j:spatial4j:0.8")
        api("com.google.zxing:core:3.5.1")

        api("net.datafaker:datafaker:1.8.0")
        api("org.objenesis:objenesis:3.3")
        api("io.protostuff:protostuff-core:1.8.0")
        api("io.protostuff:protostuff-runtime:1.8.0")
        api("com.alibaba:easyexcel:3.2.1")

        api("org.redisson:redisson:3.20.0")
        api("org.redisson:redisson-spring-boot-starter:3.20.0")
        api("org.redisson:redisson-mybatis:3.20.0")
        api("org.redisson:redisson-spring-data-27:3.20.0")

        api("org.openjdk.jmh:jmh-core:1.36")
        api("org.openjdk.jmh:jmh-generator-annprocess:1.36")

        api("com.github.ben-manes.caffeine:caffeine:3.1.5")
        api("com.github.ben-manes.caffeine:guava:3.1.5")
        api("com.github.ben-manes.caffeine:jcache:3.1.5")

        api("com.google.guava:guava-testlib:31.1-jre")

        api("com.nimbusds:nimbus-jose-jwt:9.25.1")
    }
}
