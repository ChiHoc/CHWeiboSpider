package com.chiho.server.mapper

import com.chiho.server.pojo.ArticleDO
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 5:03 下午
 */
@Mapper
interface ArticleMapper {

    @Select("SELECT * FROM weibo ORDER BY publish_time desc LIMIT #{offset}, #{limit}")
    fun list(offset: Int = 0, limit: Int = 10): List<ArticleDO>
}