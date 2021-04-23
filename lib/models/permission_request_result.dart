/// Defines the type of permission request result.
enum PermissionRequestResult {
  /// Occurs when the user grants permission.
  GRANTED,

  /// Occurs when the user denies permission.
  DENIED,

  /// Occurs when the user denies the permission once and chooses not to ask again.
  PERMANENTLY_DENIED
}

/// Returns the permission request result from [value].
PermissionRequestResult getPermissionRequestResultFromString(String? value) {
  return PermissionRequestResult.values.firstWhere(
      (e) => e.toString() == 'PermissionRequestResult.$value',
      orElse: () => PermissionRequestResult.PERMANENTLY_DENIED);
}
