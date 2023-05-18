package com.example.datastore

import android.content.Context
import android.os.Bundle
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME] = name
        }
    }

    val nameFlow: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.NAME]
        }

    private object PreferencesKeys {
        val NAME = stringPreferencesKey("name")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val preferencesManager = remember {
        PreferencesManager(context)
    }

    val name by preferencesManager.nameFlow.collectAsState(initial = "")

    Column {
        name?.let {
            TextField(
                value = it,
                onValueChange = { newValue ->
                    preferencesManager.saveName(newValue)
                },
                label = { Text("Nombre") }
            )
        }
        Button(
            onClick = {
                val storedName = name

            }
        ) {
            Text("Obtener nombre")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DataStoreTheme {
        Greeting("Android")
    }
}