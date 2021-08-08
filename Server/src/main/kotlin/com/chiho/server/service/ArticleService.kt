package com.chiho.server.service

import com.chiho.server.mapper.ArticleMapper
import com.chiho.server.pojo.ArticleDO
import com.chiho.server.pojo.ArticleVO
import com.chiho.server.serializer.ArticleSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/8/8
 * @Time: 11:08 上午
 */
@Service
class ArticleService {

    @Autowired
    lateinit var articleMapper: ArticleMapper

    @Autowired
    lateinit var articleSerializer: ArticleSerializer

    fun list(offset: Int?, limit: Int?): List<ArticleVO> {
        val result: MutableList<ArticleVO> = mutableListOf()
        val articleDOList: List<ArticleDO> = articleMapper.list(offset ?: 0, limit ?: 10)
        for (articleDO in articleDOList) {
            val articleVO = articleSerializer.serializer(articleDO)
            result.add(articleVO)
        }
        return result
    }
}