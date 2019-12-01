# fastable
 在内存中快速查询、缓存二维表数据

---
## 1 为何造此
数据库里的数据用sql查就好了，那程序运行时（jvm）内存中的数据呢？
假如内存里有像这样一批二维数据：
```
datas = [
    {"name": "坂田银时", "birth": "1980-03-08", "unit": "万事屋阿银", "gender": "M"},
    {"name": "神乐", "birth": "1996-06-01", "unit": "万事屋阿银", "gender": "F"},
    {"name": "志村新八", "birth": "1993-10-11", "unit": "万事屋阿银", "gender": "M"},
    {"name": "定春", "birth": "1900-09-01", "unit": "万事屋阿银", "gender": "?"},
    {"name": "登势", "birth": "1976-04-04", "unit": "登势酒吧", "gender": "F"},
    {"name": "凯瑟琳", "birth": "1989-04-05", "unit": "登势酒吧", "gender": "F"},
    {"name": "志村妙", "birth": "1986-04-07", "unit": "微笑酒店", "gender": "F"}
]
```
而我们想有一个这样的工具库：
1. 能进行高效的查找操作
2. 能优化存储冗余数据
3. 轻量，依赖少，API简洁直观，领导也能看懂

比如找到万事屋里的女孩子们：
> ###此处是一句打了码的神秘代码###

结果：
`[{"name": "神乐", "birth": "1996-06-01", "unit": "万事屋阿银", "gender": "F"}]`
*（啊咧，只有一个...o((>ω< ))o）*

本项目，就是满足上述需求、围绕那句打了码的神秘代码，所作的工作
## 2 快速开始
保证本项目的源码或jar包在编译路径里（暂无maven库）
### 2.1 数据源
受Java语言限制，我们要操作的原始数据必须实现于`List<?>`,其中的元素`?`必须是以下几种类型：
- `JavaBean`对象，即有对于属性的get、set方法
- 实现于`<Map<String, Object>>`接口的对象
- ~~基于特定属性注解的任意Java对象~~ TODO
### 2.2 初始化
假定我们要操作的数据源是`datas`，每一行数据由`People`类构造（`People`是标准JavaBean）
已知每个`People`中`name`属性值是唯一的，即我们可以以此作为每一行数据的唯一标识（id）
那就：
```java
// List<People> datas;
Fastable<People> fastable = new Fastable<People>(datas, 'name'); // args: 数据源, 唯一属性
```
若没有任何属性的值能作为唯一标识，那就不传第二个参数，程序会自动生成隐式rowid：
```java
// List<People> datas;
Fastable<People> fastable = new Fastable<People>(datas); // args: 数据源
```
### 2.3 数据查询
套用最开始的例子，找到【万事屋】里的【女孩子】们，神秘代码揭晓：
```java
List<People> res = fastable.query("unit", "万事屋阿银").and("gender", "F").fetch();
// res is [{"name": "神乐", "birth": "1996-06-01", "unit": "万事屋阿银", "gender": "F"}]
```
再来，找到【万事屋】或【登势酒吧】里所有【男孩子】，但排除【志村新八】：
```java
List<People> res = fastable.query("unit", "万事屋阿银")
                            .or("unit", "登势酒店")
                            .and("gender", "M")
                            .not("name", "志村新八")
                            .fetch();
// res is [{"name": "坂田银时", "birth": "1980-03-08", "unit": "万事屋阿银", "gender": "M"}]
```
相信聪明的你已经看出来了，本项目的接口方法采用“流畅接口”；对数据的查找遵循从左至右的形式逻辑运算（“与”、“或”、“非” => `.and`、`.or`、`.not`）

`.fetch`用于返回结果，如果查询条件只有一个，可以简写成`.fetchQuery`：
```java
// List<People> res = fastable.query("unit", "万事屋阿银").fetch();
// 或者写成
List<People> res = fastable.fetchQuery("unit", "万事屋阿银");
/* res is [
        {"name": "坂田银时", "birth": "1980-03-08", "unit": "万事屋阿银", "gender": "M"},
        {"name": "神乐", "birth": "1996-06-01", "unit": "万事屋阿银", "gender": "F"},
        {"name": "志村新八", "birth": "1993-10-11", "unit": "万事屋阿银", "gender": "M"}
    ] 
*/
```
## 3 注意 
 - 查询功能参数是强类型的，比如`query("gender", "M")`与`query("gender", 'M')`，前者查询的是String类型，后者查询的是char类型，不同的类型会有不同的返回结果
 - 该项目目前处于起步阶段，没有经过大量的测试，需完善的地方还有很多，不建议用于生产环境
 ( •̀ ω •́ )✧