package com.chiho.server.serializer;

import com.chiho.server.pojo.UserDO;
import com.chiho.server.pojo.UserVO;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/8/8
 * @Time: 10:37 上午
 */
@Component
class UserSerializer {

    fun serializer(userDO: UserDO): UserVO {
        val userVO = UserVO(userDO.id)
        userVO.nickname = userDO.nickname
        userVO.gender = userDO.gender
        userVO.avatar = userDO.avatar
        userVO.location = userDO.location
        userVO.birthday = userDO.birthday
        userVO.description = userDO.description
        userVO.talent = userDO.talent
        userVO.education = userDO.education
        userVO.work = userDO.work
        userVO.weiboNum = userDO.weiboNum
        userVO.following = userDO.following
        userVO.followers = userDO.followers
        return userVO
    }
}
