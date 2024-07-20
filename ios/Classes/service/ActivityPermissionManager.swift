//
//  ActivityPermissionManager.swift
//  flutter_activity_recognition
//
//  Created by Woo Jin Hwang on 9/5/24.
//

import CoreMotion
import Foundation

class ActivityPermissionManager {
  private let motionActivityManager = CMMotionActivityManager()
  
  func checkPermission(handler: ActivityPermissionHandler) {
    let status = CMMotionActivityManager.authorizationStatus()
    let activityPermission = ActivityPermission.fromAuthorizationStatus(status: status)
    handler.onResult(activityPermission: activityPermission)
  }
  
  func requestPermission(handler: ActivityPermissionHandler) {
    if containsMotionUsageDescription() {
      let now = Date()
      motionActivityManager.queryActivityStarting(from: now, to: now, to: .main) { _, __ in
        self.checkPermission(handler: handler)
      }
    } else {
      handler.onError(errorCode: ErrorCodes.ACTIVITY_USAGE_DESCRIPTION_NOT_FOUND)
    }
  }
  
  func containsMotionUsageDescription() -> Bool {
    return Bundle.main.object(forInfoDictionaryKey: "NSMotionUsageDescription") != nil
  }
}
