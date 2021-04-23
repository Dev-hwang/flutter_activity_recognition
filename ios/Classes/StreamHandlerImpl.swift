//
//  StreamHandlerImpl.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import Foundation

class StreamHandlerImpl {
  private let eventChannel: FlutterEventChannel
  private let activityRecognitionManager = ActivityRecognitionManager()
  
  init(messenger: FlutterBinaryMessenger) {
    self.eventChannel = FlutterEventChannel(
      name: "flutter_activity_recognition/updates", binaryMessenger: messenger)
    self.eventChannel.setStreamHandler(ActivityRecognitionStreamHandler(activityRecognitionManager))
  }
  
  private class ActivityRecognitionStreamHandler: NSObject, FlutterStreamHandler {
    private let jsonEncoder: JSONEncoder
    private let activityRecognitionManager: ActivityRecognitionManager
    
    init(_ activityRecognitionManager: ActivityRecognitionManager) {
      self.jsonEncoder = JSONEncoder()
      self.activityRecognitionManager = activityRecognitionManager
    }
    
    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
      activityRecognitionManager.startService { activityData in
        do {
          let activityDataJson = try self.jsonEncoder.encode(activityData)
          events(String(data: activityDataJson, encoding: .utf8))
        } catch {
          events(FlutterError(code: ErrorCodes.ACTIVITY_DATA_ENCODING_FAILED.rawValue, message: nil, details: nil))
        }
      }
      return nil
    }

    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
      activityRecognitionManager.stopService()
      return nil
    }
  }
}
