package com.faustino.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faustino.murocampus.data.PostRepositoryRemote
import com.faustino.murocampus.data.remote.RetrofitClient
import com.faustino.murocampus.ui.PantallaLista
import com.faustino.murocampus.ui.theme.MuroCampusTheme
import androidx.compose.ui.text.font.FontWeight
import com.faustino.murocampus.viewmodel.PostsViewModel
import com.faustino.murocampus.viewmodel.PostsViewModelFactory
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.faustino.murocampus.ui.PantallaDetalle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory {
                        PostsViewModel(PostRepositoryRemote(RetrofitClient.api))
                    }
                )
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                BackHandler(enabled = idSeleccionado != null) { idSeleccionado = null }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus", fontWeight = FontWeight.Bold) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { padding ->
                    val id = idSeleccionado
                    if (id == null) {
                        PantallaLista(
                            vm = vm,
                            onPostClick = { vm.abrir(it); idSeleccionado = it },
                            modifier = Modifier.padding(padding)
                        )
                    } else {
                        PantallaDetalle(vm, id, Modifier.padding(padding))
                    }
                }
            }
        }
    }
}