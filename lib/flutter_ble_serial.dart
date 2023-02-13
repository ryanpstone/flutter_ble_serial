import 'dart:io' show Platform;
import 'dart:async';

import 'flutter_ble_serial_platform_interface.dart';

// Subclass of BluetoothAdapter. Interfaces with Android system service.
class BluetoothManager {

  // Constructor
  BluetoothManager() {
    if (Platform.isAndroid) {

    }
    else {
      print("Operating system not implemented.");
    }
  }

}

// Class to interface with hardware Bluetooth adapter. Platform specific implementation.
class BluetoothAdapter {

  BluetoothManager? _bluetoothManager;

  // Constructor
  BluetoothAdapter() {
    if (Platform.isAndroid) {
      print("Platform is Android");
    }
    else {
      print("Operating system not implemented.");
    }
  }

  // Getter for _bluetoothManager
  BluetoothManager? getBluetoothManager() {
    return _bluetoothManager;
  }
}

// Check for permissions must be done within adapter?
class FlutterBleSerial {
  // static final BluetoothAdapter bluetoothAdapter = BluetoothAdapter();

  FlutterBleSerial() {
    print("Plugin loaded.");
  }

  Future<String?> getPlatformVersion() {
    return FlutterBleSerialPlatform.instance.getPlatformVersion();
  }

  Future<bool?> isBluetoothEnabled() {
    return FlutterBleSerialPlatform.instance.isBluetoothEnabled();
  }

}
