//
//  UserModel.swift
//  WeiboSpiderFlow
//
//  Created by ChiHo on 2021/6/8.
//

import Foundation

struct UserEntry: Codable {
    var id: String?
    var nickname: String?
    var avatar: String?
    var gender: String?
    var location: String?
    var birthday: String?
    var description: String?
    var talent: String?
    var education: String?
    var work: String?
    var weiboNum: Int?
    var following: Int?
    var followers: Int?
}
