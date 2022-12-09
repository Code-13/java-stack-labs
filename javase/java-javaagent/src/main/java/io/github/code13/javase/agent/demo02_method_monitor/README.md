# 修改字节码添加监控方法执行耗时

使用 javassist

`MonitorAgent`

```java
public class MonitorAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("method monitor agent:  " + agentArgs);
    inst.addTransformer(new MonitorTransformer());
  }
}
```

`MonitorTransformer`

```java
public class MonitorTransformer implements ClassFileTransformer {

  private static final Set<String> classNameSet = new HashSet<>();

  static {
    classNameSet.add(
        "io.github.code13.javase.agent.demo02_method_monitor.MonitorAgentTest");
  }

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
      throws IllegalClassFormatException {

    try {
      String currentClassName = className.replace("/", ".");
      if (!classNameSet.contains(currentClassName)) { // 提升classNameSet中含有的类
        return null;
      }
      System.out.println("transform: [" + currentClassName + "]");

      CtClass ctClass = ClassPool.getDefault().get(currentClassName);

      if (ctClass.isFrozen()) {
        return null;
      }

      CtBehavior[] methods = ctClass.getDeclaredBehaviors();
      for (CtBehavior method : methods) {
        enhanceMethod(method);
      }

      return ctClass.toBytecode();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private void enhanceMethod(CtBehavior method) throws CannotCompileException {
    if (method.isEmpty()) {
      return;
    }
    String name = method.getName();
    if ("main".equalsIgnoreCase(name)) {
      return;
    }

    String source = """
        {
          long start = System.nanoTime();
          $_ = $proceed($$);
          System.out.println("method:[{1}]");
          System.out.println("cost: " + (System.nanoTime() - start));
        }
        """.replace("{1}", name);

    method.instrument(new ExprEditor() {
      @Override
      public void edit(MethodCall m) throws CannotCompileException {
        m.replace(source);
      }
    });
  }
}
```

`MonitorAgentTest`

```java
class MonitorAgentTest {

  public static void main(String[] args) {
    MonitorAgentTest agent = new MonitorAgentTest();
    agent.echoHi();
  }

  private void echoHi() {
    System.out.println("hi agent");
  }
}
```

`build.gradle`

```groovy
def MonitorPremainClass = 'io.github.code13.javase.agent.demo02_method_monitor.MonitorAgent'
jar {
    manifest {
        attributes(
                'Premain-Class': MonitorPremainClass
        )
    }
}

dependencies {
    implementation 'org.javassist:javassist:3.28.0-GA'
}
```

### 配置信息

1. 配置位置：Run/Debug Configurations -> VM options
2. 配置内容：-javaagent:
   /Users/u0039724/workspace/opensource/java/java-stack-labs/java-labs/java-javaagent/build/libs/java-javaagent-1.0-SNAPSHOT-plain.jar=testargs