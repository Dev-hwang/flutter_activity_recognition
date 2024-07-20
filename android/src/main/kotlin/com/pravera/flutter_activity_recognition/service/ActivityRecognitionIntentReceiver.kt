package com.pravera.flutter_activity_recognition.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionResult

class ActivityRecognitionIntentReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		if (ActivityRecognitionResult.hasResult(intent)) {
			ActivityRecognitionIntentService.enqueueWork(context, intent)
		}
	}
}
