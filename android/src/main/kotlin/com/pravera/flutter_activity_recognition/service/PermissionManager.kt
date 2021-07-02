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

class PermissionManager: PluginRegistry.RequestPermissionsResultListener {
	private var activity: Activity? = null

	private var resultCallback: ((PermissionRequestResult) -> Unit)? = null
	private var errorCallback: ((ErrorCodes) -> Unit)? = null

	fun checkPermission(activity: Activity, permission: String): PermissionRequestResult {
		// if your device is below Android 10, the system automatically grants permission.
		if (permission == Manifest.permission.ACTIVITY_RECOGNITION
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
			return PermissionRequestResult.GRANTED

		if (ContextCompat.checkSelfPermission(activity,
						permission) == PackageManager.PERMISSION_GRANTED) {
			return PermissionRequestResult.GRANTED
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				val prevResult = getPrevPermissionRequestResult(activity, permission)
				if (prevResult != null
						&& prevResult == PermissionRequestResult.PERMANENTLY_DENIED
						&& !activity.shouldShowRequestPermissionRationale(permission))
					return PermissionRequestResult.PERMANENTLY_DENIED
			}

			return PermissionRequestResult.DENIED
		}
	}

	fun requestPermission(activity: Activity, permission: String, requestCode: Int,
			onResult: ((PermissionRequestResult) -> Unit), onError: ((ErrorCodes) -> Unit)) {
		// if your device is below Android 10, the system automatically grants permission.
		if (permission == Manifest.permission.ACTIVITY_RECOGNITION
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			onResult(PermissionRequestResult.GRANTED)
			return
		}

		this.activity = activity
		this.resultCallback = onResult
		this.errorCallback = onError

		ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
	}

	private fun savePermissionRequestResult(
			activity: Activity?, permission: String, result: PermissionRequestResult) {
		val prefs = activity?.getSharedPreferences(
					Constants.PERMISSION_REQUEST_RESULT_PREFS_NAME, Context.MODE_PRIVATE) ?: return

		with (prefs.edit()) {
			putString(permission, result.toString())
			commit()
		}
	}

	private fun getPrevPermissionRequestResult(
			activity: Activity?, permission: String): PermissionRequestResult? {
		val prefs = activity?.getSharedPreferences(
				Constants.PERMISSION_REQUEST_RESULT_PREFS_NAME, Context.MODE_PRIVATE) ?: return null

		val value = prefs.getString(permission, null) ?: return null
		return PermissionRequestResult.valueOf(value)
	}

	@SuppressLint("InlinedApi")
	override fun onRequestPermissionsResult(
			requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
		if (grantResults.isEmpty()) {
			errorCallback?.invoke(ErrorCodes.PERMISSION_REQUEST_CANCELLED)
			return false
		}

		val pString: String
		val pIndex: Int
		var pResult: PermissionRequestResult = PermissionRequestResult.DENIED

		when (requestCode) {
			Constants.ACTIVITY_RECOGNITION_PERMISSION_REQ_CODE -> {
				pString = Manifest.permission.ACTIVITY_RECOGNITION
				pIndex = permissions.indexOf(pString)

				if (pIndex >= 0 && (grantResults[pIndex] == PackageManager.PERMISSION_GRANTED)) {
					pResult = PermissionRequestResult.GRANTED
				} else {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
							&& activity?.shouldShowRequestPermissionRationale(pString) == false)
						pResult = PermissionRequestResult.PERMANENTLY_DENIED
				}
			}
			else -> return false
		}
		
		savePermissionRequestResult(activity, pString, pResult)
		resultCallback?.invoke(pResult)

		return true
	}
}
