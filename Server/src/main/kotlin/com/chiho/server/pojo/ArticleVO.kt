package com.chiho.server.pojo

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 5:03 下午
 */
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