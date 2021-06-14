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

  /// Returns the data fields of [Activity] in JSON format.
  Map<String, dynamic> toJson() => {'type': type, 'confidence': confidence};

  /// Gets an activity of type UNKNOWN.
  static Activity get unknown =>
      Activity(ActivityType.UNKNOWN, ActivityConfidence.LOW);

  @override
  bool operator ==(Object other) =>
      other is Activity &&
      runtimeType == other.runtimeType &&
      type == other.type &&
      confidence == other.confidence;

  @override
  int get hashCode => type.hashCode ^ confidence.hashCode;
}
