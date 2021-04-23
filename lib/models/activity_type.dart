/// Defines the type of activity.
enum ActivityType {
  /// The device is in a vehicle, such as a car.
  IN_VEHICLE,

  /// The device is on a bicycle.
  ON_BICYCLE,

  /// The device is on a user who is walking or running.
  ON_FOOT,

  /// The device is on a user who is running. This is a sub-activity of ON_FOOT.
  RUNNING,

  /// The device is still (not moving).
  STILL,

  /// The device angle relative to gravity changed significantly.
  /// This often occurs when a device is picked up from a desk or a user who is sitting stands up.
  TILTING,

  /// The device is on a user who is walking. This is a sub-activity of ON_FOOT.
  WALKING,

  /// Unable to detect the current activity.
  UNKNOWN
}

/// Returns the activity type from [value].
ActivityType getActivityTypeFromString(String? value) {
  return ActivityType.values.firstWhere(
      (e) => e.toString() == 'ActivityType.$value',
      orElse: () => ActivityType.UNKNOWN);
}
