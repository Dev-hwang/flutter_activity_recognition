package com.pravera.flutter_activity_recognition

import android.app.Activity
import com.pravera.flutter_activity_recognition.erros.ErrorCodes
import com.pravera.flutter_activity_recognition.service.ActivityRecognitionManager
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel

/** StreamHandlerImpl */
class StreamHandlerImpl: EventChannel.StreamHandler {
	private lateinit var eventChannel: EventChannel
	private lateinit var activityRecognitionManager: ActivityRecognitionManager

	private var activity: Activity? = null

	fun startListening(messenger: BinaryMessenger) {
		activityRecognitionManager = ActivityRecognitionManager()
		eventChannel = EventChannel(messenger, "flutter_activity_recognition/updates")
		eventChannel.setStreamHandler(this)
	}

	fun stopListening() {
		if (::eventChannel.isInitialized)
			eventChannel.setStreamHandler(null)
	}

	fun setActivity(activity: Activity?) {
		this.activity = activity
	}

	private fun handleError(events: EventChannel.EventSink?, errorCode: ErrorCodes) {
		events?.error(errorCode.toString(), null, null)
	}

	override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
		if (activity == null) return
		activityRecognitionManager.startService(
				activity!!,
				onSuccess = { },
				onError = { handleError(events, it) },
				updatesListener = { events?.success(it) }
		)
	}

	override fun onCancel(arguments: Any?) {
		if (activity == null) return
		activityRecognitionManager.stopService(activity!!)
	}
}
