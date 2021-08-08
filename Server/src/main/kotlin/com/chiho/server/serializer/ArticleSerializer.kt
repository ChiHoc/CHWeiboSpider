package com.chiho.server.serializer;

import com.chiho.server.mapper.UserMapper
import com.chiho.server.pojo.ArticleDO
import com.chiho.server.pojo.ArticleVO
import com.chiho.server.pojo.UserDO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/8/8
 * @Time: 10:37 上午
 */
@Component
class ArticleSerializer {

    @Value("\${server.domain}")
    private lateinit var domain: String

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var userSerializer: UserSerializer

    // 迟属性（lazy properties）: 其值只在首次访问时计算；
    val userMap: Map<String, UserDO> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        userMapper.list().associateBy { it.id }
    }

    fun serializer(articleDO: ArticleDO): ArticleVO {
        val articleVO = ArticleVO(articleDO.id)
        articleVO.articleUrl = articleDO.articleUrl
        articleVO.content = articleDO.content
        articleVO.originalPictures =
            if (!articleDO.originalPictures.equals("无")) {
                val list: MutableList<String> = mutableListOf()
                val ary = articleDO.originalPictures?.split(",")!!
                for (i in 1..ary.size) {
                    val split = ary[i-1].split(".")
                    val oriFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                    list.add("http://${domain}/assets/${articleDO.userId}/img/原创微博图片/${
                        LocalDate.parse(articleDO.publishTime, oriFormatter)
                            .format(formatter)
                    }_${articleDO.id}_${i}.${split[split.size -1]}")
                }
                list
            } else null
        articleVO.retweetPictures =
            if (!articleDO.retweetPictures.equals("无")) {
                val list: MutableList<String> = mutableListOf()
                val ary = articleDO.retweetPictures?.split(",")!!
                for (i in 1..ary.size) {
                    val split = ary[i-1].split(".")
                    val oriFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                    list.add("http://${domain}/assets/${articleDO.userId}/img/转发微博图片/${
                        LocalDate.parse(articleDO.publishTime, oriFormatter)
                            .format(formatter)
                    }_${articleDO.id}_${i}.${split[split.size -1]}")
                }
                list
            } else null
        articleVO.original = articleDO.original
        articleVO.videoUrl = if (!articleDO.videoUrl.equals("无")) {
            val oriFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            "http://${domain}/assets/${articleDO.userId}/video/${
                LocalDate.parse(articleDO.publishTime, oriFormatter)
                    .format(formatter)
            }_${articleDO.id}.mp4"
        } else null
        articleVO.publishPlace =
            if (!articleDO.publishPlace.equals("无")) articleDO.publishPlace else null
        articleVO.publishTime = articleDO.publishTime
        articleVO.publishTool = articleDO.publishTool
        articleVO.upNum = articleDO.upNum
        articleVO.retweetNum = articleDO.retweetNum
        articleVO.commentNum = articleDO.commentNum
        if (articleDO.original == true) {
            return articleVO
        }
        val ary = articleDO.content?.split("\n")
        if (ary == null || ary.size != 3) {
            return articleVO
        }
        articleVO.content = ary[0].substring(5)
        articleVO.retweetUser = ary[1].substring(5)
        articleVO.retweetContent = ary[2].substring(5)

        // User
        articleVO.user = userMap[articleDO.userId]?.let { userSerializer.serializer(it) }
        return articleVO
    }
}
