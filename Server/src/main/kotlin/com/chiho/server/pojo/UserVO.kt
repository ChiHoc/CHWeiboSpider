package com.chiho.server.pojo

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 5:12 下午
 */
data class UserVO(var id: String) {
    var nickname: String? = null
    var gender: String? = null
    var avatar: String? = null
    var location: String? = null
    var birthday: String? = null
    var description: String? = null
    var talent: String? = null
    var education: String? = null
    var work: String? = null
    var weiboNum: Int? = null
    var following: Int? = null
    var followers: Int? = null

    constructor(userDO: UserDO): this(userDO.id) {
        this.nickname = userDO.nickname
        this.gender = userDO.gender
        this.avatar = userDO.avatar
        this.location = userDO.location
        this.birthday = userDO.birthday
        this.description = userDO.description
        this.talent = userDO.talent
        this.education = userDO.education
        this.work = userDO.work
        this.weiboNum = userDO.weiboNum
        this.following = userDO.following
        this.followers = userDO.followers
    }
}