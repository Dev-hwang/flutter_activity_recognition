package com.pravera.flutter_activity_recognition.service

import com.pravera.flutter_activity_recognition.errors.ErrorCodes

interface ActivityDataCallback {
    fun onUpdate(activityJson: String)
    fun onError(errorCode: ErrorCodes)
}
