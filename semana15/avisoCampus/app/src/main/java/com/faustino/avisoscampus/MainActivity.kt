package com.faustino.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.faustino.avisoscampus.ui.PantallaAvisos
import com.faustino.avisoscampus.ui.theme.AvisosCampusTheme
import com.faustino.avisoscampus.viewmodel.AvisosViewModel
import com.faustino.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    private val vm: AvisosViewModel by viewModels { AvisosViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                PantallaAvisos(vm)
            }
        }
    }
}