import 'dart:io';
import 'dart:async';


import 'flutter_ble_serial_platform_interface.dart';

// Interfaces with Android system service.
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
  BluetoothAdapter? _bluetoothAdapter;

  FlutterBleSerial() {
    _bluetoothAdapter = BluetoothAdapter();
  }

  Future<void> startPlugin() {
    return FlutterBleSerialPlatform.instance.startPlugin();
  }

  Future<void> stopPlugin() {
    return FlutterBleSerialPlatform.instance.stopPlugin();
  }

  Future<String?> getPlatformVersion() {
    return FlutterBleSerialPlatform.instance.getPlatformVersion();
  }

  Future<bool?> isBluetoothEnabled() {
    return FlutterBleSerialPlatform.instance.isBluetoothEnabled();
  }

}
