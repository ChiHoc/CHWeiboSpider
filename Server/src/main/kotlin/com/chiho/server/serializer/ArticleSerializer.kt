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

        // User
        articleVO.user = userMap[articleDO.userId]?.let { userSerializer.serializer(it) }

        articleVO.articleUrl = articleDO.articleUrl
        articleVO.content = articleDO.content
        articleVO.originalPictures = parseImages(
            articleDO.originalPictures,
            "原创微博图片",
            articleDO.userId,
            articleDO.publishTime,
            articleDO.id
        )
        articleVO.original = articleDO.original
        articleVO.videoUrl = parseVideo(
            articleDO.videoUrl,
            articleDO.userId,
            articleDO.publishTime,
            articleDO.id
        )
        articleVO.publishPlace =
            if (!articleDO.publishPlace.equals("无")) articleDO.publishPlace else null
        articleVO.publishTime = articleDO.publishTime
        articleVO.publishTool = articleDO.publishTool
        articleVO.upNum = articleDO.upNum
        articleVO.retweetNum = articleDO.retweetNum
        articleVO.commentNum = articleDO.commentNum
        if (articleDO.original == true) {
            // 原创
            return articleVO
        }
        // 转载
        articleVO.retweetPictures = parseImages(
            articleDO.retweetPictures,
            "转发微博图片",
            articleDO.userId,
            articleDO.publishTime,
            articleDO.id
        )

        val ary = articleDO.content?.split("\n")
        if (ary == null || ary.size != 3) {
            return articleVO
        }
        articleVO.content = ary[0].substring(5)
        articleVO.retweetUser = ary[1].substring(5)
        articleVO.retweetContent = ary[2].substring(5)
        return articleVO
    }

    /*解析图片*/
    fun parseImages(
        imageString: String?,
        type: String,
        userId: String?,
        publishTime: String?,
        id: String?
    ): List<String>? {
        if (imageString?.isBlank() == true || imageString.equals("无")) {
            return null;
        }
        val list: MutableList<String> = mutableListOf()
        val ary = imageString!!.split(",")!!
        if (ary.size == 1) {
            val split = ary[0].split(".")
            val oriFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            list.add(
                "http://${domain}/assets/${userId}/img/${type}/${
                    LocalDate.parse(publishTime, oriFormatter)
                        .format(formatter)
                }_${id}.${split[split.size - 1]}"
            )
        }
        if (ary.size > 1) {
            for (i in 1..ary.size) {
                val split = ary[i - 1].split(".")
                val oriFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                list.add(
                    "http://${domain}/assets/${userId}/img/${type}/${
                        LocalDate.parse(publishTime, oriFormatter)
                            .format(formatter)
                    }_${id}_${i}.${split[split.size - 1]}"
                )
            }
        }
        return list
    }

    /*解析视频*/
    fun parseVideo(
        videoUrl: String?,
        userId: String?,
        publishTime: String?,
        id: String?
    ): String? {
        if (videoUrl.equals("无")) {
            return null
        }
        val oriFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return "http://${domain}/assets/${userId}/video/${
            LocalDate.parse(publishTime, oriFormatter)
                .format(formatter)
        }_${id}.mp4"
    }
}