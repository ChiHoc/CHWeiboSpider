//
//  CHImage.swift
//  WeiboSpiderClient
//
//  Created by ChiHo on 2021/7/31.
//

import Foundation
import SwiftUI

enum ImageType {
    case avatar, illustration
}

struct CHImage: View {
    
    @Environment(\.viewController) private var holder
    
    let src: String
    let type: ImageType
    
    init(
        src: String?,
        type: ImageType
    ) {
        self.src = src?.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        self.type = type
    }
    
    var body: some View {
        if let url = URL.init(string: src) {
            AsyncImage(url: url, placeholder: {
                if type == .avatar {
                    Image("avatar").resizable()
                } else if type == .illustration {
                    Image("illustration").resizable()
                }
            }, image: {
                Image(uiImage: $0)
                    .resizable()
            })
                .onTapGesture {
                    holder?.present { CHImagePreview(view: {
                        AsyncImage(url: url, placeholder: {
                            if type == .avatar {
                                Image("avatar").resizable()
                            } else if type == .illustration {
                                Image("illustration").resizable()
                            }
                        }, image: {
                            Image(uiImage: $0)
                                .resizable()
                        })
                    }) }
                }
        } else {
            if type == .avatar {
                Image("avatar").resizable()
            } else if type == .illustration {
                Image("illustration").resizable()
            }
        }
    }
    
}
