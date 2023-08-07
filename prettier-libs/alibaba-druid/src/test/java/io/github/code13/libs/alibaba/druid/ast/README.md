## 一、 AST

AST是abstract syntax tree的缩写，也就是抽象语法树。和所有的Parser一样，Druid Parser会生成一个抽象语法树。

在Druid中，AST节点类型主要包括SQLObject、SQLExpr、SQLStatement三种抽象类型。

```java
interface SQLObject {

}

interface SQLExpr extends SQLObject {

}

interface SQLStatement extends SQLObject {

}

interface SQLTableSource extends SQLObject {

}

class SQLSelect extends SQLObject {

}

class SQLSelectQueryBlock extends SQLObject {

}
```

## 二、语法树解析

### 2.1 核心类介绍

#### 2.1.1 SQLStatemment DQL & DML顶级抽象

- DQL 数据查询语言 select
- DML 数据操纵语言 insert update delete

最常用的Statement当然是SELECT/UPDATE/DELETE/INSERT，他们分别是

| 核心类 | 说明 |
| --- | --- |
| SQLSelectStatement | 查询语句 |
| SQLUpdateStatement | 更新语句 |
| SQLDeleteStatement | 删除语句 |
| SQLInsertStatement | 新增语句 |

#### 2.1.2 SQLSelect SQL查询

SQLSelectStatement包含一个SQLSelect，SQLSelect包含一个SQLSelectQuery。SQLSelectQuery有主要的两个派生类，
分别是SQLSelectQueryBlock(单表sql查询)和SQLUnionQuery(union查询)。

#### 2.1.3 SQLExpr

| 核心类 | 举例 | 说明 | 适用范围 | 快速记忆 |
| --- | --- | --- | --- | --- |
| SQLIdentifierExpr | id,name,age | SQLIdentifierExpr | 查询字段或者where条件 | 唯一标记 |
| SQLPropertyExpr | u.id,u.name | 区别于SQLIdentifierExpr,适用于有别名的场景; SQLPropertyExpr.name = id, SQLPropertyExpr.owner = SQLIdentifierExpr = u） | 查询字段或者where条件 | 有别名就是它 |
| SQLBinaryOpExpr | id = 1, id > 5 | SQLBinaryOpExpr(left = SQLIdentifierExpr = id ,right = SQLValuableExpr = 1) | where条件 | 有操作符就是它 |
| SQLVariantRefExpr | id = ? | 变量 | where条件 | 有变量符就是它 |
| SQLIntegerExpr | id = 1 | 数字类型 | 值类型 | \- |
| SQLCharExpr | name = '孙悟空' | 字符类型 | 值类型 | \- |

#### 2.1.4 SQLTableSource

常见的 `SQLTableSource` 包括 `SQLExprTableSource`、`SQLJoinTableSource`、`SQLSubqueryTableSource`
、`SQLWithSubqueryClause.Entry`

| 核心类 | 举例 | 说明 | 快速记忆 |
| --- | --- | --- | --- |
| SQLExprTableSource | select \* from emp where i = 3 | name = SQLIdentifierExpr = emp | 单表查询 |
| SQLJoinTableSource | select \* from emp e inner join org o on e.org\_id = o.id | left = SQLExprTableSource(emp e),right = SQLExprTableSource(org o), condition = SQLBinaryOpExpr(e.org\_id = o.id) | join 查询使用 |
| SQLSubqueryTableSource | select _from (
select_ from temp) a | from(...)是一个SQLSubqueryTableSource | 子查询语句 |
| SQLWithSubqueryClause | WITH RECURSIVE ancestors AS (SELECT _FROM org UNION SELECT
f._ FROM org f, ancestors a WHERE f.id = a.parent\_id ) SELECT \* FROM ancestors; | ancestors AS (...) 是一个SQLWithSubqueryClause.Entry | with |
