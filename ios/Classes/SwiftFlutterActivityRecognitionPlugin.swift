import Flutter
import UIKit

public class SwiftFlutterActivityRecognitionPlugin: NSObject, FlutterPlugin {
  private var methodCallHandler: MethodCallHandlerImpl? = nil
  private var streamHandler: StreamHandlerImpl? = nil
  
  public static func register(with registrar: FlutterPluginRegistrar) {
    let instance = SwiftFlutterActivityRecognitionPlugin()
    instance.setupChannels(registrar.messenger())
  }

  private func setupChannels(_ messenger: FlutterBinaryMessenger) {
    methodCallHandler = MethodCallHandlerImpl(messenger: messenger)
    streamHandler = StreamHandlerImpl(messenger: messenger)
  }
}
