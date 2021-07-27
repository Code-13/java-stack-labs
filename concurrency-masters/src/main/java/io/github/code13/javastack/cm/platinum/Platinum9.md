# 王者并发课-铂金9：互通有无-Exchanger如何完成线程间的数据交换

欢迎来到《[王者并发课](https://juejin.cn/post/6967277362455150628 "https://juejin.cn/post/6967277362455150628")》，本文是该系列文章中的**第22篇**，铂金中的**第9篇**。

在前面的文章中，我们已经介绍了[ReentrantLock](https://juejin.cn/post/6975794857675587620 "https://juejin.cn/post/6975794857675587620")，[CountDownLatch](https://juejin.cn/post/6979565807336423431/ "https://juejin.cn/post/6979565807336423431/")，[CyclicBarrier](https://juejin.cn/post/6981094209440710692 "https://juejin.cn/post/6981094209440710692")，[Semaphore](https://juejin.cn/post/6976063081751248909/ "https://juejin.cn/post/6976063081751248909/")等同步工具。在本文中，将为你介绍最后一个同步工具，即**Exchanger**
.

**Exchanger用于两个线程在某个节点时进行数据交换**
。在用法上，Exchanger并不复杂，但是实现上会稍微有点费解。所以，考虑到Exchanger在平时使用的场景并不多，况且多数读者对一些“枯燥”的源码的耐受度有限（可能引起不适或烦躁等不良情绪，阻碍学习），本文将侧重讲它的使用和思想，对于源码不会过多展开，点到为止。

一、Exchanger的使用场景
----------------

在峡谷中，铠和兰陵王都是擅长打野的英雄，各自对野怪的偏好也不完全相同。所以，为了能得到自己想要的野怪，他们经常会在峡谷的交易中心**交换**各自的猎物。

这一天，铠打到了一只**棕熊**，而兰陵王则收获了一只**野狼**，并且彼此都想要对方的野怪。于是，**他们约定在峡谷交易中心交换双方的野怪，谁先到了就先等会**。这个过程，可以用下面这幅图来表示：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/15707c5caf0944d1a40c5f2f6023c5a6~tplv-k3u1fbpfcp-zoom-1.image)

在铠和兰陵王交换猎物的过程中，有三个点需要你留意：

* **交换的双方有明确的交易地点**（峡谷交易中心）；
* **交换的双方具有明确的交易对象**（比如棕熊和野狼）；
* **谁先到了就等会儿**（他们中总会有先来后到）。

如果用代码来实现的话，也是有多种方式可以选择，比如前面所学过的同步方法等。不过，虽然做也是可以做的，只是没那么方便。所以，接下来我们就用**Exchanger**来实现这一过程。

在下面的代码中，我们定义了一个`exchanger`，它就类似于峡谷交易中心，而它的类型`Exchanger<WildMonster>` 则明确表示交换的对象是**野怪**。

接着，我们再定义两个线程，分别代表**铠**和**兰陵王**。在其线程的内部，会通过前面定义的`exchanger`对象来和对方进行交换数据。**交换完成后，他们彼此将获得对方的物品**。

```java
 public static void main(String[]args){
    Exchanger<WildMonster> exchanger=new Exchanger<> ();

    Thread 铠=newThread("铠",()->{
    try{
    WildMonster wildMonster=new Bear("棕熊");
    say("我手里有一只："+wildMonster.getName());
    WildMonster exchanged=exchanger.exchange(wildMonster);
    say("交易完成，我获得了：",wildMonster.getName(),"->",exchanged.getName());
    }catch(InterruptedException e){
    e.printStackTrace();
    }
    });

    Thread 兰陵王=newThread("兰陵王",()->{
    try{
    WildMonster wildMonster=new Wolf("野狼");
    say("我手里有一只："+wildMonster.getName());
    WildMonster exchanged=exchanger.exchange(wildMonster);
    say("交易完成，我获得了：",wildMonster.getName(),"->",exchanged.getName());
    }catch(InterruptedException e){
    e.printStackTrace();
    }
    });
    铠.start();
    兰陵王.start();
    }

```

下面是上面代码用到的内部类：

```java

@Data
private static class WildMonster {

  protected String name;
}

private static class Wolf extends WildMonster {

  public Wolf(String name) {
    this.name = name;
  }
}

private static class Bear extends WildMonster {

  public Bear(String name) {
    this.name = name;
  }
}

```

示例代码运行结果如下：

```java
铠:我手里有一只：棕熊
    兰陵王:我手里有一只：野狼
    兰陵王:交易完成，我获得了：野狼->棕熊
    铠:交易完成，我获得了：棕熊->野狼

    Process finished with exit code 0
```

从结果中可以看到，**铠用棕熊换到了野狼，而兰陵王则用野狼换到了棕熊**，他们完成了交换。

以上就是Exchanger的用法，看起来还是非常简单的，事实上也确实很简单。在使用Exchanger的时候要注意下面几点：

* **定义Exchanger对象，各线程通过这个对象完成交换**；
* **在Exchanger对象中要定义类型，也就是这两个线程要交换什么**；
* **线程在调用Exchanger进行交换时，要特别注意的是，先到的那个线程会原地等待另外一个线程的出现**
  。比如，铠先到交换地点，可这时候兰陵王还没有到，那么铠会等待兰陵王的出现，除非超过设置的时间限制，比如兰陵王中途被妲己蹲了草丛。反之亦然，兰陵王先到也到等铠的出现。

二、Exchanger的源码与实现
-----------------

虽然理解Exchanger的思想很容易，了解其用法也很简单，但是若要理清它几百余行的源码却并非易事。其原因在于，**槽是Exchanger中的核心概念和属性**，Exchanger中的数据交换分为**
单槽交换**和**多槽交换**，其中单槽交换源码简单，**但多槽交换却很复杂**
。所以，下文对Exchanger源码的阐述以概括为主，不会对源码深究。如果你有兴趣，可以参考阅读[这篇](https://link.juejin.cn/?target=https%3A%2F%2Fwww.cnblogs.com%2F54chensongxia%2Fp%2F12877843.html "https://www.cnblogs.com/54chensongxia/p/12877843.html")文章，作者对其源码的解读较为详细。

### 1\. 核心构造

与其他同步工具不同的是，**Exchanger有且仅有一个构造函数**。在这个构造中，也只初始化了一个对象`participant`.

```java
public Exchanger(){
    participant=new Participant();
    }
```

从继承关系看，Participant本质上是一个ThreadLocal，而其中的Node则是线程的本地变量。

```java
static final class Participant extends ThreadLocal<Node> {

  public Node initialValue() {
    return new Node();
  }
}
```

### 2\. 核心属性

Exchanger有四个核心变量，如下所示。当然，除此之外，还有一些用以计算的其他变量。不过，为避免引入不必要的复杂度，本文暂不提及。

```java

private final Participant participant;

private volatile Node[]arena;

private volatile Node slot;

private volatile int bound;

```

Node的具体细节，注意其中的item和match.

```java

@sun.misc.Contended
static final class Node {

  int index;
  int bound;
  int collides;
  int hash;
  Object item;
  volatile Object match;
  volatile Thread parked;
}
```

### 3\. 核心方法

```java



public V exchange(V x)throws InterruptedException{...}

public V exchange(V x,long timeout,TimeUnit unit)throws InterruptedException,TimeoutException{...}
```

所以，从源码上看上文的示例，那么铠和兰陵王交换数据的过程应该是下面这样的：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ea2b593d0b1a4c5bb5cab23f2af53474~tplv-k3u1fbpfcp-zoom-1.image)

小结
--

以上就是关于Exchanger的全部内容。在学习Exchanger时，要侧重理解它所要解决的问题场景，以及它的基本用法。对于其源码，当前阶段可以选择“**不求甚解**
”，以降维的方式降低学习难度，日后再循序渐进理解。我在写本文时，也曾多次考虑是否要讲清楚源码，最终还是决定暂缓，毕竟现阶段理解它、学会它才是重点。

正文到此结束，恭喜你又上了一颗星✨

夫子的试炼
-----

* 使用Exchanger实现生产者与消费者。

延伸阅读与参考资料
---------

* [www.cnblogs.com/54chensongx…](https://link.juejin.cn/?target=https%3A%2F%2Fwww.cnblogs.com%2F54chensongxia%2Fp%2F12877843.html "https://www.cnblogs.com/54chensongxia/p/12877843.html")
* [《王者并发课》大纲与更新进度总览](https://juejin.cn/post/6967277362455150628 "https://juejin.cn/post/6967277362455150628")

关于作者
----

关注【**技术八点半**】，及时获取文章更新。传递有品质的技术文章，记录平凡人的成长故事，偶尔也聊聊生活和理想。早晨8：30推送作者品质原创，晚上20：30推送行业深度好文。

如果本文对你有帮助，欢迎**点赞**、**关注**、**监督**，我们一起**从青铜到王者**。