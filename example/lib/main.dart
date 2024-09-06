import 'dart:async';
import 'dart:developer' as dev;

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_activity_recognition/flutter_activity_recognition.dart';

void main() => runApp(ExampleApp());

class ExampleApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _ExampleAppState();
}

class _ExampleAppState extends State<ExampleApp> {
  final ValueNotifier<ActivityResult?> _activityResult = ValueNotifier(null);

  StreamSubscription<Activity>? _activitySubscription;

  Future<void> _requestPermission() async {
    ActivityPermission permission =
        await FlutterActivityRecognition.instance.checkPermission();
    if (permission == ActivityPermission.PERMANENTLY_DENIED) {
      throw Exception('permission has been permanently denied.');
    } else if (permission == ActivityPermission.DENIED) {
      permission =
          await FlutterActivityRecognition.instance.requestPermission();
      if (permission != ActivityPermission.GRANTED) {
        throw Exception('permission is ${permission.name}.');
      }
    }
  }

  void _startService() async {
    try {
      await _requestPermission();

      // subscribe activity stream
      _activitySubscription = FlutterActivityRecognition.instance.activityStream
          .handleError(_onError)
          .listen(_onActivity);
    } catch (error) {
      _onError(error);
    }
  }

  void _onActivity(Activity activity) {
    dev.log('activity detected >> ${activity.toJson()}');
    _activityResult.value = ActivityResult.success(activity);
  }

  void _onError(dynamic error) {
    String errorMessage;
    if (error is PlatformException) {
      errorMessage = error.message ?? error.code;
    } else {
      errorMessage = error.toString();
    }

    dev.log('error >> $errorMessage');
    _activityResult.value = ActivityResult.error(errorMessage);
  }

  @override
  void initState() {
    super.initState();
    _startService();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Flutter Activity Recognition'),
          centerTitle: true,
        ),
        body: _buildContentView(),
      ),
    );
  }

  Widget _buildContentView() {
    return ValueListenableBuilder(
      valueListenable: _activityResult,
      builder: (context, result, _) {
        return ListView(
          physics: const BouncingScrollPhysics(),
          padding: const EdgeInsets.all(8.0),
          children: _buildListViewItems(result),
        );
      },
    );
  }

  List<Widget> _buildListViewItems(ActivityResult? result) {
    if (result == null) {
      return [
        Text('waiting for activity..'),
      ];
    }

    final String? error = result.error;
    if (error != null) {
      return [
        Text('time: ${DateTime.now()}'),
        Text('error: $error'),
      ];
    }

    return [
      Text('time: ${DateTime.now()}'),
      Text('type: ${result.data?.type}'),
      Text('confidence: ${result.data?.confidence}'),
    ];
  }

  @override
  void dispose() {
    _activitySubscription?.cancel();
    _activityResult.dispose();
    super.dispose();
  }
}

class ActivityResult {
  ActivityResult._private({this.data, this.error});

  final Activity? data;
  final String? error;

  factory ActivityResult.success(Activity data) =>
      ActivityResult._private(data: data);

  factory ActivityResult.error(String error) =>
      ActivityResult._private(error: error);
}
