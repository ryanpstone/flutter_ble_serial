import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_ble_serial/flutter_ble_serial.dart';
import 'package:flutter_ble_serial/flutter_ble_serial_platform_interface.dart';
import 'package:flutter_ble_serial/flutter_ble_serial_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterBleSerialPlatform
    with MockPlatformInterfaceMixin
    implements FlutterBleSerialPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<bool?> isBluetoothEnabled() => Future.value(false);
}

void main() {
  final FlutterBleSerialPlatform initialPlatform = FlutterBleSerialPlatform.instance;

  test('$MethodChannelFlutterBleSerial is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterBleSerial>());
  });

  test('getPlatformVersion', () async {
    FlutterBleSerial flutterBleSerialPlugin = FlutterBleSerial();
    MockFlutterBleSerialPlatform fakePlatform = MockFlutterBleSerialPlatform();
    FlutterBleSerialPlatform.instance = fakePlatform;

    expect(await flutterBleSerialPlugin.getPlatformVersion(), '42');
  });
}
