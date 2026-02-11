package pt.ipvc.acessomais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import pt.ipvc.acessomais.ui.screens.*
import pt.ipvc.acessomais.ui.theme.AcessoTheme
import pt.ipvc.acessomais.viewmodel.LocalViewModel

class MainActivity : ComponentActivity() {
    private val vm: LocalViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var telaAtual by remember { mutableStateOf("auth") }

            AcessoTheme {
                if (telaAtual == "auth") {
                    AuthScreen(vm) { telaAtual = "lista" }
                } else {
                    Scaffold(
                        topBar = {
                            if (telaAtual != "adicionar") {
                                CenterAlignedTopAppBar(
                                    title = { Text("Acesso+", fontWeight = FontWeight.Bold) },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            vm.logout()
                                            telaAtual = "auth"
                                        }) {
                                            Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Sair")
                                        }
                                    },
                                    actions = {
                                        IconButton(onClick = {
                                            telaAtual = if (telaAtual == "mapa") "lista" else "mapa"
                                        }) {
                                            Icon(
                                                imageVector = if (telaAtual == "mapa") Icons.Default.FormatListBulleted else Icons.Default.Map,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )
                            }
                        },
                        floatingActionButton = {
                            if (telaAtual == "lista" || telaAtual == "mapa") {
                                FloatingActionButton(
                                    onClick = { telaAtual = "adicionar" },
                                    containerColor = MaterialTheme.colorScheme.primary
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Adicionar Local")
                                }
                            }
                        }
                    ) { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            when (telaAtual) {
                                "lista" -> LocaisListScreen(vm, onLocalSelected = { local ->
                                    vm.focarLocal(local)
                                    telaAtual = "mapa"
                                })
                                "mapa" -> LocaisMapScreen(vm)
                                "adicionar" -> AddLocalScreen(
                                    viewModel = vm,
                                    onNavigateBack = { telaAtual = "lista" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}