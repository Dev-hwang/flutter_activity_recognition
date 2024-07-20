package com.pravera.flutter_activity_recognition.utils

import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

/**
 * Utilities for error handling.
 *
 * @author Dev-hwang
 * @version 1.0
 */
class ErrorHandleUtils {
	companion object {
		/** Handles errors that occur in MethodChannel. */
		fun handleMethodCallError(result: MethodChannel.Result, errorCode: ErrorCodes) {
			result.error(errorCode.toString(), errorCode.message(), null)
		}

		/** Handles errors that occur in EventChannel. */
		fun handleMethodCallError(events: EventChannel.EventSink, errorCode: ErrorCodes) {
			events.error(errorCode.toString(), errorCode.message(), null)
		}
	}
}
