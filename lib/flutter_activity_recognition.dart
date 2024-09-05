import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:flutter_activity_recognition/models/activity.dart';
import 'package:flutter_activity_recognition/models/activity_permission.dart';

export 'package:flutter_activity_recognition/models/activity.dart';
export 'package:flutter_activity_recognition/models/activity_confidence.dart';
export 'package:flutter_activity_recognition/models/activity_permission.dart';
export 'package:flutter_activity_recognition/models/activity_type.dart';

/// A class that provides an activity recognition API.
class FlutterActivityRecognition {
  FlutterActivityRecognition._internal();

  /// Instance of [FlutterActivityRecognition].
  static final instance = FlutterActivityRecognition._internal();

  /// Method channel to communicate with the platform.
  final _methodChannel = MethodChannel('flutter_activity_recognition/method');

  /// Event channel to communicate with the platform.
  final _eventChannel = EventChannel('flutter_activity_recognition/updates');

  /// Gets the activity stream.
  Stream<Activity> get activityStream {
    return _eventChannel.receiveBroadcastStream().map((event) {
      final Map<String, dynamic> json =
          Map<String, dynamic>.from(jsonDecode(event));

      return Activity.fromJson(json);
    });
  }

  /// Check whether activity recognition permission is granted.
  Future<ActivityPermission> checkPermission() async {
    final String? result = await _methodChannel.invokeMethod('checkPermission');
    return ActivityPermission.fromString(result);
  }

  /// Request activity recognition permission.
  Future<ActivityPermission> requestPermission() async {
    final String? result =
        await _methodChannel.invokeMethod('requestPermission');
    return ActivityPermission.fromString(result);
  }
}
