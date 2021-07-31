package com.chiho.server.pojo

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 5:03 下午
 */
data class ArticleVO(var id: String) {

    private lateinit var domain: String

    var user: UserVO? = null
    var content: String? = null
    var retweetContent: String? = null
    var retweetUser: String? = null
    var articleUrl: String? = null
    var originalPictures: List<String>? = null
    var retweetPictures: List<String>? = null
    var original: Boolean = false
    var videoUrl: String? = null
    var publishPlace: String? = null
    var publishTime: String? = null
    var publishTool: String? = null
    var upNum: Int? = null
    var retweetNum: Int? = null
    var commentNum: Int? = null

    constructor(articleDO: ArticleDO) : this(articleDO.id) {
        this.articleUrl = articleDO.articleUrl
        this.content = articleDO.content
        this.originalPictures =
            if (!articleDO.originalPictures.equals("无")) articleDO.originalPictures?.split(",") else null
        this.retweetPictures =
            if (!articleDO.retweetPictures.equals("无")) articleDO.retweetPictures?.split(",") else null
        this.original = articleDO.original
        this.videoUrl = if (!articleDO.videoUrl.equals("无")) {
            val oriFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            "http://localhost:8080/assets/${articleDO.userId}/${LocalDate.parse(articleDO.publishTime, oriFormatter)
                .format(formatter)}_${articleDO.id}.mp4"
        } else null
        this.publishPlace =
            if (!articleDO.publishPlace.equals("无")) articleDO.publishPlace else null
        this.publishTime = articleDO.publishTime
        this.publishTool = articleDO.publishTool
        this.upNum = articleDO.upNum
        this.retweetNum = articleDO.retweetNum
        this.commentNum = articleDO.commentNum
        if (articleDO.original == true) {
            return
        }
        val ary = articleDO.content?.split("\n")
        if (ary == null || ary.size != 3) {
            return
        }
        this.content = ary[0].substring(5)
        this.retweetUser = ary[1].substring(5)
        this.retweetContent = ary[2].substring(5)
    }
}