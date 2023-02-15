package com.CSM.plugins.flutter_ble_serial

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
//import android.app.Activity
import android.app.Service
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

// TODO: Only import needed parts of android.bluetooth
import android.bluetooth.*
import android.content.Context


//import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.os.PersistableBundle

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

import androidx.work.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.newSingleThreadContext
import kotlin.contracts.contract


@SuppressLint("RestrictedApi")
class BluetoothPlugin(appContext: Context) : ComponentActivity() {

  private val workManager = WorkManager.getInstance(appContext)

  private var bluetoothManager: BluetoothManager? = getSystemService(appContext, BluetoothManager::class.java)
  private var bluetoothAdapter: BluetoothAdapter = bluetoothManager!!.adapter

  var isBluetoothEnabled = false

  inner class enableBluetoothWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
      if (bluetoothAdapter == null) {
        // TODO: Implement exceptions
      }
      else if (!bluetoothAdapter.isEnabled) {
        // Create intent
        val enableBluetoothIntent = ActivityResultContracts.StartActivityForResult()
        enableBluetoothIntent.createIntent(this.applicationContext, Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))

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
      return Result.success()
    }
  }

  val enableBluetoothWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<enableBluetoothWorker>().build()

  fun enableBluetooth() {
    workManager.enqueue(this.enableBluetoothWorkRequest)
  }


  //fun enableBluetooth() {
  //  // Device doesn't support Bluetooth
  //  if (bluetoothAdapter == null) {
  //    // TODO: Implement exceptions
  //  }
  //  //else if (bluetoothAdapter?.isEnabled == null) {
  //  //  // TODO: Implement exceptions
  //  //}
  //  // Device supports Bluetooth but it isn't enabled
  //  else if (bluetoothAdapter?.isEnabled == false) {
  //    // Create intent
  //    val enableBluetoothIntent = ActivityResultContracts.StartActivityForResult()
  //    enableBluetoothIntent.createIntent(this.context, Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
//
  //    // Set callback for intent
  //    val enableBluetoothCallback = ActivityResultCallback<ActivityResult> {
  //      val activityResultCode = Activity.RESULT_FIRST_USER
  //      val afterActivityResultCode = enableBluetoothIntent.parseResult(activityResultCode, Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)).resultCode
  //      if (afterActivityResultCode == Activity.RESULT_OK) {
  //        isBluetoothEnabled = true
  //      }
  //    }
//
  //    // Register intent
  //    val enableBluetoothResult = this.registerForActivityResult(enableBluetoothIntent, enableBluetoothCallback)
//
  //    // Launch intent
  //    enableBluetoothResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
  //  }
  //  // Bluetooth is already enabled
  //  else {
  //    isBluetoothEnabled = true
  //  }
  //}
}



/** FlutterBleSerialPlugin */
class FlutterBleSerialPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity

  private lateinit var bluetoothPlugin : BluetoothPlugin

  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    bluetoothPlugin = BluetoothPlugin(flutterPluginBinding.getApplicationContext())

    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_ble_serial")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }

    else if (call.method == "isBluetoothEnabled") {
      if (!bluetoothPlugin.isBluetoothEnabled) {
        bluetoothPlugin.enableBluetooth()
      }
      //bluetoothPlugin = BluetoothPlugin()
      //val intent = Intent(bluetoothPlugin, BluetoothPlugin::class.java)
      //bluetoothPlugin.startActivity(intent)
      //bluetoothPlugin.enableBluetooth()
      //
      //result.success(bluetoothPlugin.isBluetoothEnabled)
    }
    else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
