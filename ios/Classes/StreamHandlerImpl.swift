//
//  StreamHandlerImpl.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import Foundation

class StreamHandlerImpl : NSObject, FlutterStreamHandler {
  private let eventChannel: FlutterEventChannel
  private let activityRecognitionManager: ActivityRecognitionManager
  
  init(messenger: FlutterBinaryMessenger) {
    self.activityRecognitionManager = ActivityRecognitionManager()
    self.eventChannel = FlutterEventChannel(name: "flutter_activity_recognition/updates", binaryMessenger: messenger)
    super.init()
    self.eventChannel.setStreamHandler(self)
  }
  
  func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
    let handler = ActivityDataHandlerImpl(events)
    activityRecognitionManager.startService(handler: handler)
    return nil
  }
  
  func onCancel(withArguments arguments: Any?) -> FlutterError? {
    activityRecognitionManager.stopService()
    return nil
  }
}
