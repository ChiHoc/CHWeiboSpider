//
//  UIViewController+Present.swift
//  UIViewController+Present
//
//  Created by ChiHo on 2021/9/4.
//

import Foundation
import SwiftUI

struct ViewControllerHolder {
    weak var value: UIViewController?
}

struct ViewControllerKey: EnvironmentKey {
    static var defaultValue: ViewControllerHolder {
        return ViewControllerHolder(value: UIApplication.shared.windows.first?.rootViewController)
    }
}

extension EnvironmentValues {
    var viewController: UIViewController? {
        get { return self[ViewControllerKey.self].value }
        set { self[ViewControllerKey.self].value = newValue }
    }
}

extension UIViewController {
    func present<Content: View>(@ViewBuilder builder: () -> Content) {
        let toPresent = UIHostingController(rootView: AnyView(EmptyView()))
        toPresent.modalPresentationStyle = .overCurrentContext
        toPresent.modalTransitionStyle = .crossDissolve
        toPresent.view.backgroundColor = .clear
        toPresent.rootView = AnyView(builder().environment(\.viewController, toPresent))
        present(toPresent, animated: false, completion: .none)
    }
}
