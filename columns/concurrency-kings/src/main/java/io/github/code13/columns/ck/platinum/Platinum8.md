# 王者并发课-铂金8：峡谷幽会-看CyclicBarrier如何跨越重峦叠嶂

欢迎来到《[王者并发课](https://juejin.cn/post/6967277362455150628)》，本文是该系列文章中的**第21篇**，铂金中的**第8篇**。

在上一篇文章中，我们介绍了CountDownLatch的用法。在协调多线程的开始和结束时，CountDownLatch是个非常不错的选择。而本文即将给你介绍的CyclicBarrier则更加有趣，它在能力上和CountDownLatch既有相似之处，又有着明显的不同，值得你一览究竟。本文会先从场景上带你理解问题，再去理解CyclicBarrier提供的方案。

一、CyclicBarrier初体验
------------------

**1\. 峡谷森林里的爱情**

在峡谷的江湖中，不仅有生杀予夺和刀光剑影，还有着美妙的爱情故事。

峡谷战神铠曾经在危急关头救了大乔，这一出英雄救美让他们擦除了爱情的火花，有事没事两人就在峡谷中的各个角落幽会。其中，**峡谷森林**就是他们常去的地方，**
谁先到就等另一个，两人都到齐后，再一起玩耍**。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8cdd8180f3a0412892fb50d2504dbee3~tplv-k3u1fbpfcp-zoom-1.image)

这里头，有两个重点。一是他们要**相互等待**，二是都到齐后再**玩耍**。现在，我们试想一下，如果用代码来模拟这个场景的话，你打算怎么做。有的同学可能会说，两个人（线程）的等待很好处理。**
可是，如果是三人呢**？

所以，这个场景问题可以概括为：**多个线程相互等待，到齐后再执行特定动作**。

接下来，我们就通过CyclicBarrier来模拟解决这个场景的问题，直观感受CyclicBarrier的用法。

在下面这段代码中，我们定义了一个**幽会地点（appointmentPlace）**，以及**大乔**和**铠**
这两个主人公。在他们都达到幽会地点后，我们输出一句包含三朵玫瑰🌹🌹🌹的话来予以确认，给他们送上祝福。

```java
 private static String appointmentPlace="峡谷森林";

public static void main(String[]args){
    CyclicBarrier cyclicBarrier=new CyclicBarrier(2,()->print("🌹🌹🌹到达约会地点：大乔和铠都来到了👉"+appointmentPlace));
    Thread 大乔=newThread("大乔",()->{
    say("铠，你在哪里...");
    try{
    cyclicBarrier.await();
    say("铠，你终于来了...");
    }catch(Exception e){
    e.printStackTrace();
    }
    });

    Thread 铠=newThread("铠",()->{
    try{
    Thread.sleep(500);
    say("我打个野，马上就到!");
    cyclicBarrier.await();
    say("乔，不好意思，刚打野遇上兰陵王了，你还好吗？！");
    }catch(Exception e){
    e.printStackTrace();
    }
    });

    大乔.start();
    铠.start();
    }

```

输出结果如下：

```shell
大乔:铠，你在哪里...
铠:我打个野，马上就到!
🌹🌹🌹到达约会地点：大乔和铠都来到了👉峡谷森林
铠:乔，不好意思，刚打野遇上兰陵王了，你还好吗？！
大乔:铠，你终于来了...

Process finished with exit code 0
```

对于代码的细节不暂且不必深究，本文后面对CyclicBarrier的内部细节会有详解，先感受它的基本用法。

从结果中可以看到，**CyclicBarrier可以像CountDownLatch一样，协调多线程的执行结束动作，在它们都结束后执行特定动作**
。从这点上来说，这是CyclicBarrier与CountDownLatch相似之处。然而，接下来的这个场景，所体现的则是它们一个明显的不同之处。

**2\. 小河边的幽会**

