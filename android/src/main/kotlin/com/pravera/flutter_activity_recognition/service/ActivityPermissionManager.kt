package com.pravera.flutter_activity_recognition.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pravera.flutter_activity_recognition.PreferencesKey
import com.pravera.flutter_activity_recognition.RequestCode
import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import com.pravera.flutter_activity_recognition.models.ActivityPermission
import io.flutter.plugin.common.PluginRegistry

class ActivityPermissionManager : PluginRegistry.RequestPermissionsResultListener {
	companion object {
		@SuppressLint("InlinedApi")
		const val permission = Manifest.permission.ACTIVITY_RECOGNITION
	}

	private var activity: Activity? = null
	private var callback: ActivityPermissionCallback? = null

	fun checkPermission(activity: Activity): ActivityPermission {
		// if your device is below Android 10, the system automatically grants permission.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			return ActivityPermission.GRANTED
		}

		if (activity.isPermissionGranted()) {
			return ActivityPermission.GRANTED
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val prevPermissionStatus = activity.getPrevPermissionStatus()
			if (prevPermissionStatus != null
				&& prevPermissionStatus == ActivityPermission.PERMANENTLY_DENIED
				&& !activity.shouldShowRequestPermissionRationale(permission)) {
				return ActivityPermission.PERMANENTLY_DENIED
			}
		}

		return ActivityPermission.DENIED
	}

	fun requestPermission(activity: Activity, callback: ActivityPermissionCallback) {
		// if your device is below Android 10, the system automatically grants permission.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			callback.onResult(ActivityPermission.GRANTED)
			return
		}

		this.activity = activity
		this.callback = callback

		ActivityCompat.requestPermissions(
			activity, arrayOf(permission), RequestCode.REQUEST_ACTIVITY_PERMISSION)
	}

	private fun Context.isPermissionGranted(): Boolean {
		return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
	}

	private fun Context.setPrevPermissionStatus(status: ActivityPermission) {
		val prefs = getSharedPreferences(
			PreferencesKey.ACTIVITY_PERMISSION_STATUS_PREFS, Context.MODE_PRIVATE) ?: return
		with (prefs.edit()) {
			putString(permission, status.toString())
			commit()
		}
	}

	private fun Context.getPrevPermissionStatus(): ActivityPermission? {
		val prefs = getSharedPreferences(
			PreferencesKey.ACTIVITY_PERMISSION_STATUS_PREFS, Context.MODE_PRIVATE) ?: return null
		val value = prefs.getString(permission, null) ?: return null

		return ActivityPermission.valueOf(value)
	}

	private fun disposeReference() {
		this.activity = null
		this.callback = null
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
		if (grantResults.isEmpty()) {
			callback?.onError(ErrorCodes.ACTIVITY_PERMISSION_REQUEST_CANCELLED)
			disposeReference()
			return false
		}

		val permissionIndex: Int
		var permissionStatus = ActivityPermission.DENIED

		when (requestCode) {
			RequestCode.REQUEST_ACTIVITY_PERMISSION -> {
				permissionIndex = permissions.indexOf(permission)
				if (permissionIndex >= 0
						&& grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED) {
					permissionStatus = ActivityPermission.GRANTED
				} else {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
							activity?.shouldShowRequestPermissionRationale(permission) == false) {
						permissionStatus = ActivityPermission.PERMANENTLY_DENIED
					}
				}
			}
			else -> return false
		}
		
		activity?.setPrevPermissionStatus(permissionStatus)
		callback?.onResult(permissionStatus)
		disposeReference()

		return true
	}
}
