import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_ble_serial_method_channel.dart';

abstract class FlutterBleSerialPlatform extends PlatformInterface {
  /// Constructs a FlutterBleSerialPlatform.
  FlutterBleSerialPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterBleSerialPlatform _instance = MethodChannelFlutterBleSerial();

  /// The default instance of [FlutterBleSerialPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterBleSerial].
  static FlutterBleSerialPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterBleSerialPlatform] when
  /// they register themselves.
  static set instance(FlutterBleSerialPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> startPlugin() {
    throw UnimplementedError('startPlugin() has not been implemented.');
  }

  Future<void> stopPlugin() {
    throw UnimplementedError('stopPlugin() has not been implemented.');
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('getPlatformVersion() has not been implemented.');
  }

  Future<bool?> isBluetoothEnabled() {
    throw UnimplementedError('isBluetoothEnabled() has not been implemented.');
  }
}
