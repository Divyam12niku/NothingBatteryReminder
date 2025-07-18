package com.divyam.batteryreminder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AudioViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val started = remember { mutableStateOf(false) }

            Column(Modifier.padding(24.dp)) {
                Text("Nothing Battery Reminder", style = MaterialTheme.typography.h6)

                Spacer(Modifier.height(24.dp))

                BatteryRangePicker("0–25%", viewModel::setLowUri)
                BatteryRangePicker("26–75%", viewModel::setMidUri)
                BatteryRangePicker("76–99%", viewModel::setHighUri)

                Spacer(Modifier.height(24.dp))

                Button(onClick = {
                    if (!started.value) {
                        startService(Intent(this@MainActivity, MediaService::class.java))
                    } else {
                        stopService(Intent(this@MainActivity, MediaService::class.java))
                    }
                    started.value = !started.value
                }) {
                    Text(if (started.value) "Stop" else "Start")
                }
            }
        }
    }
}

@Composable
fun BatteryRangePicker(label: String, onSelect: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) onSelect(it)
    }

    Button(onClick = {
        launcher.launch(arrayOf("audio/*"))
    }) {
        Text("Select sound for $label")
    }
}

class AudioViewModel : ViewModel() {
    var lowUri: Uri? by mutableStateOf(null)
    var midUri: Uri? by mutableStateOf(null)
    var highUri: Uri? by mutableStateOf(null)

    fun setLowUri(uri: Uri) { lowUri = uri }
    fun setMidUri(uri: Uri) { midUri = uri }
    fun setHighUri(uri: Uri) { highUri = uri }
}
