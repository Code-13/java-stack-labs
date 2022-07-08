# java.time

Java SE 8 发行版中引入的 Date-Time 包 java.time 提供了一个日期和时间的综合模型，并且是在 JSR 310：Date and Time API

下开发的。虽然 java.time 基于国际标准化组织（ISO）日历系统，但常用的全球日历也受支持。

该轨迹涵盖了使用基于 ISO 的类来表示日期和时间以及操纵日期和时间值的基本原理。

时间似乎是一个简单的主题; 即使是便宜的手表也能提供相当准确的日期和时间。但是，仔细检查后， 您会发现影响您对时间理解的微妙复杂性和诸多因素。例如，闰年比 1 月 31
日增加一个月的结果与其他年份的结果不同。 时区也增加了复杂性。例如，一个国家可能会在短时间内进入或退出夏令时，或每年不止一次，或者可能会在特定年份内完全跳过夏令时。

日期 - 时间 API 使用 ISO-8601

中定义的日历系统作为默认日历。 此日历基于公历系统，并在全球范围内用作表示日期和时间的事实标准。Date-Time API 中的核心类具有诸如 LocalDateTime，ZonedDateTime
和 OffsetDateTime 等名称。所有这些都使用 ISO 日历系统。 如果您想使用替代日历系统（例如 Hijrah 或泰国佛教徒），java.time.chrono
软件包允许您使用其中一个预定义日历系统。 或者你可以创建自己的。

Date-Time API 使用 Unicode 通用语言环境数据存储库（CLDR） 。 此存储库支持世界各种语言，并包含世界上最大的可用区域设置数据集合。这个存储库中的信息已被本地化为数百种语言。
日期时间 API 还使用 时区数据库（TZDB） 。该数据库自 1970 年以来提供有关全球每个时区变化的信息， 并介绍了自引入该概念以来的主要时区的历史记录。

## 日期时间设计原则

Date-Time API 是使用多种设计原则开发的。

### 明确 / Clear

API 中的方法定义明确，行为清晰明了。例如，使用 null 参数值调用 Date-Time 方法通常会触发 NullPointerException。

### 流式 / Fluent

Date-Time API 提供流式接口，使代码易于阅读。因为大多数方法不允许带有空值的参数并且不返回空值， 所以可以将方法调用链接在一起，并且可以快速理解结果代码。例如：

```java
LocalDate today=LocalDate.now();

  LocalDate payday=today.with(TemporalAdjusters.lastDayOfMonth()).minusDays(2);
```

### 不可变 / Immutable

Date-Time API 中的大多数类都创建了不可变的对象，这意味着在创建对象之后，它不能被修改。 要改变 不可变对象 的值，必须将新对象构造为原始对象的修改副本。
这也意味着根据定义，Date-Time API 是线程安全的。这会影响大部分用于创建日期或时间对象的方法的 API 的前缀 of，from 或者 with， 而不是构造函数，并且没有 set
方法。例如：

```java
LocalDate dateOfBirth=LocalDate.of(2012,Month.MAY,14);
  LocalDate firstBirthday=dateOfBirth.plusYears(1);
```

### 扩展 / Extensible

只要有可能，Date-Time API 都是可扩展的。例如，您可以定义自己的时间调节器和查询，或者构建您自己的日历系统。

## 时间包

Date-Time API 由主包 java.time 和四个子包组成：

- java.time

  表示日期和时间的 API 的核心。它包括日期，时间，日期和时间相结合的类别， 时区/zones，瞬间/instants，持续时间/duration 和 时钟/clocks。这些类基于
  ISO-8601 中定义的日历系统， 并且不可变且线程安全。

- java.time.chrono

  用于表示除默认 ISO-8601 以外的日历系统的 API。您也可以定义自己的日历系统。本教程不包含任何细节。

- java.time.format

  用于格式化和分析日期和时间的类。

- java.time.temporal

  扩展 API 主要用于框架和库编写器，允许日期和时间类之间的互操作，查询和调整。字段（TemporalField 和 ChronoField） 和单位（TemporalUnit 和
  ChronoUnit）在此包中定义。

- java.time.zone

  支持时区的类，时区的偏移和时区规则。如果使用时区，大多数开发人员只需使用 ZonedDateTime 和 ZoneId 或 ZoneOffset

## 方法命名约定

Date-Time API 在丰富的类中提供了丰富的方法。尽可能使方法名称在类之间保持一致。 例如，许多类提供了一种 now 的方法，该方法捕获与该类相关的当前时刻的日期或时间值。
有从允许从一个类转换到另一个类的方法。

还有关于方法名称前缀的标准化。由于 Date-Time API 中的大多数类都是不可变的， 因此 API 不包含 set 方法。(在创建之后，不可变对象的值不能改变) 下表列出了常用的前缀：

| Prefix | Method Type    | Use                                                                     |
|--------|----------------|-------------------------------------------------------------------------|
| of     | static factory | 创建工厂主要验证输入参数的实例，而不是转换它们。                        |
| from   | static factory | 将输入参数转换为目标类的实例，这可能会导致输入信息丢失。                |
| parse  | static factory | 分析输入字符串以生成目标类的实例。                                      |
| format | instance       | 使用指定的格式化程序来格式化时间对象中的值以生成字符串。                |
| get    | instance       | 返回目标对象状态的一部分。                                              |
| is     | instance       | 查询目标对象的状态。                                                    |
| with   | instance       | 返回一个元素已更改的目标对象的副本; 类似set方法，不过是返回一个新的对象 |
| plus   | instance       | 加上时间量返回目标对象的副本。                                          |
| minus  | instance       | 减去时间量返回目标对象的副本。                                          |
| to     | instance       | 将此对象转换为另一种类型                                                |
| at     | instance       | 将此对象与另一个组合起来。                                              |