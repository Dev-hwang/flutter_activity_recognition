/// Defines the confidence of activity.
enum ActivityConfidence {
  /// High accuracy: 80~100
  HIGH,

  /// Medium accuracy: 50~80
  MEDIUM,

  /// Low accuracy: 0~50
  LOW;

  /// Returns the activity confidence from [value].
  static ActivityConfidence fromString(String? value) =>
      ActivityConfidence.values.firstWhere(
        (e) => e.toString() == 'ActivityConfidence.$value',
        orElse: () => ActivityConfidence.LOW,
      );
}
