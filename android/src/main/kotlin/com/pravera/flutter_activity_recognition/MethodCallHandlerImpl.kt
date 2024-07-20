package com.pravera.flutter_activity_recognition

import android.content.Context
import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import com.pravera.flutter_activity_recognition.models.ActivityPermission
import com.pravera.flutter_activity_recognition.service.ActivityPermissionCallback
import com.pravera.flutter_activity_recognition.service.ActivityPermissionManager
import com.pravera.flutter_activity_recognition.utils.ErrorHandleUtils
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** MethodCallHandlerImpl */
class MethodCallHandlerImpl(private val context: Context) : MethodChannel.MethodCallHandler {
	private lateinit var methodChannel: MethodChannel
	private lateinit var activityPermissionManager: ActivityPermissionManager

	private var binding: ActivityPluginBinding? = null

	fun init(messenger: BinaryMessenger) {
		activityPermissionManager = ActivityPermissionManager()
		methodChannel = MethodChannel(messenger, "flutter_activity_recognition/method")
		methodChannel.setMethodCallHandler(this)
	}

	fun dispose() {
		if (::methodChannel.isInitialized) {
			methodChannel.setMethodCallHandler(null)
		}
	}

	fun onAttachedToActivity(binding: ActivityPluginBinding) {
		this.binding = binding
		this.binding?.addRequestPermissionsResultListener(activityPermissionManager)
	}

	fun onDetachedFromActivity() {
		binding?.removeRequestPermissionsResultListener(activityPermissionManager)
		binding = null
	}

	override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
		val activity = binding?.activity
		if (activity == null) {
			ErrorHandleUtils.handleMethodCallError(result, ErrorCodes.ACTIVITY_NOT_ATTACHED)
			return
		}

		when (call.method) {
			"checkPermission" -> {
				val activityPermission = activityPermissionManager.checkPermission(activity)
				result.success(activityPermission.toString())
			}
			"requestPermission" -> {
				activityPermissionManager.requestPermission(
					activity = activity,
					callback = object : ActivityPermissionCallback {
						override fun onResult(activityPermission: ActivityPermission) {
							result.success(activityPermission.toString())
						}

						override fun onError(errorCode: ErrorCodes) {
							ErrorHandleUtils.handleMethodCallError(result, errorCode)
						}
					}
				)
			}
			else -> result.notImplemented()
		}
	}
}
