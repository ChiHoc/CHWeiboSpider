//
//  CHImagePreview.swift
//  CHImagePreview
//
//  Created by ChiHo on 2021/9/4.
//

import Foundation
import SwiftUI

struct CHImagePreview<Placeholder: View>: View {
    @Environment(\.viewController) private var holder
    @State private var appear = false
    private let view: Placeholder
    
    init(
        @ViewBuilder view: () -> Placeholder
    ) {
        self.view = view()
    }
    
    var body: some View {
        ZStack {
            Color.black.opacity(appear ? 0.8 : 0)
                .edgesIgnoringSafeArea(.all)
            view
                .scaledToFit()
                .scaleEffect(appear ? 1 : 0.5)
                .opacity(appear ? 1 : 0)
        }
        .onTapGesture {
            withAnimation(.easeInOut(duration: 0.3)) {
                self.appear = false
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                self.holder?.dismiss(animated: true, completion: .none)
            }
        }
        .onAppear {
            withAnimation(.easeInOut(duration: 0.3)) {
                self.appear = true
            }
        }
    }
}
