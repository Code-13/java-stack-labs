# bytebuddy 方法监控

`ByteBuddyMonitorAgent`

```java
public class ByteBuddyMonitorAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("byte buddy agent：" + agentArgs);

    AgentBuilder.Transformer transformer =
        (builder, typeDescription, classLoader, module) ->
            builder
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(MethodCostTime.class));

    AgentBuilder.Listener listener =
        new Listener() {
          @Override
          public void onDiscovery(
              String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
          }

          @Override
          public void onTransformation(
              TypeDescription typeDescription,
              ClassLoader classLoader,
              JavaModule module,
              boolean loaded,
              DynamicType dynamicType) {
          }

          @Override
          public void onIgnored(
              TypeDescription typeDescription,
              ClassLoader classLoader,
              JavaModule module,
              boolean loaded) {
          }

          @Override
          public void onError(
              String typeName,
              ClassLoader classLoader,
              JavaModule module,
              boolean loaded,
              Throwable throwable) {
          }

          @Override
          public void onComplete(
              String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
          }
        };

    new AgentBuilder.Default()
        .type(
            ElementMatchers.nameStartsWith(
                "io.github.code13.javase.agent.demo03_bytebuddy_monitor"))
        .transform(transformer)
        .with(listener)
        .installOn(inst);
  }
}
```

`MethodCostTime`

```java
public class MethodCostTime {

  @RuntimeType
  public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable)
      throws Exception {
    long start = System.currentTimeMillis();
    try {
      return callable.call();
    } finally {
      System.out.println(method + " 方法耗时:  " + (System.currentTimeMillis() - start) + "ms");
    }
  }
}
```

`ByteBuddyMonitorAgentTest`

```java
class ByteBuddyMonitorAgentTest {

  public static void main(String[] args) throws InterruptedException {
    ByteBuddyMonitorAgentTest apiTest = new ByteBuddyMonitorAgentTest();
    apiTest.echoHi();
  }

  private void echoHi() throws InterruptedException {
    System.out.println("hi agent");
    Thread.sleep((long) (Math.random() * 500));
  }
}
```

`build.gradle`

```groovy
def ByteBuddyMonitorPremainClass = 'io.github.code13.javase.agent.demo03_bytebuddy_monitor.ByteBuddyMonitorAgent'
jar {
    manifest {
        attributes(
                'Premain-Class': ByteBuddyMonitorPremainClass
        )
    }
}

dependencies {
    // https://mvnrepository.com/artifact/org.javassist/javassist
    implementation 'org.javassist:javassist:3.28.0-GA'
    // https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy
    // implementation 'net.bytebuddy:byte-buddy:1.12.10'
    implementation 'net.bytebuddy:byte-buddy'
}
```

## 配置信息

1. 配置位置：Run/Debug Configurations -> VM options
2. 配置内容：-javaagent:
   /Users/u0039724/workspace/opensource/java/java-stack-labs/java-labs/java-javaagent/build/libs/java-javaagent-1.0-SNAPSHOT-plain.jar=testargs