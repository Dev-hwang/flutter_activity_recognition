package com.pravera.flutter_activity_recognition.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.google.android.gms.location.*
import com.pravera.flutter_activity_recognition.Constants
import com.pravera.flutter_activity_recognition.errors.ErrorCodes

class ActivityRecognitionManager: SharedPreferences.OnSharedPreferenceChangeListener {
	companion object {
		const val TAG = "ActivityRecognition"
		const val UPDATES_INTERVAL_MILLIS = 5000L
	}

	private var successCallback: (() -> Unit)? = null
	private var errorCallback: ((ErrorCodes) -> Unit)? = null
	private var updatesCallback: ((String) -> Unit)? = null

	private var pendingIntent: PendingIntent? = null
	private var serviceClient: ActivityRecognitionClient? = null

	fun startService(context: Context, onSuccess: (() -> Unit), onError: ((ErrorCodes) -> Unit),
			updatesListener: ((String) -> Unit)) {

		if (serviceClient != null) {
			Log.d(TAG, "The activity recognition service has already started.")
			stopService(context)
		}

		this.successCallback = onSuccess
		this.errorCallback = onError
		this.updatesCallback = updatesListener

		registerSharedPreferenceChangeListener(context)
		requestActivityUpdates(context)
	}

	fun stopService(context: Context) {
		unregisterSharedPreferenceChangeListener(context)
		removeActivityUpdates()

		this.errorCallback = null
		this.successCallback = null
		this.updatesCallback = null
	}

	private fun registerSharedPreferenceChangeListener(context: Context) {
		val prefs = context.getSharedPreferences(
				Constants.ACTIVITY_RECOGNITION_RESULT_PREFS_NAME, Context.MODE_PRIVATE) ?: return
		prefs.registerOnSharedPreferenceChangeListener(this)
		with (prefs.edit()) {
			remove(Constants.ACTIVITY_DATA_PREFS_KEY)
			remove(Constants.ACTIVITY_ERROR_PREFS_KEY)
			commit()
		}
	}

	private fun unregisterSharedPreferenceChangeListener(context: Context) {
		val prefs = context.getSharedPreferences(
				Constants.ACTIVITY_RECOGNITION_RESULT_PREFS_NAME, Context.MODE_PRIVATE) ?: return
		prefs.unregisterOnSharedPreferenceChangeListener(this)
	}

	@SuppressLint("MissingPermission")
	private fun requestActivityUpdates(context: Context) {
		pendingIntent = getPendingIntentForService(context)
		serviceClient = ActivityRecognition.getClient(context)

		val task = serviceClient?.requestActivityUpdates(UPDATES_INTERVAL_MILLIS, pendingIntent!!)
		task?.addOnSuccessListener { successCallback?.invoke() }
		task?.addOnFailureListener { errorCallback?.invoke(ErrorCodes.ACTIVITY_UPDATES_REQUEST_FAILED) }
	}

	@SuppressLint("MissingPermission")
	private fun removeActivityUpdates() {
		val task = serviceClient?.removeActivityUpdates(pendingIntent!!)
		task?.addOnSuccessListener { successCallback?.invoke() }
		task?.addOnFailureListener { errorCallback?.invoke(ErrorCodes.ACTIVITY_UPDATES_REMOVE_FAILED) }

		pendingIntent = null
		serviceClient = null
	}

	private fun getPendingIntentForService(context: Context): PendingIntent {
		val intent = Intent(context, ActivityRecognitionIntentReceiver::class.java)
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
		} else {
			PendingIntent.getBroadcast(context, 0, intent, 0)
		}
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
		when (key) {
			Constants.ACTIVITY_DATA_PREFS_KEY -> {
				val data = sharedPreferences.getString(key, null) ?: return
				updatesCallback?.invoke(data)
			}
			Constants.ACTIVITY_ERROR_PREFS_KEY -> {
				val error = sharedPreferences.getString(key, null) ?: return
				errorCallback?.invoke(ErrorCodes.valueOf(error))
			}
		}
	}
}
