import 'package:flutter/material.dart';

void main() => runApp(ExampleApp());

class ExampleApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _ExampleAppState();
}

class _ExampleAppState extends State<ExampleApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Flutter Activity Recognition'),
          centerTitle: true,
        ),
        body: SizedBox.shrink(),
      ),
    );
  }
}
