package com.pravera.flutter_activity_recognition

import android.content.Context
import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import com.pravera.flutter_activity_recognition.service.PermissionManager
import com.pravera.flutter_activity_recognition.utils.ErrorHandleUtils
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** MethodCallHandlerImpl */
class MethodCallHandlerImpl(private val context: Context) : MethodChannel.MethodCallHandler {
	private lateinit var methodChannel: MethodChannel
	private lateinit var permissionManager: PermissionManager

	private var binding: ActivityPluginBinding? = null

	fun init(messenger: BinaryMessenger) {
		permissionManager = PermissionManager()
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
		this.binding?.addRequestPermissionsResultListener(permissionManager)
	}

	fun onDetachedFromActivity() {
		binding?.removeRequestPermissionsResultListener(permissionManager)
		binding = null
	}

	override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
		val binding = this.binding
		if (binding == null) {
			ErrorHandleUtils.handleMethodCallError(result, ErrorCodes.ACTIVITY_NOT_ATTACHED)
			return
		}

		when (call.method) {
			"checkPermission" -> {
				val permissionRequestResult = permissionManager.checkPermission(binding.activity)
				result.success(permissionRequestResult.toString())
			}
			"requestPermission" -> {
				permissionManager.requestPermission(
					activity = binding.activity,
					onResult = { result.success(it.toString()) },
					onError = { ErrorHandleUtils.handleMethodCallError(result, it) }
				)
			}
			else -> result.notImplemented()
		}
	}
}
