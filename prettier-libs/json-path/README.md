# [Jayway JsonPath](https://github.com/json-path/JsonPath)

Configuration
-------------

```java
// 为 JsonPath 默认配置 jackson 序列化
Configuration.setDefaults(
    new Defaults(){

private final JsonProvider jsonProvider=new JacksonJsonProvider();
private final MappingProvider mappingProvider=new JacksonMappingProvider();

@Override
public JsonProvider jsonProvider(){
    return jsonProvider;
    }

@Override
public Set<Option> options(){
    return EnumSet.noneOf(Option.class);
    }

@Override
public MappingProvider mappingProvider(){
    return mappingProvider;
    }
    });

    Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
```

## Use

### Maven

```xml

<dependency>
  <groupId>com.jayway.jsonpath</groupId>
  <artifactId>json-path</artifactId>
  <version>2.7.0</version>
</dependency>
```

Operators
---------

| Operator                  | Description                                                        |
| :------------------------ | :----------------------------------------------------------------- |
| `$`                       | The root element to query. This starts all path expressions.       |
| `@`                       | The current node being processed by a filter predicate.            |
| `*`                       | Wildcard. Available anywhere a name or numeric are required.       |
| `..`                      | Deep scan. Available anywhere a name is required.                  |
| `.<name>`                 | Dot-notated child                                                  |
| `['<name>' (, '<name>')]` | Bracket-notated child or children                                  |
| `[<number> (, <number>)]` | Array index or indexes                                             |
| `[start:end]`             | Array slice operator                                               |
| `[?(<expression>)]`       | Filter expression. Expression must evaluate to a boolean value.    |

Functions
---------

Functions can be invoked at the tail end of a path - the input to a function is the output of the
path expression.
The function output is dictated by the function itself.

| Function                  | Description                                                         | Output type |
| :------------------------ | :------------------------------------------------------------------ |:----------- |
| min()                     | Provides the min value of an array of numbers                       | Double      |
| max()                     | Provides the max value of an array of numbers                       | Double      |
| avg()                     | Provides the average value of an array of numbers                   | Double      | 
| stddev()                  | Provides the standard deviation value of an array of numbers        | Double      | 
| length()                  | Provides the length of an array                                     | Integer     |
| sum()                     | Provides the sum value of an array of numbers                       | Double      |
| keys()                    | Provides the property keys (An alternative for terminal tilde `~`)  | `Set<E>`    |
| concat(X)                 | Provides a concatinated version of the path output with a new item  | like input  |
| append(X)                 | add an item to the json path output array                           | like input  |

Filter Operators
-----------------

Filters are logical expressions used to filter arrays. A typical filter would be `[?(@.age > 18)]`
where `@` represents the current item being processed. More complex filters can be created with
logical operators `&&` and `||`. String literals must be enclosed by single or double
quotes (`[?(@.color == 'blue')]` or `[?(@.color == "blue")]`).

| Operator                 | Description                                                           |
| :----------------------- | :-------------------------------------------------------------------- |
| ==                       | left is equal to right (note that 1 is not equal to '1')              |
| !=                       | left is not equal to right                                            |
| <                        | left is less than right                                               |
| <=                       | left is less or equal to right                                        |
| >                        | left is greater than right                                            |
| > =                       | left is greater than or equal to right                                |
| =~                       | left matches regular expression  [?(@.name =~ /foo.*?/i)]             |
| in                       | left exists in right [?(@.size in ['S', 'M'])]                        |
| nin                      | left does not exists in right                                         |
| subsetof                 | left is a subset of right [?(@.sizes subsetof ['S', 'M', 'L'])]       |
| anyof                    | left has an intersection with right [?(@.sizes anyof ['M', 'L'])]     |
| noneof                   | left has no intersection with right [?(@.sizes noneof ['M', 'L'])]    |
| size                     | size of left (array or string) should match right                     |
| empty                    | left (array or string) should be empty                                |