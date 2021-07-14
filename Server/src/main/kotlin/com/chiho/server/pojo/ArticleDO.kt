package com.chiho.server.pojo

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 5:03 下午
 */
data class ArticleDO(var id: String) {
    var userId: String? = null
    var content: String? = null
    var articleUrl: String? = null
    var originalPictures: String? = null
    var retweetPictures: String? = null
    var original: Boolean = false
    var videoUrl: String? = null
    var publishPlace: String? = null
    var publishTime: String? = null
    var publishTool: String? = null
    var upNum: Int? = null
    var retweetNum: Int? = null
    var commentNum: Int? = null
}