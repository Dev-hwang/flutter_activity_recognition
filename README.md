This plugin is used to recognize user activity on Android and iOS platforms. To implement this plugin, Android used `ActivityRecognitionClient` and iOS used `CMMotionActivityManager`.

[![pub package](https://img.shields.io/pub/v/flutter_activity_recognition.svg)](https://pub.dev/packages/flutter_activity_recognition)

## Features

* Can request activity recognition permission.
* Can subscribe to `ActivityStream` to listen activity in real time.

## Getting started

To use this plugin, add `flutter_activity_recognition` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/). For example:

```yaml
dependencies:
  flutter_activity_recognition: ^4.0.0
```

After adding the `flutter_activity_recognition` plugin to the flutter project, we need to specify the platform-specific permissions to use for this plugin to work properly.

### :baby_chick: Android

Open the `AndroidManifest.xml` file and add the following permissions between the `<manifest>` and `<application>` tags.

```
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />
<uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION" />
```

### :baby_chick: iOS

Open the `ios/Runner/Info.plist` file and add the following permission inside the `<dict>` tag.

```
<key>NSMotionUsageDescription</key>
<string>Used to recognize user activity information.</string>
```

## How to use

1. Checks whether activity recognition permission is granted.

```dart
Future<bool> _checkAndRequestPermission() async {
  ActivityPermission permission =
      await FlutterActivityRecognition.instance.checkPermission();
  if (permission == ActivityPermission.PERMANENTLY_DENIED) {
    // permission has been permanently denied.
    return false;
  } else if (permission == ActivityPermission.DENIED) {
    permission =
        await FlutterActivityRecognition.instance.requestPermission();
    if (permission != ActivityPermission.GRANTED) {
      // permission is denied.
      return false;
    }
  }

  return true;
} 
```

2. To listen activity in real time, use the `activityStream` getter.

```dart
StreamSubscription<Activity>? _activitySubscription;

Future<void> _subscribeActivityStream() async {
  if (await _checkAndRequestPermission()) {
    _activitySubscription = FlutterActivityRecognition.instance.activityStream
        .handleError(_onError)
        .listen(_onActivity);
  }
}

void _onActivity(Activity activity) {
  print('activity detected >> ${activity.toJson()}');
}

void _onError(dynamic error) {
  print('error >> $error');
}
```

## Background Mode

If you want to use this plugin in the background, use the [`flutter_foreground_task`](https://pub.dev/packages/flutter_foreground_task) plugin.

## More Documentation

Go [here](./documentation/models_documentation.md) to learn about the `models` provided by this plugin.

Go [here](./documentation/migration_documentation.md) to `migrate` to the new version.

## Support

If you find any bugs or issues while using the plugin, please register an issues on [GitHub](https://github.com/Dev-hwang/flutter_activity_recognition/issues). You can also contact us at <hwj930513@naver.com>.
