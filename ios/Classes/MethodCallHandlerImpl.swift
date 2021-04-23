//
//  MethodCallHandlerImpl.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import Foundation

class MethodCallHandlerImpl {
  private let methodChannel: FlutterMethodChannel
  private let permissionManager = PermissionManager()
  
  init(messenger: FlutterBinaryMessenger) {
    self.methodChannel = FlutterMethodChannel(
      name: "flutter_activity_recognition/method", binaryMessenger: messenger)
    self.methodChannel.setMethodCallHandler(methodCallHandler)
  }
  
  private func methodCallHandler(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
      case "checkPermission":
        permissionManager.checkMotionActivityPermission { permissionRequestResult in
          result(permissionRequestResult.rawValue)
        }
        break
      default:
        result(FlutterMethodNotImplemented)
    }
  }
}
