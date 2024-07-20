//
//  ActivityPermission.swift
//  flutter_activity_recognition
//
//  Created by Woo Jin Hwang on 9/5/24.
//

import CoreMotion
import Foundation

enum ActivityPermission : String {
  case GRANTED
  case DENIED
  case PERMANENTLY_DENIED
  
  static func fromAuthorizationStatus(status: CMAuthorizationStatus) -> ActivityPermission {
    switch status {
      case .authorized:
        return ActivityPermission.GRANTED
      case .notDetermined, .restricted:
        return ActivityPermission.DENIED
      case .denied:
        return ActivityPermission.PERMANENTLY_DENIED
      default:
        return ActivityPermission.DENIED
    }
  }
}
