package com.pravera.flutter_activity_recognition.models

import com.google.gson.annotations.SerializedName

data class ActivityData(
	@SerializedName("type") val type: String,
	@SerializedName("confidence") val confidence: String
)
