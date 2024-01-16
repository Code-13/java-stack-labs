plugins {
  id("com.github.node-gradle.node")
}

node {
  download.value(false)
  nodeProxySettings.value(com.github.gradle.node.npm.proxy.ProxySettings.OFF)
}

val cleanBuild = tasks.register<Delete>("cleanBuild") {
  delete("build")
  delete("dist")
  delete("../authorization-server/src/main/resources/site")
}

val cleanAll = tasks.register<Delete>("cleanAll") {
  dependsOn(cleanBuild)
  delete("node_modules")
  delete(".gradle")
}

val pnpmBuild = tasks.register<com.github.gradle.node.pnpm.task.PnpmTask>("pnpmBuild") {
  //dependsOn pnpmInstall
  dependsOn(cleanBuild)
  args.value(listOf("run", "build"))
  outputs.dir("dist")
  outputs.upToDateWhen {
    true
  }
}

val buildWeb = tasks.register<Copy>("buildWeb") {
  dependsOn(pnpmBuild)
  from("dist") {
    eachFile {
      println(path)
    }
  }
  into("../authorization-server/src/main/resources/site")
}

val buildWebWithoutBuild = tasks.register<Copy>("buildWebWithoutBuild") {
  delete("../authorization-server/src/main/resources/site")

  from("dist") {
    eachFile {
      println(path)
    }
  }
  into("../authorization-server/src/main/resources/site")
}
