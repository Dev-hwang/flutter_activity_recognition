//
//  ActivityRecognitionManager.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import CoreMotion
import Foundation

class ActivityRecognitionManager {
  private let motionActivityManager = CMMotionActivityManager()
  private let jsonEncoder = JSONEncoder()
  
  private var isRunningService = false

  func startService(handler: ActivityDataHandler) {
    if (isRunningService) {
      NSLog("The activity recognition service has already started.")
      stopService()
    }

    motionActivityManager.startActivityUpdates(to: .main) { activity in
      do {
        guard let activity = activity else { return }
        
        let activityData = ActivityData(from: activity)
        let activityJson = try self.jsonEncoder.encode(activityData)
        let activityJsonString = String(data: activityJson, encoding: .utf8)
        
        handler.onUpdate(activityJson: activityJsonString!)
      } catch {
        handler.onError(errorCode: ErrorCodes.ACTIVITY_DATA_ENCODING_FAILED)
      }
    }
    isRunningService = true
  }
  
  func stopService() {
    motionActivityManager.stopActivityUpdates()
    isRunningService = false
  }
}
