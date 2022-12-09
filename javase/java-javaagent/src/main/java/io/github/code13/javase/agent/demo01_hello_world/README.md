# javaagent 简单 demo

### 新建类：

```java
/**
 * HelloAgent.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/13 21:39
 */
public class HelloAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("Hello! JavaAgent " + agentArgs);
  }
}
```

### 测试类：

```java
public class HelloAgentTest {

  public static void main(String[] args) {
    System.out.println("hi HelloAgent");
  }
}
```

### Gradle 配置：

```groovy
def PremainClass = 'io.github.code13.javase.agent.demo01_hello_world.HelloAgent'
jar {
    manifest {
        attributes(
                'Premain-Class': PremainClass
        )
    }
}
```

### 配置信息

1. 配置位置：Run/Debug Configurations -> VM options
2. 配置内容：-javaagent:
   /Users/u0039724/workspace/opensource/java/java-stack-labs/java-labs/java-javaagent/build/libs/java-javaagent-1.0-SNAPSHOT-plain.jar=testargs
