import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_ble_serial/flutter_ble_serial.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool _isBluetoothEnabled = false;
  final _flutterBleSerialPlugin = FlutterBleSerial();

  @override
  void initState() {
    super.initState();
    initPlatformState();
    initBluetoothEnabledState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _flutterBleSerialPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> initBluetoothEnabledState() async {
    bool isBluetoothEnabled;

    try {
      isBluetoothEnabled = await _flutterBleSerialPlugin.isBluetoothEnabled() ?? false;
    } on PlatformException {
      isBluetoothEnabled = false;
    }

    if (!mounted) return;
    setState(() {
      _isBluetoothEnabled = isBluetoothEnabled;
    });
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('Running on: $_platformVersion\n'),
              Text('isBluetoothEnabled: $_isBluetoothEnabled\n'),
            ],
          )
        ),
      ),
    );
  }
}
