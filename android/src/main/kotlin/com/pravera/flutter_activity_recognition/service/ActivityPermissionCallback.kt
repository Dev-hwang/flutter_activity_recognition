package com.pravera.flutter_activity_recognition.service

import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import com.pravera.flutter_activity_recognition.models.ActivityPermission

interface ActivityPermissionCallback {
    fun onResult(activityPermission: ActivityPermission)
    fun onError(errorCode: ErrorCodes)
}
