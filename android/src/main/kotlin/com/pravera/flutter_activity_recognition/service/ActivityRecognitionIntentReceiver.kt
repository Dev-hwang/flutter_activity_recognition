package com.pravera.flutter_activity_recognition.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransitionResult

class ActivityRecognitionIntentReceiver: BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		if (ActivityTransitionResult.hasResult(intent)) {
			intent.setClass(context, ActivityRecognitionIntentService::class.java)
			ActivityRecognitionIntentService.enqueueWork(context, intent)
		}
	}
}
