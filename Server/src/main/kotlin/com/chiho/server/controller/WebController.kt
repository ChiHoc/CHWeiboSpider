package com.chiho.server.controller

import com.chiho.server.mapper.ArticleMapper
import com.chiho.server.mapper.UserMapper
import com.chiho.server.pojo.ArticleDO
import com.chiho.server.pojo.ArticleVO
import com.chiho.server.pojo.UserDO
import com.chiho.server.pojo.UserVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 4:28 下午
 */
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