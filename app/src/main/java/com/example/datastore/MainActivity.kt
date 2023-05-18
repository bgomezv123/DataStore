package com.example.datastore

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.Lifecycling
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData


class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DataStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val context = LocalContext.current
    val preferenceManager = remember {
        PreferenceManager(context)
    }

    val name by preferenceManager.nameFlow.collectAsState(initial = "")


    Column {
        if(name != "") {
            var text by remember { mutableStateOf(name) }
            Log.d("name123", name.toString())

            Text(text = "Data Store")
            text?.let {
                TextField(
                    value = text.toString(),
                    onValueChange = {

                        text = it
                        GlobalScope.launch(Dispatchers.Main) {
                            preferenceManager.saveName(text.toString())
                        }
                    },
                )
            }
        }

        val mContext = LocalContext.current

        Button(
            onClick = {
                mToast(mContext,name.toString())
            }
        ) {
            Text("Obtener nombre")
        }

    }
}
private fun mToast(context: Context, name: String){
    Toast.makeText(context, "Hola, mucho gusto "+name+"!!!", Toast.LENGTH_LONG).show()
}

