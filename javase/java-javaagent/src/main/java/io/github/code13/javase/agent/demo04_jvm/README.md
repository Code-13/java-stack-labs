# JVMGC 信息打印

`JvmStack`

```java
public class JvmStack {

  private static final long MB = 1048576L;

  static void printMemoryInfo() {
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

    String info = """
        init: {1}\t max: {2}\t used: {3}\t committed: {4}\t use rate: {5}
        """
        .replace("{1}", formatUse(heapMemoryUsage.getInit()))
        .replace("{2}", formatUse(heapMemoryUsage.getMax()))
        .replace("{3}", formatUse(heapMemoryUsage.getUsed()))
        .replace("{4}", formatUse(heapMemoryUsage.getCommitted()))
        .replace("{5}", (heapMemoryUsage.getUsed() * 100 / heapMemoryUsage.getCommitted()) + "%");

    System.out.println(info);

    MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

    info = """
        init: {1}\t max: {2}\t used: {3}\t committed: {4}\t use rate: {5}
        """
        .replace("{1}", formatUse(nonHeapMemoryUsage.getInit()))
        .replace("{2}", formatUse(nonHeapMemoryUsage.getMax()))
        .replace("{3}", formatUse(nonHeapMemoryUsage.getUsed()))
        .replace("{4}", formatUse(nonHeapMemoryUsage.getCommitted()))
        .replace("{5}",
            (nonHeapMemoryUsage.getUsed() * 100 / nonHeapMemoryUsage.getCommitted()) + "%");

    System.out.println(info);
  }

  static void printGCInfo() {
    List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
    for (GarbageCollectorMXBean bean : garbageCollectorMXBeans) {
      String info = """
          name: {1}\t count: {2}\t took: {3}\t pool name: {4}
          """
          .replace("{1}", bean.getName())
          .replace("{2}", String.valueOf(bean.getCollectionCount()))
          .replace("{3}", String.valueOf(bean.getCollectionTime()))
          .replace("{4}", Arrays.deepToString(bean.getMemoryPoolNames()));
      System.out.println(info);
    }
  }

  static String formatUse(long use) {
    return (use / MB) + "MB";
  }

}
```

`JvmStackAgent`

```java
public class JvmStackAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("this is jvm agent: " + agentArgs);

    Executors.newScheduledThreadPool(1)
        .scheduleAtFixedRate(
            () -> {
              JvmStack.printMemoryInfo();
              JvmStack.printGCInfo();
              System.out.println(
                  "============================================================================");
            },
            0,
            5000,
            TimeUnit.MILLISECONDS);
  }
}
```

`JvmStackAgentTest`

```java
class JvmStackAgentTest {

  public static void main(String[] args) {
    while (true) {
      List<Object> list = new LinkedList<>();
      list.add("嗨！JavaAgent");
      list.add("嗨！JavaAgent");
      list.add("嗨！JavaAgent");
    }
  }
}
```

`build.gradle`

```groovy
def JvmAgentPremainClass = 'io.github.code13.javase.agent.demo04_jvm.JvmStackAgent'
jar {
    manifest {
        attributes(
                'Premain-Class': JvmAgentPremainClass
        )
    }
}
```

## 配置信息

1. 配置位置：Run/Debug Configurations -> VM options
2. 配置内容：-javaagent:
   /Users/u0039724/workspace/opensource/java/java-stack-labs/java-labs/java-javaagent/build/libs/java-javaagent-1.0-SNAPSHOT-plain.jar=testargs