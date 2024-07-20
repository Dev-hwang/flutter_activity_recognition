//
//  ErrorHandleUtils.swift
//  flutter_activity_recognition
//
//  Created by Woo Jin Hwang on 9/5/24.
//

import Flutter
import Foundation

class ErrorHandleUtils {
  static func handleMethodCallError(result: FlutterResult, errorCode: ErrorCodes) {
    let error = FlutterError(code: errorCode.rawValue, message: errorCode.message(), details: nil)
    result(error)
  }
  
  static func handleStreamError(events: FlutterEventSink, errorCode: ErrorCodes) {
    let error = FlutterError(code: errorCode.rawValue, message: errorCode.message(), details: nil)
    events(error)
  }
}
