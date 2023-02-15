import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_ble_serial_platform_interface.dart';

/// An implementation of [FlutterBleSerialPlatform] that uses method channels.
class MethodChannelFlutterBleSerial extends FlutterBleSerialPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_ble_serial');

  @override
  Future<void> startPlugin() async {
    final version = await methodChannel.invokeMethod<void>('startPlugin');
    return version;
  }

  @override
  Future<void> stopPlugin() async {
    final version = await methodChannel.invokeMethod<void>('stopPlugin');
    return version;
  }

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<bool?> isBluetoothEnabled() async {
    final isBluetoothEnabled = await methodChannel.invokeMethod<bool>('isBluetoothEnabled');
    return isBluetoothEnabled;
  }
}
