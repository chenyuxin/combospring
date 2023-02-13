# combo-framework
 常用框架

 
在maven项目中，引用指定工具模块

例如:

```xml
<dependency>
	<groupId>com.github.chenyuxin</groupId>
	<artifactId>combo-dao</artifactId>
	<version>3.0.0</version>
</dependency>

```

然后就可以使用了。


## 目录
* [combo-dao通用dao工具](#combo-dao)
    *  [配置dao](#combo-dao)
    *  数据库操作javaBean的注解方式
    *  combo-dao使用
* [combo-util通用工具模块](#combo-util)
* [combo-util-cipher通用加密工具模块](#combo-util-cipher)
* [combo-util-chinese通用chinese工具模块](#combo-util-chinese)
* [combo-util-json通用json工具模块](#combo-util-json)
* [combo-util-xml通用xml工具模块](#combo-util-xml)
* [combo-webservice通用webservice工具模块](#combo-webservice)
* [combo-webservice-cxf通用cxf工具模块](#combo-webservice-cxf)

## combo-dao

轻量操作数据库，无需对数据库过多绑架，同时保持与数据库交互的封装便利性。

引入后已经与spring框架集成，无需再配置spring框架。

通用Dao层,使用springjdbc，支持配置多数据源使用，支持多数据库类型。

支持获取list<map>数据，也支持javaBean类封装的直接的增删改查，或者自定义sql操作数据库，灵活方便。

### 配置dao
在您的当前maven项目中,引入ComboDao依赖

```xml
<dependency>
	<groupId>com.github.chenyuxin</groupId>
	<artifactId>combo-dao</artifactId>
	<version>3.0.0</version>
</dependency>

```

springboot使用ComboDao时，在maven中引入项目的依赖即可自动完成装配。

spring framework 项目，推荐使用注解的配置方式，参看：

```xml
/combo-dao/src/test/java/com/wondersgroup/test/configuration/MyWebApplicationInitializer.java
```

在 spring framework 项目中添加此类，并配置jdbc，参看：

```xml
/combo-dao-2.0/src/test/resources/properties/applicationJdbc.properties
```
springboot项目中同样使用此jdbc配置，即可开始享用。



注解配置使用多数据源时，参看：

```xml
/combo-dao/src/test/java/com/wondersgroup/test/configuration/MoreDataSourceConfiguration.java

/combo-dao/src/test/resources/properties
```

在您的配置中注入其它数据源的`@Bean`，并记录下配置的`beanName`，在具体使用的时候，给`ComboDao`传参，让`ComboDao`执行此数据源的数据库连接。

-------------------------------------------------------------------




对于当前maven项目中任然想使用的`web.xml`的配置方式时,在web.xml里配置spring配置读取文件

```xml
<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>classpath*:/applicationContext/applicationContext-*.xml</param-value>
</context-param>

```
建立web根目录下配置文件夹/applicationContext和/properties,

Maven工程可在/resources下建立

/yourMavenWebProject/src/main/resources/applicationContext

/yourMavenWebProject/src/main/resources/properties

将配置文件applicationContext-XXX.xml和XXX.properties文件配置于此处。

-------------------------------------------------------------------

此工具在spring项目中默认数据源配置,可以直接复制

```xml
/combo-dao/src/test/resources/applicationContext/applicationContext-ComboDao.xml
```

其他配置,查看配置样例:

配置jdbc的XXX.properties文件,数据库连接和配置同注解配置和springboot配置相同，参看:

```java
/combo-dao/src/test/resources/properties

```

多数据源properties文件配置，其它数据源自行配置文件参数，在自行配置的多数据源spring初始配置文件applicationContext-XXX.xml 与 XXX.properties 中。

-------------------------------------------------------------------

对默认数据源的aop管理的xml配置样例，可自行修改那些类的哪些方法参与事务。参看：

```xml
/combo-dao/src/test/resources/applicationContext/applicationContext-aop.xml
```
	
对于多数据源的配置，参看：

```xml
/combo-dao/src/test/resources/applicationContext/applicationContext-dataSource2.xml
```

这里为`web.xml`配置的参考，`spring5`还是推荐使用注解的方式配置项目。
参看`/combo-framework/test-project`下的样例工程的配置。


### 数据库操作，可以使用javaBean的注解方式

```java

@Table(name="test")//与数据库交互的表名,若要规定schema直接在表名前添加,如 @Table(name="user.test")
public class TestPo {
	
	@Id//不限制数据库，作为更新和删除的筛选条件使用，也可以认为是数据库主键。mysql需要设置数据库主键 saveOrUpdate(Obj); 方法才能正常使用
	private String testId;

	private String testName;//默认属性名驼峰转下划线为数据库列名
	
	@Column(name="test_col")//自定义数据库列名
	private Double testCol1;
	
	@Column(name="")//字段名设置为""的属性不与数据库做交互。
	private int testCol2;//当数据库字段或者前端传值，允许null值时，请勿使用基础类型声明属性，应使用类。例如： Integer

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	
}


```


### combo-dao使用

在本工程项目中spring `bean`中(比如service实现类)引入如下`@Autowired`即可使用

```java

	@Autowired ComboDao comboDao;//操作数据库的Dao
	
```


数据库操作使用样例：

```java

//通过建表语句创建表 或者 执行特殊修改操作的sql,可使用其它配置的数据源
String resMessage = comboDao.useTable("ALTER TABLE test MODIFY COLUMN test_name varchar(64)");
String resMessage = comboDao.useTable("ALTER TABLE test MODIFY COLUMN test_name varchar(64)", "dataSource2");

//清空表 truncate,可使用其它配置的数据源
String resMessage = comboDao.delTruncateTable(TableType.getTableType("testTableName"));
String resMessage = comboDao.delTruncateTable(TableType.getTableType("testTableName"), "dataSource2");

/**
   *保存list数据,使用其它配置的数据源
   * 一种是hashMap<String,Object>的list接收的,
   * 一种是实体类的对象list 接收的
*/   
String resMessage = comboDao.saveObj(List<T> list, TableType tableName);//当 T 为javaBean对象时,且有@Table注解时，tableName可为null，使用默认注解名
String resMessage = comboDao.saveObj(List<T> list, TableType tableName, String dataSourceName);//也可保存 List<Map<String,Object>>形式的数据

//还可保存单个对象数据,一种是hashMap<String,Object>接收的, 一种是实体类的对象的 接收的
//另外可以对javaBean对象执行删除，更新等操作。
//javaBean对象可以通过反射获取注解的数据库表名clazz.getAnnotation(Table.class).name();
String resMessage = comboDao.saveObj(Object obj);//对象的保存
String resMessage = comboDao.saveObj(Map<String,Object> obj,TableType tableName);//hashMap<String,Object>的保存

```

查询功能select 追求方法的简单干净，同时满足所有查询需求。

```java
//调用方法获取查询数据

//获取List<Map<String, Object>>数据
selectObjMap

//获取List<T> 对象数据
selectObj

```

查询条件设置参数传参

```java

/**
*可选paramMap
*/
Map<String, Object> paramMap = new HashMap<String, Object>();//会做 = 条件查询
paramMap.put("colName", value);//会下划线转驼峰数据库字段名为col_name
paramMap.put("col_name2", value2);//可以直接使用数据库小写字段名作为key

List<Map<String, Object>> data = selectObjMap(List<String> attributeNames, TableType tableName, paramMap);//获取map数据
List<User> users = selectObj(User.class, paramMap);//条件查询获取对象

/**
*可选dataSrouceName
*/
String dataSrouceName = "dataSrouce2";//不使用默认数据源时，传入其它数据源的springBean配置的id

List<Map<String, Object>> data = selectObjMap(List<String> attributeNames, TableType tableName, dataSrouceName);//其它数据源获取map数据
List<User> users = selectObj(User.class, dataSrouceName);//其它数据源查询获取对象

/**
* 可选QueryCondition
*    当paramMap不能满足特殊的查询需求时，使用 QueryCondition 的各种实现类，传入查询方法实现各种特殊条件查询
*    也可以自己实现QueryCondition 生成自需求的查询条件和传入参数
*
*    可选传参类型
* QueryCondition... 或者 QueryCondition[]或者 List<QueryCondition>
*/ 
QueryCondition[] queryConditions = new QueryCondition[3];
queryConditions[0] = new QueryConditionCommonLess("JLGXSJ", new Date(), "JLGXSJ_custom", true);
queryConditions[1] = new QueryConditionXzqh("XZQH", "510100");
queryConditions[2] = new QueryConditionCommonBetween ("update_time",new Date(0),new Date(),"updateTime",1); 

List<Map<String, Object>> data = selectObjMap(List<String> attributeNames, TableType tableName, queryConditions);//特殊或自定义获取map数据
List<User> users = selectObj(User.class, queryConditions);//特殊或自定义条件查询获取对象


/**
*获取查询结果，多种参数配置可选dataSrouceName,paramMap,queryConditions
*/
PageBean<TestPo> pageBean = comboDao.selectObj(1, 10,TestPo.class, paramMap, dataSourceName,queryConditions, new QueryConditionCommonGreater("test_id", "1", "testId_acf32d9", true));


```

关于 `驼峰` 转下划线的 规则，在生成sql时，javaBean对象的属性都会驼峰转下划线，paramMap查询条件参数的key会驼峰转下划线生成需要查询sql的数据库字段

`不会转下滑线` 的情况有 

1. 查询获取的不是javaBean对象，而是List<Map<String,Object>>时，传入的属性字段，就是数据库字段名。 解释: 自定义字段名 就是数据库字段名。

2. javaBean的属性使用了@Cloumn标注，而直接使用标注了的数据库字段名。

3. 或者用小写字母放入key和对象属性，就不存在驼峰，而直接使用数据库字段名。

### dao查询结果缓存 //TODO


```properties

#单机应用服务器缓存查询结果，避免过多访问数据库.
#是否开启查询结果缓存(数据缓存期内,数据库存在数据变更时可能存在脏读，请合理设置数据缓存和缓存时间)
dao.qureyDataCache=true
#查询结果缓存时间(单位秒s),默认5分钟
dao.queryDateCacheTime=300
#查询结果缓存最大记录数,默认500条
dao.queryDateCacheMaxSize=500

```

### 支持对springboot中使用ComboDao

springboot引入ComboDao的依赖时，直接自动装配。



## combo-util

## combo-util-cipher

## combo-util-chinese

## combo-util-json

## combo-util-xml

## combo-webservice

## combo-webservice-cxf
