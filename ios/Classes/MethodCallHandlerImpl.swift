//
//  MethodCallHandlerImpl.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import Foundation

class MethodCallHandlerImpl : NSObject {
  private let methodChannel: FlutterMethodChannel
  private let activityPermissionManager: ActivityPermissionManager
  
  init(messenger: FlutterBinaryMessenger) {
    self.activityPermissionManager = ActivityPermissionManager()
    self.methodChannel = FlutterMethodChannel(name: "flutter_activity_recognition/method", binaryMessenger: messenger)
    super.init()
    self.methodChannel.setMethodCallHandler(onMethodCall)
  }
  
  func onMethodCall(call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
      case "checkPermission":
        let handler = ActivityPermissionHandlerImpl(result)
        activityPermissionManager.checkPermission(handler: handler)
        break
      case "requestPermission":
        let handler = ActivityPermissionHandlerImpl(result)
        activityPermissionManager.requestPermission(handler: handler)
        break
      default:
        result(FlutterMethodNotImplemented)
    }
  }
}
