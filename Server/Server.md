新建工程选择“Spring Initializr” （本来以为是IDEA的bug，少了个e，结果人家真的叫这个）

依赖选择：

- Srping Native（据说是新的黑科技，看看是什么东西）
- Spring Boot DevTools（提供热更新，快速重启）
- Lombok（Model的好盆友，注解增加hashCode，copy等方法）
- Spring Configuration Processor（配置文件联想提醒）
- Spring Web
- Spring Security
- MyBatis Framework
- MySQL Driver

建好工程后直接运行：

```
Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
```

因为我们添加MyBatis依赖，所以需要提供数据库地址等配置

在application.propertites中添加配置：

```properties
# mysql
spring.datasource.url=jdbc:mysql://localhost:3306/weibo
spring.datasource.username=name
spring.datasource.password=pwd
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

重新run一遍，这次可以正常运行了，默认端口为8080。

欸？我们不是依赖了Srping Native，到底是个什么东西？我现在在使用了吗？

打开HELP.md，找到文档地址：https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/

哦吼，要安装docker，我的机器上没有，也没空间装了。

大概意思就是它可以直接打成一个image镜像，然后在docker上运行，省去写脚本打镜像的麻烦。

打开链接http://localhost:8080，发现重定向到登录界面。

这是因为我们依赖了Spring Security，他会自动启用默认配置，在web增加登录窗口。

在@SpringBootApplication注解增加忽略：

```kotlin
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
```

不用重新run，直接build完，DevTools会直接做热更新

刷新页面，就能正常发送请求

增加一个WebController，加上@RestController注解，提供"/"路径映射

```kotlin
@RestController
class WebController {

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(): String {
        return "Hello kotlin";
    }
}
```

build，刷新，嗯，正常。

接下来就是MyBatis查询数据库了，依然是HELP.md：https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start

教程很清楚了，就是Mapper查询数据，DO解析数据。

增加配置，让MyBatis映射的字段自动把下划线变成驼峰：

```kotlin
# MyBatis 下划线转驼峰
mybatis.configuration.map-underscore-to-camel-case=true
```

说好的Model好盆友lombok，在实际使用发现kotlin的语法糖中有个数据集data，直接可以取代lombok

```kotlin
data class ArticleDO(var id: String) {
    var userId: String? = null
    var content: String? = null
    var articleUrl: String? = null
    var originalPictures: String? = null
    var retweetPictures: String? = null
    var original: String? = null
    var videoUrl: String? = null
    var publishPlace: String? = null
    var publishTime: String? = null
    var publishTool: String? = null
    var upNum: Int? = null
    var retweetNum: Int? = null
    var commentNum: Int? = null
}
```

Mapper:

```kotlin
@Mapper
interface ArticleMapper {

    @Select("SELECT * FROM weibo ORDER BY publish_time desc LIMIT #{offset}, #{limit}")
    fun list(offset: Int = 0, limit: Int = 10): List<ArticleDO>
}
```

修改一下Controller：

```kotlin
@RequestMapping(value = ["/"], method = [RequestMethod.GET])
fun index(offset: Int?, limit: Int?): Map<String, List<ArticleDO>> {
    return mapOf("data" to articleMapper.list(offset ?: 0, limit ?: 10));
}
```

build，刷新，完美

正常来说，不应该直接吧DO传给前端展示，再封装一层VO，同时把userId解析成User：

```kotlin
data class ArticleVO(var id: String) {
    var user: UserVO? = null
    var content: String? = null
    var articleUrl: String? = null
    var originalPictures: String? = null
    var retweetPictures: String? = null
    var original: String? = null
    var videoUrl: String? = null
    var publishPlace: String? = null
    var publishTime: String? = null
    var publishTool: String? = null
    var upNum: Int? = null
    var retweetNum: Int? = null
    var commentNum: Int? = null

    constructor(articleDO: ArticleDO): this(articleDO.id) {
        this.content = articleDO.content
        this.articleUrl = articleDO.articleUrl
        this.originalPictures = articleDO.originalPictures
        this.retweetPictures = articleDO.retweetPictures
        this.original = articleDO.original
        this.videoUrl = articleDO.videoUrl
        this.publishPlace = articleDO.publishPlace
        this.publishTime = articleDO.publishTime
        this.publishTool = articleDO.publishTool
        this.upNum = articleDO.upNum
        this.retweetNum = articleDO.retweetNum
        this.commentNum = articleDO.commentNum
    }
}
```

增加了次构造函数，方便从DO转成VO

因为User表很少数据，干脆直接全拿回来做个map映射表好了

这里涉及到迟属性的语法糖，挺有趣的，类似单例的用法

```kotlin
@RestController
class WebController {

    @Autowired
    lateinit var articleMapper: ArticleMapper
    @Autowired
    lateinit var userMapper: UserMapper

    // 迟属性（lazy properties）: 其值只在首次访问时计算；
    val userMap: Map<String, UserDO> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        userMapper.list().associateBy { it.id }
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(offset: Int?, limit: Int?): Map<String, List<ArticleVO>> {
        val result: MutableList<ArticleVO> = mutableListOf()
        val articleDOList: List<ArticleDO> = articleMapper.list(offset ?: 0, limit ?: 10)
        for (articleDO in articleDOList) {
            val articleVO = ArticleVO(articleDO)
            // 防止null
            articleVO.user = userMap[articleDO.userId]?.let { UserVO(it) }
            result.add(articleVO)
        }
        return mapOf("data" to result)
    }
}
```

好了现在接口能正常访问了，还缺了最后一样东西，权限控制

因为没有用户系统，所以权限控制的思路简化成时间戳+key做DES加密，服务器端做逆向解密，若在失效时间内，则信任token。

加个Filter做拦截：

```kotlin
@Configuration
class WebConfig : WebMvcConfigurer {

    @Bean
    fun filterRegistrationBean(): FilterRegistrationBean<*> {
        val bean = FilterRegistrationBean<OncePerRequestFilter>()
        bean.setFilter(AuthFilter())
        bean.addUrlPatterns("/*")
        return bean
    }
}
```

完事了，可以部署上线