# ThreadLocal 链路追踪

`TrackContext`

```java
public class TrackContext {

  private static final ThreadLocal<String> trackLocal = new ThreadLocal<>();

  public static void clear() {
    trackLocal.remove();
  }

  public static String getLinkId() {
    return trackLocal.get();
  }

  public static void setLinkId(String linkId) {
    trackLocal.set(linkId);
  }
}
```

`TrackManager`

```java
public class TrackManager {

  private static final ThreadLocal<Stack<String>> track = new ThreadLocal<>();

  private static String createSpan() {
    Stack<String> stack = track.get();
    if (stack == null) {
      stack = new Stack<>();
      track.set(stack);
    }
    String linkId;
    if (stack.isEmpty()) {
      linkId = TrackContext.getLinkId();
      if (linkId == null) {
        linkId = "nvl";
        TrackContext.setLinkId(linkId);
      }
    } else {
      linkId = stack.peek();
      TrackContext.setLinkId(linkId);
    }
    return linkId;
  }

  public static String createEntrySpan() {
    String span = createSpan();
    Stack<String> stack = track.get();
    stack.push(span);
    return span;
  }

  public static String getExitSpan() {
    Stack<String> stack = track.get();
    if (stack == null || stack.isEmpty()) {
      TrackContext.clear();
      return null;
    }
    return stack.pop();
  }

  public static String getCurrentSpan() {
    Stack<String> stack = track.get();
    if (stack == null || stack.isEmpty()) {
      return null;
    }
    return stack.peek();
  }
}
```

`TrackAdvice`

```java
public class TrackAdvice {

  @Advice.OnMethodEnter()
  public static void enter(
      @Advice.Origin("#t") String className, @Advice.Origin("#m") String methodName) {
    String linkId = TrackManager.getCurrentSpan();
    if (linkId == null) {
      linkId = UUID.randomUUID().toString();
      TrackContext.setLinkId(linkId);
    }

    String entrySpan = TrackManager.createEntrySpan();
    System.out.println("链路追踪：" + entrySpan + " " + className + "." + methodName);
  }

  @Advice.OnMethodEnter()
  public static void exit(
      @Advice.Origin("#t") String className, @Advice.Origin("#m") String methodName) {
    TrackManager.getExitSpan();
  }
}
```

`TrackAgent`

```java
public class TrackAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("基于javaagent链路追踪");

    AgentBuilder agentBuilder = new Default();

    AgentBuilder.Transformer transformer =
        (builder, typeDescription, classLoader, module) ->
            builder.visit(
                Advice.to(TrackAdvice.class)
                    .on(
                        ElementMatchers.isMethod()
                            .and(ElementMatchers.any())
                            .and(ElementMatchers.not(ElementMatchers.nameStartsWith("main")))));

    agentBuilder =
        agentBuilder
            .type(
                ElementMatchers.nameStartsWith(
                    "io.github.code13.javase.agent.demo05_threadlocal_track"))
            .transform(transformer)
            .asTerminalTransformation();

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
            System.out.println("onTransformation：" + typeDescription);
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

    agentBuilder.with(listener).installOn(inst);
  }
}
```

`TrackAgentTest`

```java
public class TrackAgentTest {

  public static void main(String[] args) {
    // 线程一
    new Thread(() -> new TrackAgentTest().http_lt1()).start();

    // 线程二
    new Thread(
        () -> {
          new TrackAgentTest().http_lt1();
        })
        .start();
  }

  public void http_lt1() {
    System.out.println("测试结果：hi1");
    http_lt2();
  }

  public void http_lt2() {
    System.out.println("测试结果：hi2");
    http_lt3();
  }

  public void http_lt3() {
    System.out.println("测试结果：hi3");
  }
}
```

`build.gradle`

```groovy
def TrackAgentPremainClass = 'io.github.code13.javase.agent.demo05_threadlocal_track.TrackAgent'
jar {
    manifest {
        attributes(
                'Premain-Class': TrackAgentPremainClass
        )
    }
}
```

## 配置信息

1. 配置位置：Run/Debug Configurations -> VM options
2. 配置内容：-javaagent:
   /Users/u0039724/workspace/opensource/java/java-stack-labs/java-labs/java-javaagent/build/libs/java-javaagent-1.0-SNAPSHOT-plain.jar=testargs