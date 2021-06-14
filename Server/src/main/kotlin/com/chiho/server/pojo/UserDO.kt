package com.chiho.server.pojo

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 5:02 下午
 */
data class UserDO(var id: String) {
    var nickname: String? = null
    var gender: String? = null
    var location: String? = null
    var birthday: String? = null
    var description: String? = null
    var verified_reason: String? = null
    var talent: String? = null
    var education: String? = null
    var work: String? = null
    var weiboNum: Int? = null
    var following: Int? = null
    var followers: Int? = null
}