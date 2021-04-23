package com.pravera.flutter_activity_recognition.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ActivityRecognitionIntentReceiver: BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		intent.setClass(context, ActivityRecognitionIntentService::class.java)
		ActivityRecognitionIntentService.enqueueWork(context, intent)
	}
}
