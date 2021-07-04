import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:flutter_activity_recognition/models/activity.dart';
import 'package:flutter_activity_recognition/models/activity_confidence.dart';
import 'package:flutter_activity_recognition/models/activity_type.dart';
import 'package:flutter_activity_recognition/models/permission_request_result.dart';

export 'package:flutter_activity_recognition/models/activity.dart';
export 'package:flutter_activity_recognition/models/activity_confidence.dart';
export 'package:flutter_activity_recognition/models/activity_type.dart';
export 'package:flutter_activity_recognition/models/permission_request_result.dart';

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
      final data = Map<String, dynamic>.from(jsonDecode(event));
      final type = getActivityTypeFromString(data['type']);
      final confidence = getActivityConfidenceFromString(data['confidence']);
      return Activity(type, confidence);
    });
  }

  /// Check whether activity recognition permission is granted.
  Future<PermissionRequestResult> checkPermission() async {
    final permissionResult =
        await _methodChannel.invokeMethod('checkPermission');
    return getPermissionRequestResultFromString(permissionResult);
  }

  /// Request activity recognition permission.
  Future<PermissionRequestResult> requestPermission() async {
    final permissionResult = (Platform.isAndroid)
        ? await _methodChannel.invokeMethod('requestPermission')
        : await _methodChannel.invokeMethod('checkPermission');
    return getPermissionRequestResultFromString(permissionResult);
  }
}
