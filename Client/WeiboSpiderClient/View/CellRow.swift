//
//  CellRow.swift
//  WeiboSpiderClient
//
//  Created by ChiHo on 2021/6/21.
//

import SwiftUI

struct CellRow: View {
    @State var data: ContentEntry
    private let gridItemLayout = [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack(alignment: .center, spacing: 10) {
                CHImage(src: data.user?.avatar, type: .avatar)
                    .scaledToFit()
                    .frame(width: 40, height: 40)
                    .clipShape(Circle())
                    .overlay(RoundedRectangle(cornerRadius: 20)
                                .stroke(Color.init(red: 0.6, green: 0.6, blue: 0.6), lineWidth: 1))
                
                VStack(alignment: .leading, spacing: 6) {
                    Text(data.user?.nickname ?? "")
                        .foregroundColor(Color.init(red: 0.2, green: 0.2, blue: 0.2))
                        .bold()
                        .font(Font.system(size: 15))
                        .padding(.top, 6)
                    Text(data.publishTime ?? "")
                        .foregroundColor(Color.init(red: 0.6, green: 0.6, blue: 0.6))
                        .font(Font.system(size: 14))
                }
            }
            .padding(.top, 10)
            Text(data.content ?? "")
                .foregroundColor(Color.init(red: 0.2, green: 0.2, blue: 0.2))
                .font(Font.system(size: 14))
                .lineSpacing(6)
                .fixedSize(horizontal: false, vertical: true)
            if (data.originalPictures?.count != 0) {
                LazyVGrid(columns: gridItemLayout, spacing: 20) {
                    ForEach(data.originalPictures ?? [], id: \.self) { data in
                        CHImage(src: data, type: .illustration)
                            .scaledToFit()
                            .aspectRatio(1, contentMode: .fill)
                            .clipShape(
                                Rectangle()
                            )
                    }
                    .padding(.top, 10)
                }
            }
            if (data.original! && data.videoUrl != nil) {
                CHVideo(src: data.videoUrl)
            }
            if (!data.original!) {
                VStack(alignment: .leading, spacing: 6) {
                    Text("@\(data.retweetUser ?? "")")
                        .foregroundColor(Color.init(red: 0.2, green: 0.2, blue: 0.2))
                        .font(Font.system(size: 13))
                        .bold()
                        .fixedSize(horizontal: false, vertical: true)
                    Text(data.retweetContent ?? "")
                        .foregroundColor(Color.init(red: 0.2, green: 0.2, blue: 0.2))
                        .font(Font.system(size: 13))
                        .lineSpacing(6)
                        .fixedSize(horizontal: false, vertical: true)
                    if (data.retweetPictures?.count != 0) {
                        LazyVGrid(columns: gridItemLayout, spacing: 20) {
                            ForEach(data.retweetPictures ?? [], id: \.self) { data in
                                CHImage(src: data, type: .illustration)
                                    .scaledToFit()
                                    .aspectRatio(1, contentMode: .fill)
                                    .clipShape(
                                        Rectangle()
                                    )
                            }
                            .padding(.top, 10)
                        }
                    }
                    if (data.videoUrl != nil) {
                        CHVideo(src: data.videoUrl)
                    }
                }
                .padding(6)
                .frame(
                    minWidth: 0,
                    maxWidth: .infinity,
                    minHeight: 0,
                    maxHeight: .infinity,
                    alignment: .topLeading
                )
                .background(Color.init(red: 0.95, green: 0.95, blue: 0.95))
            }
        }
    }
}


