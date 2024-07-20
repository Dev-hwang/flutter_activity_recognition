package com.pravera.flutter_activity_recognition.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import com.google.android.gms.location.*
import com.pravera.flutter_activity_recognition.PreferencesKey
import com.pravera.flutter_activity_recognition.RequestCode
import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import io.flutter.Log

class ActivityRecognitionManager : SharedPreferences.OnSharedPreferenceChangeListener {
	companion object {
		private val TAG = ActivityRecognitionManager::class.java.simpleName
	}

	private var callback: ActivityDataCallback? = null

	private var pendingIntent: PendingIntent? = null
	private var serviceClient: ActivityRecognitionClient? = null

	fun startService(context: Context, callback: ActivityDataCallback) {
		if (serviceClient != null) {
			Log.d(TAG, "The activity recognition service has already started.")
			stopService(context)
		}

		this.callback = callback

		registerSharedPreferenceChangeListener(context)
		requestActivityUpdates(context)
	}

	fun stopService(context: Context) {
		unregisterSharedPreferenceChangeListener(context)
		removeActivityUpdates()

		this.callback = null
	}

	private fun registerSharedPreferenceChangeListener(context: Context) {
		val prefs = context.getSharedPreferences(
			PreferencesKey.ACTIVITY_RECOGNITION_RESULT_PREFS, Context.MODE_PRIVATE) ?: return
		prefs.registerOnSharedPreferenceChangeListener(this)
		with (prefs.edit()) {
			remove(PreferencesKey.ACTIVITY_DATA)
			remove(PreferencesKey.ACTIVITY_ERROR)
			commit()
		}
	}

	private fun unregisterSharedPreferenceChangeListener(context: Context) {
		val prefs = context.getSharedPreferences(
			PreferencesKey.ACTIVITY_RECOGNITION_RESULT_PREFS, Context.MODE_PRIVATE) ?: return
		prefs.unregisterOnSharedPreferenceChangeListener(this)
		with (prefs.edit()) {
			clear()
			commit()
		}
	}

	@SuppressLint("MissingPermission")
	private fun requestActivityUpdates(context: Context) {
		pendingIntent = getPendingIntent(context)
		serviceClient = ActivityRecognition.getClient(context)

		val task = serviceClient?.requestActivityUpdates(1000, pendingIntent!!)
		task?.addOnSuccessListener {
			// success
		}
		task?.addOnFailureListener {
			callback?.onError(ErrorCodes.ACTIVITY_UPDATES_REQUEST_FAILED)
		}
	}

	@SuppressLint("MissingPermission")
	private fun removeActivityUpdates() {
		val task = serviceClient?.removeActivityUpdates(pendingIntent!!)
		task?.addOnSuccessListener {
			// success
		}
		task?.addOnFailureListener {
			callback?.onError(ErrorCodes.ACTIVITY_UPDATES_REMOVE_FAILED)
		}

		pendingIntent = null
		serviceClient = null
	}

	private fun getPendingIntent(context: Context): PendingIntent {
		val intent = Intent(context, ActivityRecognitionIntentReceiver::class.java)
		var flags = PendingIntent.FLAG_UPDATE_CURRENT
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			flags = flags or PendingIntent.FLAG_MUTABLE
		}

		return PendingIntent.getBroadcast(context, RequestCode.ACTIVITY_DETECTED, intent, flags)
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		when (key) {
			PreferencesKey.ACTIVITY_DATA -> {
				val data = sharedPreferences?.getString(key, null) ?: return
				callback?.onUpdate(data)
			}
			PreferencesKey.ACTIVITY_ERROR -> {
				val error = sharedPreferences?.getString(key, null) ?: return
				callback?.onError(ErrorCodes.valueOf(error))
			}
		}
	}
}
