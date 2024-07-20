package com.pravera.flutter_activity_recognition.errors

enum class ErrorCodes {
    ACTIVITY_NOT_ATTACHED,
    ACTIVITY_PERMISSION_REQUEST_CANCELLED,
    ACTIVITY_UPDATES_REQUEST_FAILED,
    ACTIVITY_UPDATES_REMOVE_FAILED,
    ACTIVITY_DATA_ENCODING_FAILED;

    fun message(): String {
        return when (this) {
            ACTIVITY_NOT_ATTACHED ->
                "Cannot call method because Activity is not attached to FlutterEngine."
            ACTIVITY_PERMISSION_REQUEST_CANCELLED ->
                "The activity permission request dialog was closed or the request was cancelled."
            ACTIVITY_UPDATES_REQUEST_FAILED ->
                "Failed to start activity recognition service."
            ACTIVITY_UPDATES_REMOVE_FAILED ->
                "Failed to stop activity recognition service."
            ACTIVITY_DATA_ENCODING_FAILED ->
                "Failed to encode activity data in JSON format."
        }
    }
}
