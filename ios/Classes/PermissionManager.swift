//
//  PermissionManager.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import CoreMotion
import Foundation

typealias PermissionRequestResultHandler = (PermissionRequestResult) -> Void

class PermissionManager {
  private let motionActivityManager = CMMotionActivityManager()
  
  public func checkMotionActivityPermission(handler: @escaping PermissionRequestResultHandler) {
    let nowDate = Date()
    motionActivityManager.queryActivityStarting(from: nowDate, to: nowDate, to: .main) { (activities, error) in
      if let error = error, (error as NSError).code == CMErrorMotionActivityNotAuthorized.rawValue {
        handler(PermissionRequestResult.PERMANENTLY_DENIED)
        return
      }

      handler(PermissionRequestResult.GRANTED)
    }
  }
}