在上面的场景中，铠已经提到他在打野时遇到了兰陵王。而在铠与大乔的约会中，兰陵王竟然又撞见了他们，真是冤家路窄。于是，在兰陵王的搅局下，铠和大乔不得不转移阵地，**
他们同样约定到新的约定地点后等待对方**。（**铠一直以为兰陵王也喜欢大乔，要和他横刀夺爱，其实兰陵王在乎的只是铠打了它的野，他的心里只有野怪，对任何女人毫无兴趣**）。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5275b684a4084756b80590c97eabf29e~tplv-k3u1fbpfcp-zoom-1.image)

此时，如果继续用代码模拟这一场景的话，那么CountDownLatch就无能为力了，**因为CountDownLatch的使用是一次性的**，无法重复利用。而此时，**
你就会发现CyclicBarrier的神奇之处，它竟然可以重复利用**。似乎，你可能已经大概明白它为什么叫**Cyclic**的原因了。

接下来，我们再走一段代码，模拟大乔和铠的第二次幽会。在代码中，我们仍然定义幽会地点、大乔和铠两个主人公。**但是与此前不同的是，我们还增加了兰陵王这个搅局者，以及中途变更了幽会地点**。

```java
private static String appointmentPlace="峡谷森林";

public static void main(String[]args){
    CyclicBarrier cyclicBarrier=new CyclicBarrier(2,()->System.out.println("🌹🌹🌹到达约会地点：大乔和铠都来到了👉"+appointmentPlace));
    Thread 大乔=newThread("大乔",()->{
    say("铠，你在哪里...");
    try{
    cyclicBarrier.await();
    say("铠，你终于来了...");
    Thread.sleep(2600);
    say("好的，你要小心！");
    cyclicBarrier.await();
    Thread.sleep(100);
    say("我愿意！");
    }catch(Exception e){
    e.printStackTrace();
    }
    });

    Thread 铠=newThread("铠",()->{
    try{
    Thread.sleep(500);
    say("我打个野，马上就到!");
    cyclicBarrier.await();
    say("乔，不好意思，刚打野遇上兰陵王了，你还好吗？！");
    Thread.sleep(1500);

    note("幽会中...\n");

    Thread.sleep(1000);
    say("这个该死的兰陵王！乔，你先走，小河边见！");
    appointmentPlace="小河边";

    Thread.sleep(1500);
    note("︎\uD83D\uDDE1\uD83D\uDD2A铠和兰陵王决战开始，最终铠杀死了兰陵王，并前往小河边...\n");
    cyclicBarrier.await();

    say("乔，我已经解决了兰陵王，你看今晚夜色多美，我陪你看星星到天明...");
    }catch(Exception ignored){}
    });

    Thread 兰陵王=newThread("兰陵王",()->{
    try{
    Thread.sleep(2500);
    note("兰陵王出场...");
    say("铠打了我的野，不杀他誓不罢休！");

    say("铠，原来你和大乔在这里！\uD83D\uDDE1️\uD83D\uDDE1️");
    }catch(Exception ignored){}
    });

    兰陵王.start();
    大乔.start();
    铠.start();
    }

```

输出结果如下所示。**铠峡谷森林的好事被兰陵王搅局后，铠怒火中烧，让大乔先走，并约定在小河边碰面。随后，铠斩杀了兰陵王（可怜的钢铁直男），并前往小河边，完成他和大乔的第二次幽会**。

```java
大乔:铠，你在哪里...
    铠:我打个野，马上就到!
    🌹🌹🌹到达约会地点：大乔和铠都来到了👉峡谷森林
    铠:乔，不好意思，刚打野遇上兰陵王了，你还好吗？！
    大乔:铠，你终于来了...
    幽会中...

    兰陵王出场...
    兰陵王:铠打了我的野，不杀他誓不罢休！
    兰陵王:铠，原来你和大乔在这里！🗡️🗡️
    铠:这个该死的兰陵王！乔，你先走，小河边见！
    大乔:好的，你要小心！
    ︎🗡🔪铠和兰陵王决战开始，最终铠杀死了兰陵王，并前往小河边...

    🌹🌹🌹到达约会地点：大乔和铠都来到了👉小河边
    铠:乔，我已经解决了兰陵王，你看今晚夜色多美，我陪你看星星到天明...
    大乔:真好！

    Process finished with exit code 0
```

