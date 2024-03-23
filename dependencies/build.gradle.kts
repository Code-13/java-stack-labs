plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:3.2.4"))
    api(platform("org.springframework.cloud:spring-cloud-dependencies:2023.0.0"))
    constraints {
        api("com.baomidou:mybatis-plus-generator:3.5.5")
        api("com.baomidou:mybatis-plus-boot-starter:3.5.5")
        api("com.baomidou:mybatis-plus-core:3.5.5")
        api("com.baomidou:mybatis-plus-extension:3.5.5")
        api("com.baomidou:mybatis-plus-extension:3.5.5")
        api("com.baomidou:mybatis-plus-annotation:3.5.5")

        api("com.easy-query:sql-mysql:1.10.44")
        api("com.easy-query:sql-api-proxy:1.10.44")
        api("com.easy-query:sql-processor:1.10.44")

        api("org.mapstruct:mapstruct:1.5.5.Final")
        api("oorg.mapstruct:mapstruct-processor:1.5.5.Final")

        api("com.alibaba:druid-spring-boot-starter:1.2.22")
        api("com.alibaba:druid:1.2.22")

        api("io.vavr:vavr:0.10.4")
        api("io.vavr:vavr-jackson:0.10.3")
        api("io.vavr:vavr-test:0.10.3")
        api("io.vavr:vavr-beanvalidation2:0.10.0")

        api("commons-io:commons-io:2.15.1")
        api("org.apache.commons:commons-collections4:4.4")
        api("commons-beanutils:commons-beanutils:1.9.4")
        api("commons-fileupload:commons-fileupload:1.5")
        api("commons-net:commons-net:3.10.0")
        api("commons-codec:commons-codec:1.16.1")

        api("org.apache.pdfbox:pdfbox:3.0.2")
        api("org.apache.pdfbox:fontbox:3.0.2")
        api("org.apache.pdfbox:preflight:3.0.2")
        api("org.apache.pdfbox:pdfbox-tools:3.0.2")

        api("org.jsoup:jsoup:1.17.2")
        api("org.reflections:reflections:0.10.2")
        api("com.googlecode.libphonenumber:libphonenumber:8.13.33")
        api("org.locationtech.spatial4j:spatial4j:0.8")
        api("com.google.zxing:core:3.5.3")

        api("net.datafaker:datafaker:2.1.0")
        api("org.objenesis:objenesis:3.3")
        api("io.protostuff:protostuff-core:1.8.0")
        api("io.protostuff:protostuff-runtime:1.8.0")
        api("com.alibaba:easyexcel:3.3.4")

        api("org.redisson:redisson:3.27.2")
        api("org.redisson:redisson-spring-boot-starter:3.27.2")
        api("org.redisson:redisson-mybatis:3.27.2")
        api("org.redisson:redisson-spring-data-32:3.27.2")

        api("org.openjdk.jmh:jmh-core:1.37")
        api("org.openjdk.jmh:jmh-generator-annprocess:1.37")

        api("com.github.ben-manes.caffeine:caffeine:3.1.8")
        api("com.github.ben-manes.caffeine:guava:3.1.8")
        api("com.github.ben-manes.caffeine:jcache:3.1.8")

        api("com.google.guava:guava-testlib:33.1.0-jre")

        api("com.nimbusds:nimbus-jose-jwt:9.37.3")
    }
}
