## 4.0.0

* [**RENAME**] Rename `PermissionRequestResult` to `ActivityPermission`
* [**DOCS**] Update example and readme
* [**REFACTOR**] Refactored the code to make this plugin work properly

## 3.1.0

* [**FEAT**] Support AGP 8

## 3.0.0

* [**CHORE**] Update dependency constraints to `sdk: '>=2.18.0 <4.0.0'` `flutter: '>=3.3.0'`

## 2.0.0
* [**CHORE**] Upgrade minimum Flutter version to 3.0.0
* [**CHORE**] Upgrade dependencies

## 1.3.0

* [[#1](https://github.com/Dev-hwang/flutter_activity_recognition/pull/1)] Bump Android ext.kotlin_version to 1.5.31.

## 1.2.1

* Downgrade Android minSdkVersion to 21.

## 1.2.0

* Rollback to previous activity recognition API.
* Fixed an issue where IllegalArgumentException was thrown when starting the service on Android version 11 and higher.
* Bump Android minSdkVersion to 23.
* Bump Android compileSdkVersion to 31.

## 1.1.2

* Fix `requestPermission` not working properly.
* Change the `getActivityStream` function to a `getter` function.

## 1.1.0

* Move component declaration inside the plugin. Check the Android settings in the readme.
* Upgrade to `Activity Recognition Transition API`.
* Remove `ON_FOOT` activity type.
* Remove `TILTING` activity type.

## 1.0.2

* Change the toMap function name of the Activity model to toJson.

## 1.0.1

* Fix incorrect package name.

## 1.0.0

* Initial release.