同样的，你暂时不要理会代码的细节，**但是你要注意到其中铠和大乔对`await()`的两次调用**。在你没有理解它的原理之前，可能会惊讶于它的神奇，这是正常现象。

二、CyclicBarrier是如何实现的
---------------------

CyclicBarrier是Java中提供的一个线程同步工具，与CountDownLatch相似，但又并不完全相同，**
最核心的区别在于CyclicBarrier是可以循环使用的，这一点在它的名字中也已经有所体现**。

接下来，我们来分析下它具体的源码实现。

**1\. 核心数据结构**

* `private final ReentrantLock lock = new ReentrantLock()`：进入屏障的锁，只有一把；
* `private final Condition trip = lock.newCondition()`：和上面的lock配套使用；
* `private final int parties`：参与方的数量，本文上述的例子只有铠和大乔，所以数量是2；
* `private final Runnable barrierCommand`：在本轮结束时运行的特定代码。本文上述例子用到了它，可以上翻查看；
* `private Generation generation = new Generation()`
  ：当前屏障的代次。比如本文上述的两个场景中，generation是不同的，在铠和大乔将幽会地点改成小河边后，会生成新的generation；
* `private int count`
  ：正在等待的参与方数量。在每个代次中，count会从最初的参与数量（即parties）降至0，到0时本代次结束，而在新的代次或本代次被拆除（broken）时，count的值会恢复为parties的值。

**2\. 核心构造**

* `public CyclicBarrier(int parties)`：指定参与方的数量；
* `public CyclicBarrier(int parties, Runnable barrierAction)`：指定参与方的数量，并指定在本代次结束时运行的代码。

**3\. 核心方法**

* `public int await()`：如果当前线程不是第一个到达屏障的话，它将会进入等待，直到其他线程都到达，除非发生**被中断**、**屏障被拆除**、**屏障被重设**等情况；
* `public int await(long timeout, TimeUnit unit)`：和await()类似，但是加上了时间限制；
* `public boolean isBroken()`：当前屏障是否被拆除；
* `public void reset()`：重设当前屏障。会先拆除屏障再设置新的屏障；
* `public int getNumberWaiting()`：正在等待的线程数量。

在CyclicBarrier的各方法中，最为核心的就是`dowait()`，两个`await()`的内部都是调用这个方法。所以，理解了`dowait()`
，基本上就理解了CyclicBarrier的实现关键。

`dowait()`方法略长，稍微需要点耐心，我已经对其中部分做了注释。当然，如果你想看源码的话，还是建议直接从JDK中看它的全部，这里的源码只是为了辅助你理解上下文。

```java
private int dowait(boolean timed,long nanos)
    throws InterruptedException,BrokenBarrierException,TimeoutException{
final ReentrantLock lock=this.lock;
    lock.lock(); // 注意，这里是一定要加锁的
    try{
final Generation g=generation;

    if(g.broken) // 如果当前屏障被拆除，则抛出异常
    throw new BrokenBarrierException();

    if(Thread.interrupted()){
    breakBarrier(); // 如果当前线程被中断，则拆除屏障并抛出异常
    throw new InterruptedException();
    }

    int index=--count; // 当线程调用await后，count减1
    if(index==0){ // tripped // 如果count为0，接下来将尝试结束屏障，并开启新的屏障
    boolean ranAction=false;
    try{
final Runnable command=barrierCommand;
    if(command!=null)
    command.run();
    ranAction=true;
    nextGeneration();
    return 0;
    }finally{
    if(!ranAction)
    breakBarrier();
    }
    }

    // loop until tripped, broken, interrupted, or timed out
    for(;;){
    try{
    if(!timed)
    trip.await();
    else if(nanos>0 L)
    nanos=trip.awaitNanos(nanos);
    }catch(InterruptedException ie){
    if(g==generation&&!g.broken){
    breakBarrier();
    throw ie;
    }else{
    // We're about to finish waiting even if we had not
    // been interrupted, so this interrupt is deemed to
    // "belong" to subsequent execution.
    Thread.currentThread().interrupt();
    }
    }

    if(g.broken)
    throw new BrokenBarrierException();

    if(g!=generation)
    return index;

    if(timed&&nanos<=0 L){
    breakBarrier();
    throw new TimeoutException();
    }
    }
    }finally{
    lock.unlock();
    }
    }
```

