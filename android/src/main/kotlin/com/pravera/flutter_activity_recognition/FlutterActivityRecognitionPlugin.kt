package com.pravera.flutter_activity_recognition

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** FlutterActivityRecognitionPlugin */
class FlutterActivityRecognitionPlugin: FlutterPlugin, ActivityAware {
  private lateinit var methodCallHandler: MethodCallHandlerImpl
  private lateinit var streamHandler: StreamHandlerImpl

  override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    methodCallHandler = MethodCallHandlerImpl()
    methodCallHandler.startListening(binding.binaryMessenger)

    streamHandler = StreamHandlerImpl()
    streamHandler.startListening(binding.binaryMessenger)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    if (::methodCallHandler.isInitialized)
      methodCallHandler.stopListening()

    if (::streamHandler.isInitialized)
      streamHandler.stopListening()
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    methodCallHandler.setActivity(binding.activity)
    streamHandler.setActivity(binding.activity)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    methodCallHandler.setActivity(null)
    streamHandler.setActivity(null)
  }
}
