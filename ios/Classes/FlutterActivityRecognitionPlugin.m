#import "FlutterActivityRecognitionPlugin.h"
#if __has_include(<flutter_activity_recognition/flutter_activity_recognition-Swift.h>)
#import <flutter_activity_recognition/flutter_activity_recognition-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_activity_recognition-Swift.h"
#endif

@implementation FlutterActivityRecognitionPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterActivityRecognitionPlugin registerWithRegistrar:registrar];
}
@end
