//
//  ContentView.swift
//  WeiboSpiderFlow
//
//  Created by ChiHo on 2021/6/8.
//

import SwiftUI

let limit = 10;

struct ContentView: View {
    
    var contentManager = NetworkManager<[ContentEntry]>(url: "http://localhost:8080")
    
    @State private var dataList = [ContentEntry]()
    
    @State private var isLoadAll: Bool = false
    
    private var RefreshListener: some View {
        HStack {
            if self.isLoadAll {
                Spacer()
                Text("已经到底啦")
                    .foregroundColor(Color.init(red: 0.6, green: 0.6, blue: 0.6))
                    .font(.subheadline)
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
            .navigationTitle("微博")
        }
    }
    
    private func refresh() {
        contentManager.fetch(parameters: ["offset": 0, "limit": limit],
                             success: { dataList in
                                self.dataList = dataList ?? []
                             }, failure: {
                                
                             })
    }
    
    private func fetch() {
        contentManager.fetch(parameters: ["offset": dataList.count, "limit": limit],
                             success: { dataList in
                                if (dataList?.count ?? 0 > 0) {
                                    self.dataList.append(contentsOf: dataList!)
                                } else {
                                    self.isLoadAll = true;
                                }
                             }, failure: {
                                
                             })
    }
}
