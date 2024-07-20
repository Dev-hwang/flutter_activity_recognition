package com.pravera.flutter_activity_recognition.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.android.gms.location.*
import com.pravera.flutter_activity_recognition.Constants
import com.pravera.flutter_activity_recognition.errors.ErrorCodes

class ActivityRecognitionManager: SharedPreferences.OnSharedPreferenceChangeListener {
	companion object {
		private const val UPDATES_INTERVAL_MILLIS = 5000L
	}

	private var onSuccess: (() -> Unit)? = null
	private var onError: ((ErrorCodes) -> Unit)? = null
	private var onUpdate: ((String) -> Unit)? = null

	private var pendingIntent: PendingIntent? = null
	private var serviceClient: ActivityRecognitionClient? = null

	fun startService(context: Context, onSuccess: (() -> Unit), onError: ((ErrorCodes) -> Unit), onUpdate: ((String) -> Unit)) {
		// already started
		if (serviceClient != null) {
			stopService(context)
		}

		this.onSuccess = onSuccess
		this.onError = onError
		this.onUpdate = onUpdate

		registerSharedPreferenceChangeListener(context)
		requestActivityUpdates(context)
	}

	fun stopService(context: Context) {
		unregisterSharedPreferenceChangeListener(context)
		removeActivityUpdates()

		this.onSuccess = null
		this.onError = null
		this.onUpdate = null
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
		task?.addOnSuccessListener { onSuccess?.invoke() }
		task?.addOnFailureListener { onError?.invoke(ErrorCodes.ACTIVITY_UPDATES_REQUEST_FAILED) }
	}

	@SuppressLint("MissingPermission")
	private fun removeActivityUpdates() {
		val task = serviceClient?.removeActivityUpdates(pendingIntent!!)
		task?.addOnSuccessListener { onSuccess?.invoke() }
		task?.addOnFailureListener { onError?.invoke(ErrorCodes.ACTIVITY_UPDATES_REMOVE_FAILED) }

		pendingIntent = null
		serviceClient = null
	}

	private fun getPendingIntentForService(context: Context): PendingIntent {
		val intent = Intent(context, ActivityRecognitionIntentReceiver::class.java)
		return PendingIntent.getBroadcast(
				context, Constants.ACTIVITY_DETECTED, intent, PendingIntent.FLAG_MUTABLE)
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		when (key) {
			Constants.ACTIVITY_DATA_PREFS_KEY -> {
				val dataJson = sharedPreferences?.getString(key, null) ?: return
				onUpdate?.invoke(dataJson)
			}
			Constants.ACTIVITY_ERROR_PREFS_KEY -> {
				val error = sharedPreferences?.getString(key, null) ?: return
				onError?.invoke(ErrorCodes.valueOf(error))
			}
		}
	}
}
