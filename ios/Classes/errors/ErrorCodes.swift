//
//  ErrorCodes.swift
//  flutter_activity_recognition
//
//  Created by WOO JIN HWANG on 2021/04/23.
//

import Foundation

enum ErrorCodes : String {
  case ACTIVITY_USAGE_DESCRIPTION_NOT_FOUND
  case ACTIVITY_DATA_ENCODING_FAILED
  
  func message() -> String {
    switch self {
      case .ACTIVITY_USAGE_DESCRIPTION_NOT_FOUND:
        return "The motion usage description key could not be found in the Info.plist file. You are required to include the NSMotionUsageDescription key in your app's Info.plist file."
      case .ACTIVITY_DATA_ENCODING_FAILED:
        return "Failed to encode activity data in JSON format."
    }
  }
}
