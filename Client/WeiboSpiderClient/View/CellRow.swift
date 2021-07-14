//
//  CellRow.swift
//  WeiboSpiderClient
//
//  Created by ChiHo on 2021/6/21.
//

import SwiftUI

struct CellRow: View {
    var data: ContentEntry
    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack(alignment: .center, spacing: 10) {
                AvatarView
                    .frame(width: 40, height: 40)
                    .clipShape(Circle())
                    .overlay(RoundedRectangle(cornerRadius: 20)
                        .stroke(Color.init(red: 0.6, green: 0.6, blue: 0.6), lineWidth: 1))
                
                VStack(alignment: .leading, spacing: 6) {
                    Text(data.user?.nickname ?? "")
                        .foregroundColor(Color.init(red: 0.2, green: 0.2, blue: 0.2))
                        .bold()
                        .font(.headline)
                    Text(data.publishTime ?? "")
                        .foregroundColor(Color.init(red: 0.6, green: 0.6, blue: 0.6))
                        .font(.subheadline)
                        .padding(.top, 1)
                }
            }
            .padding(.top, 10)
            VStack(alignment: .leading, spacing: 6) {
                Text(data.content ?? "")
                    .foregroundColor(Color.init(red: 0.2, green: 0.2, blue: 0.2))
                    .bold()
                    .font(Font.system(size: 16))
            }.padding(.vertical, 6)
        }
    }
    
    @ViewBuilder
    private var AvatarView: some View {
        if let url = URL.init(string: data.user?.avatar ?? "") {
           AsyncImage(url: url, placeholder: {
                Text("Loading ...")
            }, image: { Image(uiImage: $0).resizable() })
        } else {
            Image("")
        }
    }
}


