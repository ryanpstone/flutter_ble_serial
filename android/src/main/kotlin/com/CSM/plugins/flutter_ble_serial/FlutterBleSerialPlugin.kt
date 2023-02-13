package com.CSM.plugins.flutter_ble_serial

import android.app.Activity
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

// TODO: Only import needed parts of android.bluetooth
import android.bluetooth.*

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult


class BluetoothPlugin() : ComponentActivity(){

  private lateinit var bluetoothManager: BluetoothManager
  private lateinit var bluetoothAdapter: BluetoothAdapter

  var isBluetoothEnabled = false



  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    bluetoothManager = getSystemService(BluetoothManager::class.java)
    bluetoothAdapter = bluetoothManager.getAdapter()
    super.onCreate(savedInstanceState, persistentState)
  }




  fun enableBluetooth() {
    // Device doesn't support Bluetooth
    if (bluetoothAdapter == null) {
      // TODO: Implement exceptions
    }
    else if (bluetoothAdapter?.isEnabled == null) {
      // TODO: Implement exceptions
    }
    // Device supports Bluetooth but it isn't enabled
    else if (bluetoothAdapter?.isEnabled == false) {
      // Create intent
      val enableBluetoothIntent = ActivityResultContracts.StartActivityForResult()
      enableBluetoothIntent.createIntent(applicationContext, Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))

      // Set callback for intent
      val enableBluetoothCallback = ActivityResultCallback<ActivityResult> {
        val activityResultCode = Activity.RESULT_FIRST_USER
        val afterActivityResultCode = enableBluetoothIntent.parseResult(activityResultCode, Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)).resultCode
        if (afterActivityResultCode == Activity.RESULT_OK) {
          isBluetoothEnabled = true
        }
      }

      // Register intent
      val enableBluetoothResult = registerForActivityResult(enableBluetoothIntent, enableBluetoothCallback)

      // Launch intent
      enableBluetoothResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }
    // Bluetooth is already enabled
    else {
      isBluetoothEnabled = true
    }
  }

  init {

  }

}

/** FlutterBleSerialPlugin */
class FlutterBleSerialPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var bluetoothPlugin : BluetoothPlugin


  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_ble_serial")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }
    else if (call.method == "isBluetoothEnabled") {
      bluetoothPlugin = BluetoothPlugin()
      val intent = Intent(bluetoothPlugin, BluetoothPlugin::class.java)
      bluetoothPlugin.startActivity(intent)
      bluetoothPlugin.enableBluetooth()

      result.success(bluetoothPlugin.isBluetoothEnabled)
    }
    else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
