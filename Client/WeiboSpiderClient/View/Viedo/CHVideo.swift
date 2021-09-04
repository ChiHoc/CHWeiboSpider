//
//  CHVideo.swift
//  CHVideo
//
//  Created by ChiHo on 2021/9/4.
//

import Foundation
import SwiftUI
import AVKit

struct CHVideo: View {
    
    let src: String
    
    init(
        src: String?
    ) {
        self.src = src?.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
    }
    
    var body: some View {
        if let url: URL = URL.init(string: src.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? "") {
            let player = AVPlayer(url: url)
            VStack {
                GeometryReader { geometry in
                    VideoPlayer(player: player)
                        .frame(width: geometry.size.width, height: geometry.size.height, alignment: .center)
                        .onAppear {
                            player.play()
                        }
                        .onDisappear {
                            player.pause()
                        }
                }
            }
            .aspectRatio(1.78, contentMode: .fit)
        }
    }
    
}
