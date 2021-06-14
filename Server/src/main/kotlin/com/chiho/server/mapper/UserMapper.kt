package com.chiho.server.mapper

import com.chiho.server.pojo.ArticleDO
import com.chiho.server.pojo.UserDO
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
interface UserMapper {

    @Select("SELECT * FROM user")
    fun list(): List<UserDO>
}