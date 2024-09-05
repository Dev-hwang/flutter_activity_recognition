import 'package:flutter_activity_recognition/models/activity_confidence.dart';
import 'package:flutter_activity_recognition/models/activity_type.dart';

/// A model representing the user's activity.
class Activity {
  /// The type of activity recognized.
  final ActivityType type;

  /// The confidence of activity recognized.
  final ActivityConfidence confidence;

  /// Constructs an instance of [Activity].
  const Activity(this.type, this.confidence);

  factory Activity.fromJson(Map<String, dynamic> json) {
    final ActivityType type = ActivityType.fromString(json['type']);
    final ActivityConfidence confidence =
        ActivityConfidence.fromString(json['confidence']);

    return Activity(type, confidence);
  }

  Map<String, dynamic> toJson() {
    return {
      'type': type.name,
      'confidence': confidence.name,
    };
  }

  @override
  bool operator ==(Object other) =>
      other is Activity && type == other.type && confidence == other.confidence;

  @override
  int get hashCode => type.hashCode ^ confidence.hashCode;
}
