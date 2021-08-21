# client_app

A new Flutter project.

## Getting Started

### util/credentials.dart file

You will need to create you own credentials.dart file in the util directory. The purpose of this file is to contain the security tokens that allows the application to connect to your Astra database via a REST API.

```dart
class Credentials {
  static String ASTRA_DB_ID = "<your DB ID?";
  static String ASTRA_DB_REGION= "<your DB region>";
  static String ASTRA_DB_KEYSPACE= "<your DB keyspace>";
  static String ASTRA_DB_TOKEN = "<your DB token>";
}
```

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://flutter.dev/docs/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://flutter.dev/docs/cookbook)

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.
