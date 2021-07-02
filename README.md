This plugin is used to recognize user activity on Android and iOS platforms. To implement this plugin, Android used `ActivityRecognitionClient` and iOS used `CMMotionActivityManager`.

[![pub package](https://img.shields.io/pub/v/flutter_activity_recognition.svg)](https://pub.dev/packages/flutter_activity_recognition)

## Features

* Can check or request activity recognition permission.
* Subscribe to an activity stream to detect user activity in real time.

## Getting started

To use this plugin, add `flutter_activity_recognition` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/). For example:

```yaml
dependencies:
  flutter_activity_recognition: ^1.0.2
```

After adding the `flutter_activity_recognition` plugin to the flutter project, we need to specify the platform-specific permissions and services to use for this plugin to work properly.

### :baby_chick: Android

Open the `AndroidManifest.xml` file and add the following permissions between the `<manifest>` and `<application>` tags.

```
<uses-permission android:name="android.permission.WAKE_LOCK" />
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

1. Create a `FlutterActivityRecognition` instance.

```dart
final activityRecognition = FlutterActivityRecognition.instance;
```

2. Checks whether activity recognition permission is granted.

```dart
Future<bool> isPermissionGrants() async {
  // Check if the user has granted permission. If not, request permission.
  PermissionRequestResult reqResult;
  reqResult = await activityRecognition.checkPermission();
  if (reqResult == PermissionRequestResult.PERMANENTLY_DENIED) {
    dev.log('Permission is permanently denied.');
    return false;
  } else if (reqResult == PermissionRequestResult.DENIED) {
    reqResult = await activityRecognition.requestPermission();
    if (reqResult != PermissionRequestResult.GRANTED) {
      dev.log('Permission is denied.');
      return false;
    }
  }

  return true;
} 
```

3. Subscribe to an activity stream to receive activity data in real time.

```dart
// Subscribe to the activity stream.
final activityStreamSubscription = activityRecognition.getActivityStream()
  .handleError(_handleError).listen(_onActivityReceive);
```

4. When the widget is dispose or the plugin is finished using, cancel the subscription.

```dart
@override
void dispose() {
  activityStreamSubscription?.cancel();
  super.dispose();
}
```

## Models

### :chicken: PermissionRequestResult

Defines the type of permission request result.

| Value | Description |
|---|---|
| `GRANTED` | Occurs when the user grants permission. |
| `DENIED` | Occurs when the user denies permission. |
| `PERMANENTLY_DENIED` | Occurs when the user denies the permission once and chooses not to ask again. |

### :chicken: Activity

A model representing the user's activity.

| Property | Description |
|---|---|
| `type` | The type of activity recognized. |
| `confidence` | The confidence of activity recognized. |

### :chicken: ActivityType

Defines the type of activity.

| Value | Description |
|---|---|
| `IN_VEHICLE` | The device is in a vehicle, such as a car. |
| `ON_BICYCLE` | The device is on a bicycle. |
| `RUNNING` | The device is on a user who is running. This is a sub-activity of ON_FOOT. |
| `STILL` | The device is still (not moving). |
| `WALKING` | The device is on a user who is walking. This is a sub-activity of ON_FOOT. |
| `UNKNOWN` | Unable to detect the current activity. |

### :chicken: ActivityConfidence

Defines the confidence of activity.

| Value | Description |
|---|---|
| `HIGH` | High accuracy: 75~100 |
| `MEDIUM` | Medium accuracy: 50~75 |
| `LOW` | Low accuracy: 0~50 |

## Support

If you find any bugs or issues while using the plugin, please register an issues on [GitHub](https://github.com/Dev-hwang/flutter_activity_recognition/issues). You can also contact us at <hwj930513@naver.com>.
