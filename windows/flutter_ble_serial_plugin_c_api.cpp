#include "include/flutter_ble_serial/flutter_ble_serial_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "flutter_ble_serial_plugin.h"

void FlutterBleSerialPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  flutter_ble_serial::FlutterBleSerialPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
