package com.pravera.flutter_activity_recognition

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** FlutterActivityRecognitionPlugin */
class FlutterActivityRecognitionPlugin : FlutterPlugin, ActivityAware {
  private lateinit var methodCallHandler: MethodCallHandlerImpl
  private lateinit var streamHandler: StreamHandlerImpl

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    methodCallHandler = MethodCallHandlerImpl(binding.applicationContext)
    methodCallHandler.init(binding.binaryMessenger)

    streamHandler = StreamHandlerImpl(binding.applicationContext)
    streamHandler.init(binding.binaryMessenger)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    if (::methodCallHandler.isInitialized) {
      methodCallHandler.dispose()
    }

    if (::streamHandler.isInitialized) {
      streamHandler.dispose()
    }
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    methodCallHandler.onAttachedToActivity(binding)
    streamHandler.onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    methodCallHandler.onDetachedFromActivity()
    streamHandler.onDetachedFromActivity()
  }
}