对于CyclicBarrier的核心数据结构、构造和方法，都在上面，它们很重要。但是，更为重要的是，要理解CyclicBarrier的思想，**也就是下面这幅值得你收藏的图**。**
理解了这幅图，也就理解了CyclicBarrier**.

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ecc05e33575744dc8c0c873377f9c14f~tplv-k3u1fbpfcp-zoom-1.image)

此时，从这幅图再回头看第一节的两个场景，铠和大乔先后在**峡谷森林**、**小河边**两个地点幽会。那么，如果也用一幅图来表示的话，它应该是下面这样：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/441718e7963a43dabcb71803ac95d93a~tplv-k3u1fbpfcp-zoom-1.image)

三、CyclicBarrier与CountDownLatch有何不同
----------------------------------

前面两节已经提到了两者的核心不同：

* **CountDownLatch是一次性的，而CyclicBarrier则可以多次设置屏障，实现重复利用**；
* **CountDownLatch中的各个子线程不可以等待其他线程，只能完成自己的任务；而CyclicBarrier中的各个线程可以等待其他线程**。

除此之外，它们俩还有着一些其他的不同，整体汇总后如下面的表格所示：

| CyclicBarrier | CountDownLatch |
| --- | --- |
| CyclicBarrier是可重用的，其中的线程会等待所有的线程完成任务。届时，屏障将被拆除，并可以选择性地做一些特定的动作。 | CountDownLatch是一次性的，不同的线程在同一个计数器上工作，直到计数器为0. |
| CyclicBarrier面向的是线程数 | CountDownLatch面向的是任务数 |
| 在使用CyclicBarrier时，你必须在构造中指定参与协作的线程数，这些线程必须调用await()方法 | 使用CountDownLatch时，则必须要指定任务数，至于这些任务由哪些线程完成无关紧要 |
| CyclicBarrier可以在所有的线程释放后重新使用 | CountDownLatch在计数器为0时不能再使用 |
| 在CyclicBarrier中，如果某个线程遇到了中断、超时等问题时，则处于await的线程都会出现问题 | 在CountDownLatch中，如果某个线程出现问题，其他线程不受影响 |

小结
--

以上就是关于CyclicBarrier的全部内容。在学习CyclicBarrier时，要侧重理解它所要解决的问题场景，以及它与CountDownLatch的不同，然后再去看源码，这也是为什么我们没有上来就放源码而是绕弯讲了个故事的原因，虽然那个故事挺“狗血”。当然，如果这个狗血的故事能让你记住这个知识点，狗血也值得了。

正文到此结束，恭喜你又上了一颗星✨

夫子的试炼
-----

* 编写代码体验CyclicBarrier用法。

延伸阅读与参考资料
---------

* [《王者并发课》大纲与更新进度总览](https://juejin.cn/post/6967277362455150628)

关于作者
----

关注【**技术八点半**】，及时获取文章更新。传递有品质的技术文章，记录平凡人的成长故事，偶尔也聊聊生活和理想。早晨8：30推送作者品质原创，晚上20：30推送行业深度好文。

如果本文对你有帮助，欢迎**点赞**、**关注**、**监督**，我们一起**从青铜到王者**。