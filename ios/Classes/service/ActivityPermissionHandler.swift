//
//  ActivityPermissionHandler.swift
//  flutter_activity_recognition
//
//  Created by Woo Jin Hwang on 9/5/24.
//

import Foundation

protocol ActivityPermissionHandler {
  func onResult(activityPermission: ActivityPermission)
  func onError(errorCode: ErrorCodes)
}

class ActivityPermissionHandlerImpl : NSObject, ActivityPermissionHandler {
  private let result: FlutterResult
  
  init(_ result: @escaping FlutterResult) {
    self.result = result
  }
  
  func onResult(activityPermission: ActivityPermission) {
    result(activityPermission.rawValue)
  }
  
  func onError(errorCode: ErrorCodes) {
    ErrorHandleUtils.handleMethodCallError(result: result, errorCode: errorCode)
  }
}
