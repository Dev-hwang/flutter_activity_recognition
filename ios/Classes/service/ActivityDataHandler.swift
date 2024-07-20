//
//  ActivityDataHandler.swift
//  flutter_activity_recognition
//
//  Created by Woo Jin Hwang on 9/5/24.
//

import Foundation

protocol ActivityDataHandler {
  func onUpdate(activityJson: String)
  func onError(errorCode: ErrorCodes)
}

class ActivityDataHandlerImpl : NSObject, ActivityDataHandler {
  private let events: FlutterEventSink
  
  init(_ events: @escaping FlutterEventSink) {
    self.events = events
  }
  
  func onUpdate(activityJson: String) {
    events(activityJson)
  }
  
  func onError(errorCode: ErrorCodes) {
    ErrorHandleUtils.handleStreamError(events: events, errorCode: errorCode)
  }
}
