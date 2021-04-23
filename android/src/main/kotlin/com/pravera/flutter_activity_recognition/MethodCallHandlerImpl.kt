package com.pravera.flutter_activity_recognition

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.NonNull
import com.pravera.flutter_activity_recognition.erros.ErrorCodes
import com.pravera.flutter_activity_recognition.service.PermissionManager

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** MethodCallHandlerImpl */
class MethodCallHandlerImpl: MethodChannel.MethodCallHandler {
	private lateinit var methodChannel: MethodChannel
	private lateinit var permissionManager: PermissionManager

	private var activity: Activity? = null

	fun startListening(messenger: BinaryMessenger) {
		permissionManager = PermissionManager()
		methodChannel = MethodChannel(messenger, "flutter_activity_recognition/method")
		methodChannel.setMethodCallHandler(this)
	}

	fun stopListening() {
		if (::methodChannel.isInitialized)
			methodChannel.setMethodCallHandler(null)
	}

	fun setActivity(activity: Activity?) {
		this.activity = activity
	}

	private fun handleError(result: MethodChannel.Result, errorCode: ErrorCodes) {
		result.error(errorCode.toString(), null, null)
	}

	@SuppressLint("InlinedApi")
	override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
		if (activity == null) {
			handleError(result, ErrorCodes.ACTIVITY_NOT_REGISTERED)
			return
		}

		when (call.method) {
			"checkPermission" -> {
				val reqResult = permissionManager.checkPermission(
						activity!!, Manifest.permission.ACTIVITY_RECOGNITION)
				result.success(reqResult.toString())
			}
			"requestPermission" -> {
				permissionManager.requestPermission(
						activity!!,
						permission = Manifest.permission.ACTIVITY_RECOGNITION,
						requestCode = Constants.ACTIVITY_RECOGNITION_PERMISSION_REQ_CODE,
						onResult = { result.success(it.toString()) },
						onError = { handleError(result, it) }
				)
			}
			else -> result.notImplemented()
		}
	}
}
