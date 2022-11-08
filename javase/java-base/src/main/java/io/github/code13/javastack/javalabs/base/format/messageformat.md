# MessageFormat

### 参数模式

MessageFormat采用`{}`来标记需要被**替换/插入**的部分，其中`{}`里面的参数结构具有一定模式：

```java
ArgumentIndex[,FormatType[,FormatStyle]] 
```

- `ArgumentIndex`：**非必须**。从`0`开始的索引值
- `FormatType`：**非必须**。使用不同的`java.text.Format`实现类对入参进行格式化处理。它能有如下值：
    - number：调用NumberFormat进行格式化
    - date：调用DateFormat进行格式化
    - time：调用DateFormat进行格式化
    - **choice**：调用ChoiceFormat进行格式化
- `FormatStyle`：**非必须**。设置FormatType使用的样式。它能有如下值：
    - short、medium、long、full、integer、currency、percent、**SubformPattern（如日期格式、数字格式#.##等）**

> 说明：FormatType和FormatStyle只有在传入值为日期时间、数字、百分比等类型时才有可能需要设置，使用得并不多。毕竟：我在外部格式化好后再放进去不香吗？

### 和String.format选谁？

二者都能用于字符串拼接（格式化）上，撇开MessageFormat支持各种模式不说，我们只需要考虑它俩的性能上差异。

- **MeesageFormat**：先分析（模版可提前分析，且可以只分析一次），再在指定位置上插入相应的值
    - 分析：遍历字符串，维护一个`{}`数组并记录位置
    - 填值
- **String.format**：该静态方法是采用运行时用**正则表达式** 匹配到占位符，然后执行替换的
    - 正则表达式为`"%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])"`
    - 根据正则匹配到占位符列表和位置，然后填值

一说到正则表达式，我心里就发触，因为它对性能是**不友好**的，所以孰优孰劣，高下立判。

> 说明：还是那句话，没有绝对的谁好谁坏，如果你的系统对性能不敏感，那就是方便第一