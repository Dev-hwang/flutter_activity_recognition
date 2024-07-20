package com.pravera.flutter_activity_recognition

object PreferencesKey {
    private const val prefix = "com.pravera.flutter_activity_recognition.prefs."

    // permissions
    const val ACTIVITY_PERMISSION_STATUS_PREFS = prefix + "ACTIVITY_PERMISSION_STATUS"

    // service
    const val ACTIVITY_RECOGNITION_RESULT_PREFS = prefix + "ACTIVITY_RECOGNITION_RESULT"
    const val ACTIVITY_DATA = "ACTIVITY_DATA"
    const val ACTIVITY_ERROR = "ACTIVITY_ERROR"
}
