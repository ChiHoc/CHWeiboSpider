//
//  ContentModel.swift
//  WeiboSpiderFlow
//
//  Created by ChiHo on 2021/6/8.
//

import Foundation

struct ContentEntry: Codable {
    
    var id: String?
    var user: UserEntry?
    var content: String?
    var retweetUser: String?
    var retweetContent: String?
    var articleUrl: String?
    var originalPictures: Array<String>?
    var retweetPictures: Array<String>?
    var original: Bool?
    var videoUrl: String?
    var publishPlace: String?
    var publishTime: String?
    var publishTool: String?
    var upNum: Int?
    var retweetNum: Int?
    var commentNum: Int?
}
