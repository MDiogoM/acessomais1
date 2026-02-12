package pt.ipvc.acessomais.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pt.ipvc.acessomais.data.model.Local
import pt.ipvc.acessomais.viewmodel.LocalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocalScreen(
    viewModel: LocalViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var nome by remember { mutableStateOf("") }
    var latInput by remember { mutableStateOf("") }
    var lonInput by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }

    val userEmail by viewModel.userEmailLogado.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Novo Local") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Local") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = latInput,
                    onValueChange = { latInput = it },
                    label = { Text("Latitude (ex: 41.69)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = lonInput,
                    onValueChange = { lonInput = it },
                    label = { Text("Longitude (ex: -8.83)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentários") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val lat = latInput.replace(',', '.').toDoubleOrNull()
                    val lon = lonInput.replace(',', '.').toDoubleOrNull()

                    if (lat == null || lon == null) {
                        Toast.makeText(context, "Insira coordenadas válidas!", Toast.LENGTH_SHORT).show()
                    } else {
                        userEmail?.let { email ->
                            val novoLocal = Local(
                                userEmail = email,
                                nome = nome,
                                latitude = lat,
                                longitude = lon,
                                comentario = comentario,
                                tipo = "Geral",
                                temRampa = false,
                                temElevador = false,
                                larguraEntradaCm = null,
                                temWcAdaptado = false,
                                rating = 0
                            )
                            viewModel.saveLocal(novoLocal)
                            onNavigateBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nome.isNotBlank() && latInput.isNotBlank() && lonInput.isNotBlank()
            ) {
                Text("Criar Local")
            }
        }
    }
}