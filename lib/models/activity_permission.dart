/// Defines the type of permission request result.
enum ActivityPermission {
  /// Occurs when the user grants permission.
  GRANTED,

  /// Occurs when the user denies permission.
  DENIED,

  /// Occurs when the user denies the permission once and chooses not to ask again.
  PERMANENTLY_DENIED;

  /// Returns the permission request result from [value].
  static ActivityPermission fromString(String? value) =>
      ActivityPermission.values.firstWhere(
        (e) => e.toString() == 'ActivityPermission.$value',
        orElse: () => ActivityPermission.PERMANENTLY_DENIED,
      );
}
