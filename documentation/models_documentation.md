## Models

### :chicken: ActivityPermission

Defines the type of permission request result.

| Value                | Description                                                                   |
|----------------------|-------------------------------------------------------------------------------|
| `GRANTED`            | Occurs when the user grants permission.                                       |
| `DENIED`             | Occurs when the user denies permission.                                       |
| `PERMANENTLY_DENIED` | Occurs when the user denies the permission once and chooses not to ask again. |

### :chicken: Activity

A model representing the user's activity.

| Property     | Description                            |
|--------------|----------------------------------------|
| `type`       | The type of activity recognized.       |
| `confidence` | The confidence of activity recognized. |

### :chicken: ActivityType

Defines the type of activity.

| Value        | Description                                                                |
|--------------|----------------------------------------------------------------------------|
| `IN_VEHICLE` | The device is in a vehicle, such as a car.                                 |
| `ON_BICYCLE` | The device is on a bicycle.                                                |
| `RUNNING`    | The device is on a user who is running. This is a sub-activity of ON_FOOT. |
| `STILL`      | The device is still (not moving).                                          |
| `WALKING`    | The device is on a user who is walking. This is a sub-activity of ON_FOOT. |
| `UNKNOWN`    | Unable to detect the current activity.                                     |

### :chicken: ActivityConfidence

Defines the confidence of activity.

| Value    | Description            |
|----------|------------------------|
| `HIGH`   | High accuracy: 80~100  |
| `MEDIUM` | Medium accuracy: 50~80 |
| `LOW`    | Low accuracy: 0~50     |
