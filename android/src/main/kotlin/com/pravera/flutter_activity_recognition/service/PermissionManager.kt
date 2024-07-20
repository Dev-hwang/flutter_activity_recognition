package com.pravera.flutter_activity_recognition.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pravera.flutter_activity_recognition.Constants
import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import com.pravera.flutter_activity_recognition.models.PermissionRequestResult
import io.flutter.plugin.common.PluginRegistry

class PermissionManager : PluginRegistry.RequestPermissionsResultListener {
	companion object {
		@SuppressLint("InlinedApi")
		const val permission = Manifest.permission.ACTIVITY_RECOGNITION
	}

	private var activity: Activity? = null
	private var onResult: ((PermissionRequestResult) -> Unit)? = null
	private var onError: ((ErrorCodes) -> Unit)? = null

	fun checkPermission(activity: Activity): PermissionRequestResult {
		// if your device is below Android 10, the system automatically grants permission.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			return PermissionRequestResult.GRANTED
		}

		val isGrantedPermission = ContextCompat.checkSelfPermission(activity, permission)
		if (isGrantedPermission == PackageManager.PERMISSION_GRANTED) {
			return PermissionRequestResult.GRANTED
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				val prevResult = getPrevPermissionRequestResult()
				if (prevResult != null
						&& prevResult == PermissionRequestResult.PERMANENTLY_DENIED
						&& !activity.shouldShowRequestPermissionRationale(permission))
					return PermissionRequestResult.PERMANENTLY_DENIED
			}

			return PermissionRequestResult.DENIED
		}
	}

	fun requestPermission(activity: Activity, onResult: ((PermissionRequestResult) -> Unit), onError: ((ErrorCodes) -> Unit)) {
		// if your device is below Android 10, the system automatically grants permission.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			onResult(PermissionRequestResult.GRANTED)
			return
		}

		this.activity = activity
		this.onResult = onResult
		this.onError = onError

		ActivityCompat.requestPermissions(
				activity, arrayOf(permission), Constants.ACTIVITY_RECOGNITION_PERMISSION_REQ_CODE)
	}

	private fun savePermissionRequestResult(result: PermissionRequestResult) {
		val prefs = activity?.getSharedPreferences(
					Constants.PERMISSION_REQUEST_RESULT_PREFS_NAME, Context.MODE_PRIVATE) ?: return

		with (prefs.edit()) {
			putString(permission, result.toString())
			commit()
		}
	}

	private fun getPrevPermissionRequestResult(): PermissionRequestResult? {
		val prefs = activity?.getSharedPreferences(
				Constants.PERMISSION_REQUEST_RESULT_PREFS_NAME, Context.MODE_PRIVATE) ?: return null

		val value = prefs.getString(permission, null) ?: return null
		return PermissionRequestResult.valueOf(value)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
		if (grantResults.isEmpty()) {
			onError?.invoke(ErrorCodes.PERMISSION_REQUEST_CANCELLED)
			return false
		}

		val index: Int
		var result: PermissionRequestResult = PermissionRequestResult.DENIED

		when (requestCode) {
			Constants.ACTIVITY_RECOGNITION_PERMISSION_REQ_CODE -> {
				index = permissions.indexOf(permission)
				if (index >= 0 && (grantResults[index] == PackageManager.PERMISSION_GRANTED)) {
					result = PermissionRequestResult.GRANTED
				} else {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
							activity?.shouldShowRequestPermissionRationale(permission) == false) {
						result = PermissionRequestResult.PERMANENTLY_DENIED
					}
				}
			}
			else -> return false
		}
		
		savePermissionRequestResult(result)
		onResult?.invoke(result)

		return true
	}
}
