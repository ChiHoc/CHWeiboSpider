//
//  ContentView.swift
//  WeiboSpiderFlow
//
//  Created by ChiHo on 2021/6/8.
//

import SwiftUI
import SwiftUIRefresh

let limit = 10;

struct ContentView: View {
    
    var contentManager = NetworkManager<[ContentEntry]>(url: "http://192.168.1.55:8080")
    
    @State private var dataList = [ContentEntry]()
    
    @State private var isLoading: Bool = false
    @State private var isLoadAll: Bool = false
    @State private var isLoadFailed: Bool = false
    
    private var RefreshListener: some View {
        HStack {
            if self.isLoadAll {
                Spacer()
                Text("已经到底啦")
                    .foregroundColor(Color.init(red: 0.6, green: 0.6, blue: 0.6))
                    .font(.subheadline)
                Spacer()
            } else if self.isLoadFailed {
                Spacer()
                Text("加载失败，点击重试")
                    .foregroundColor(Color.init(red: 0.6, green: 0.6, blue: 0.6))
                    .font(.subheadline)
                    .onTapGesture {
                        fetch()
                        self.isLoadFailed = false
                    }
                Spacer()
            } else {
                Spacer()
                ProgressView()
                Spacer()
            }
        }
        .onAppear {
            fetch()
        }
    }
    
    var body: some View {
        NavigationView {
            List {
                ForEach(dataList, id: \.id) { data in
                    CellRow(data: data)
                }
                RefreshListener
            }
            .pullToRefresh(isShowing: $isLoading) {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                    self.isLoading = false
                    refresh()
                }
            }
            .navigationTitle("微博")
        }
    }
    
    private func refresh() {
        contentManager.fetch(parameters: ["offset": 0, "limit": limit],
                             success: { dataList in
            self.dataList = dataList ?? []
            self.isLoadAll = false
            self.isLoading = false
        }, failure: {
            self.isLoading = false
        })
    }
    
    private func fetch() {
        contentManager.fetch(parameters: ["offset": dataList.count, "limit": limit],
                             success: { dataList in
            if (dataList?.count ?? 0 > 0) {
                self.dataList.append(contentsOf: dataList!)
            } else {
                self.isLoadAll = true
            }
        }, failure: {
            self.isLoadFailed = true
        })
    }
}
