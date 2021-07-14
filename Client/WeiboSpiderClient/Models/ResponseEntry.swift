//
//  ResponseEntry.swift
//  WeiboSpiderClient
//
//  Created by ChiHo on 2021/6/26.
//

import Foundation

struct ResponseEntry<DataType: Codable>: Codable {
    var data: DataType?
    var code: Int?
}
