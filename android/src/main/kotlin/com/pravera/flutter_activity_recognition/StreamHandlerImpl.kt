package com.pravera.flutter_activity_recognition

import android.content.Context
import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import com.pravera.flutter_activity_recognition.service.ActivityDataCallback
import com.pravera.flutter_activity_recognition.service.ActivityRecognitionManager
import com.pravera.flutter_activity_recognition.utils.ErrorHandleUtils
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel

/** StreamHandlerImpl */
class StreamHandlerImpl(private val context: Context) : EventChannel.StreamHandler {
	private lateinit var eventChannel: EventChannel
	private lateinit var activityRecognitionManager: ActivityRecognitionManager

	private var binding: ActivityPluginBinding? = null

	fun init(messenger: BinaryMessenger) {
		activityRecognitionManager = ActivityRecognitionManager()
		eventChannel = EventChannel(messenger, "flutter_activity_recognition/updates")
		eventChannel.setStreamHandler(this)
	}

	fun dispose() {
		if (::eventChannel.isInitialized) {
			eventChannel.setStreamHandler(null)
		}
	}

	fun onAttachedToActivity(binding: ActivityPluginBinding) {
		this.binding = binding
	}

	fun onDetachedFromActivity() {
		binding = null
	}

	override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
		activityRecognitionManager.startService(
			context = context,
			callback = object : ActivityDataCallback {
				override fun onUpdate(activityJson: String) {
					events.success(activityJson)
				}

				override fun onError(errorCode: ErrorCodes) {
					ErrorHandleUtils.handleMethodCallError(events, errorCode)
				}
			}
		)
	}

	override fun onCancel(arguments: Any?) {
		activityRecognitionManager.stopService(context)
	}
}
