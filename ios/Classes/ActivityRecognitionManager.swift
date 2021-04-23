//
//  ActivityRecognitionManager.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import CoreMotion
import Foundation

typealias ActivityDataHandler = (ActivityData) -> Void

class ActivityRecognitionManager {
  private let motionActivityManager = CMMotionActivityManager()
  private var isRunningService = false

  public func startService(handler: @escaping ActivityDataHandler) {
    if (isRunningService) {
      NSLog("The activity recognition service has already started.")
      stopService()
    }

    motionActivityManager.startActivityUpdates(to: .main) { (activity) in
      guard let activity = activity else { return }
      handler(ActivityData(from: activity))
    }
    isRunningService = true
  }
  
  public func stopService() {
    motionActivityManager.stopActivityUpdates()
    isRunningService = false
  }
}
