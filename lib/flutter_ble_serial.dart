
import 'flutter_ble_serial_platform_interface.dart';

class FlutterBleSerial {
  Future<String?> getPlatformVersion() {
    return FlutterBleSerialPlatform.instance.getPlatformVersion();
  }



}
