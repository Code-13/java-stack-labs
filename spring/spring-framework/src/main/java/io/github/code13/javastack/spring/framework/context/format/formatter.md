[原文](https://fangshixiang.blog.csdn.net/article/details/111824522)

![https://img-blog.csdnimg.cn/20210106104344522.png#pic_center](https://img-blog.csdnimg.cn/20210106104344522.png#pic_center)

## FormatterRegistry：格式化器注册中心

**field属性**格式化器的注册表（注册中心）。请注意：这里强调了field的存在，先混个眼熟，后面你将能有较深体会。

```java
public interface FormatterRegistry extends ConverterRegistry {

  void addPrinter(Printer<?> printer);

  void addParser(Parser<?> parser);

  void addFormatter(Formatter<?> formatter);

  void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);

  void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser);

  void addFormatterForFieldAnnotation(
      AnnotationFormatterFactory<? extends Annotation> annotationFormatterFactory);
}
```

此接口继承自类型转换器注册中心`ConverterRegistry`，所以格式化注册中心是转换器注册中心的**加强版**
，是其超集，功能更多更强大。

虽然`FormatterRegistry`提供的添加方法挺多，但其实基本都是在描述同一个事：为指定类型`fieldType`
添加格式化器（printer或parser），绘制成图如下所示：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201227221334697.png#pic_center)

> 说明：最后一个接口方法除外，`addFormatterForFieldAnnotation()`和格式化注解相关，因为它非常重要，因此放在下文专门撰文讲解

FormatterRegistry接口的继承树如下：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201227221601744.png#pic_center)  
有了学过ConverterRegistry的经验，这种设计套路**很容易**被看穿。这两个实现类按层级进行分工：

- `FormattingConversionService`：实现所有接口方法
- `DefaultFormattingConversionService`：继承自上面的FormattingConversionService，在其基础上注册**
  默认的**格式化器

事实上，功能分类确实如此。本文重点介绍`FormattingConversionService`，这个类的设计实现上有很多讨巧之处，只要你来，要你好看。

## FormattingConversionService

它是`FormatterRegistry`接口的实现类，实现其**所有**接口方法。

`FormatterRegistry`是`ConverterRegistry`
的子接口，而ConverterRegistry接口的所有方法均已由`GenericConversionService`
全部实现了，所以可以通过继承它来**间接完成**
ConverterRegistry接口方法的实现，因此本类的继承结构是这样子的（请细品这个结构）：  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210103155139539.png#pic_center)

FormattingConversionService通过继承GenericConversionService搞定“左半边”（父接口`ConverterRegistry`
）；只剩“右半边”待处理，也就是FormatterRegistry新增的接口方法。

```java
FormattingConversionService：

@Override
public void addPrinter(Printer<?> printer){
    Class<?> fieldType=getFieldType(printer,Printer.class);
    addConverter(new PrinterConverter(fieldType,printer,this));
    }
@Override
public void addParser(Parser<?> parser){
    Class<?> fieldType=getFieldType(parser,Parser.class);
    addConverter(new ParserConverter(fieldType,parser,this));
    }
@Override
public void addFormatter(Formatter<?> formatter){
    addFormatterForFieldType(getFieldType(formatter),formatter);
    }
@Override
public void addFormatterForFieldType(Class<?> fieldType,Formatter<?> formatter){
    addConverter(new PrinterConverter(fieldType,formatter,this));
    addConverter(new ParserConverter(fieldType,formatter,this));
    }
@Override
public void addFormatterForFieldType(Class<?> fieldType,Printer<?> printer,Parser<?> parser){
    addConverter(new PrinterConverter(fieldType,printer,this));
    addConverter(new ParserConverter(fieldType,parser,this));
    }
```

从接口的实现可以看到这个“惊天大秘密”：所有的格式化器（含Printer、Parser、Formatter）都是被当作`Converter`
注册的，也就是说真正的注册中心只有一个，那就是`ConverterRegistry`。  
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201227223654692.png#pic_center)

格式化器的注册管理**远没有**转换器那么复杂，因为它是基于**上层适配**
的思想，最终适配为Converter来完成注册的。所以最终注册进去的实际是个经由格式化器适配来的转换器，完美**
复用了**那套复杂的转换器管理逻辑。

### PrinterConverter：Printer接口适配器

把`Printer<?>`适配为转换器，转换目标为`fieldType -> String`。

```java
private static class PrinterConverter implements GenericConverter {

  private final Class<?> fieldType;
  // 从Printer<?>泛型里解析出来的类型，有可能和fieldType一样，有可能不一样
  private final TypeDescriptor printerObjectType;
  // 实际执行“转换”动作的组件
  private final Printer printer;
  private final ConversionService conversionService;

  public PrinterConverter(Class<?> fieldType, Printer<?> printer,
      ConversionService conversionService) {
		...
    // 从类上解析出泛型类型，但不一定是实际类型
    this.printerObjectType = TypeDescriptor.valueOf(resolvePrinterObjectType(printer));
		...
  }

  // fieldType -> String
  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Collections.singleton(new ConvertiblePair(this.fieldType, String.class));
  }

}
```

既然是转换器，重点当然是它的convert转换方法：

```java
PrinterConverter：

@Override
@SuppressWarnings("unchecked")
public Object convert(@Nullable Object source,TypeDescriptor sourceType,TypeDescriptor targetType){
    // 若sourceType不是printerObjectType的子类型
    // 就尝试用conversionService转一下类型试试
    // （也就是说：若是子类型是可直接处理的，无需转换一趟）
    if(!sourceType.isAssignableTo(this.printerObjectType)){
    source=this.conversionService.convert(source,sourceType,this.printerObjectType);
    }
    if(source==null){
    return"";
    }

    // 执行实际转换逻辑
    return this.printer.print(source,LocaleContextHolder.getLocale());
    }
```

转换步骤分为两步：

1. 若**源**类型（实际类型）不是该Printer类型的泛型类型的子类型的话，那就尝试使用conversionService转一趟
    1. 例如：Printer处理的是Number类型，但是你传入的是Person类型，这个时候conversionService就会发挥作用了
2. 交由目标格式化器Printer执行**实际的**转换逻辑

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210103221758587.png#pic_center)

可以说Printer它可以直接转，也可以是构建在conversionService **之上** 的一个转换器：只要源类型是我**能**
处理的，或者经过conversionService后能成为我**能**处理的类型，都能进行转换。有一次完美的**能力复用**。

### ParserConverter：Parser接口适配器

把`Parser<?>`适配为转换器，转换目标为`String -> fieldType`。

```java
private static class ParserConverter implements GenericConverter {

  private final Class<?> fieldType;
  private final Parser<?> parser;
  private final ConversionService conversionService;

	... // 省略构造器

  // String -> fieldType
  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Collections.singleton(new ConvertiblePair(String.class, this.fieldType));
  }

}
```

既然是转换器，重点当然是它的convert转换方法：

```java
ParserConverter：

@Override
@Nullable
public Object convert(@Nullable Object source,TypeDescriptor sourceType,TypeDescriptor targetType){
    // 空串当null处理
    String text=(String)source;
    if(!StringUtils.hasText(text)){
    return null;
    }

    ...
    Object result=this.parser.parse(text,LocaleContextHolder.getLocale());
    ...

    // 解读/转换结果
    TypeDescriptor resultType=TypeDescriptor.valueOf(result.getClass());
    if(!resultType.isAssignableTo(targetType)){
    result=this.conversionService.convert(result,resultType,targetType);
    }
    return result;
    }
```

转换步骤分为两步：

1. 通过Parser将String转换为指定的类型结果result（若失败，则抛出异常）
2. 判断若result属于**目标类型**的子类型，直接返回，否则调用ConversionService转换一把

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210103222351831.png#pic_center)