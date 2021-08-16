//
//  ContentManager.swift
//  WeiboSpiderClient
//
//  Created by ChiHo on 2021/6/21.
//

import Foundation
import Alamofire
import CommonCrypto

extension String {

    func desEncrypt(key:String) -> String? {
        if let keyData = key.data(using: String.Encoding.utf8),
            let data = self.data(using: String.Encoding.utf8),
            let cryptData    = NSMutableData(length: Int((data.count)) + kCCBlockSizeDES) {


            let keyLength              = size_t(kCCKeySizeDES)
            let operation: CCOperation = UInt32(kCCEncrypt)
            let algoritm:  CCAlgorithm = UInt32(kCCAlgorithmDES)
            let options:   CCOptions   = UInt32(kCCOptionPKCS7Padding|kCCOptionECBMode)



            var numBytesEncrypted :size_t = 0

            let cryptStatus = CCCrypt(operation,
                                      algoritm,
                                      options,
                                      (keyData as NSData).bytes, keyLength,
                                      nil,
                                      (data as NSData).bytes, data.count,
                                      cryptData.mutableBytes, cryptData.length,
                                      &numBytesEncrypted)

            if cryptStatus == kCCSuccess {
                cryptData.length = Int(numBytesEncrypted)
                let base64cryptString = cryptData.base64EncodedString(options: .lineLength64Characters)
                return base64cryptString

            }
            else {
                return nil
            }
        }
        return nil
    }

    func desDecrypt(key:String) -> String? {
        if let keyData = key.data(using: String.Encoding.utf8),
            let data = NSData(base64Encoded: self, options: .ignoreUnknownCharacters),
            let cryptData    = NSMutableData(length: Int((data.length)) + kCCBlockSizeDES) {

            let keyLength              = size_t(kCCKeySizeDES)
            let operation: CCOperation = UInt32(kCCDecrypt)
            let algoritm:  CCAlgorithm = UInt32(kCCAlgorithmDES)
            let options:   CCOptions   = UInt32(kCCOptionPKCS7Padding|kCCOptionECBMode)

            var numBytesEncrypted :size_t = 0

            let cryptStatus = CCCrypt(operation,
                                      algoritm,
                                      options,
                                      (keyData as NSData).bytes, keyLength,
                                      nil,
                                      data.bytes, data.length,
                                      cryptData.mutableBytes, cryptData.length,
                                      &numBytesEncrypted)

            if cryptStatus == kCCSuccess {
                cryptData.length = Int(numBytesEncrypted)
                let unencryptedMessage = String(data: cryptData as Data, encoding:String.Encoding.utf8)
                return unencryptedMessage
            }
            else {
                return nil
            }
        }
        return nil
    }
}

class NetworkManager<ReturnType: Codable>: NSObject {
    
    var url: String
    
    init(url: String) {
        self.url = url
        AF.sessionConfiguration.timeoutIntervalForRequest = 8
    }
    
    func fetch(parameters: [String: Any]? = nil, success:@escaping ((_ dataList: ReturnType?) -> Void), failure:@escaping (() -> Void)) {
        let key: String = "TqV3fXPY"
        let id: String = "WeiboSpider"
        let data = "\(id)|\(Int(Date().timeIntervalSince1970*1000))"

        let auth_header = HTTPHeaders.init([ "AUTH-TOKEN" : data.desEncrypt(key: key) ?? "" ])
        AF.request(self.url, method: .get, parameters: parameters, headers: auth_header).validate().responseJSON { response in
            switch response.result {
            case .success(let json):
//                debugPrint(json)
                if let data = response.data {
                    do {
                        let returnData = try JSONDecoder().decode(ResponseEntry<ReturnType>.self, from: data)
                        DispatchQueue.main.async {
                            success(returnData.data)
                        }
                    } catch {
                        print(error)
                        failure()
                    }
                }
            case .failure(let error):
                print(error)
                failure()
            }
        }
    }
}
