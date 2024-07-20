package com.pravera.flutter_activity_recognition.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.gson.Gson
import com.pravera.flutter_activity_recognition.PreferencesKey
import com.pravera.flutter_activity_recognition.errors.ErrorCodes
import com.pravera.flutter_activity_recognition.models.ActivityData
import com.pravera.flutter_activity_recognition.utils.ActivityRecognitionUtils

class ActivityRecognitionIntentService : JobIntentService() {
	companion object {
		private val jsonConverter: Gson = Gson()

		fun enqueueWork(context: Context, intent: Intent) {
			enqueueWork(context, ActivityRecognitionIntentService::class.java, 0, intent)
		}
	}

	override fun onHandleWork(intent: Intent) {
		val result = ActivityRecognitionResult.extractResult(intent)
		val activities = result?.probableActivities

		val bestActivity = activities?.maxByOrNull { it.confidence } ?: return
		val activityData = ActivityData(
			ActivityRecognitionUtils.getActivityTypeFromValue(bestActivity.type),
			ActivityRecognitionUtils.getActivityConfidenceFromValue(bestActivity.confidence)
		)

		var prefsKey: String
		var prefsValue: String
		try {
			prefsKey = PreferencesKey.ACTIVITY_DATA
			prefsValue = jsonConverter.toJson(activityData)
		} catch (e: Exception) {
			prefsKey = PreferencesKey.ACTIVITY_ERROR
			prefsValue = ErrorCodes.ACTIVITY_DATA_ENCODING_FAILED.toString()
		}

		val prefs = getSharedPreferences(
			PreferencesKey.ACTIVITY_RECOGNITION_RESULT_PREFS, Context.MODE_PRIVATE) ?: return
		with (prefs.edit()) {
			putString(prefsKey, prefsValue)
			commit()
		}
	}
}
