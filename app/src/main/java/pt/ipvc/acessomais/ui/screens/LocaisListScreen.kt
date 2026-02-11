package pt.ipvc.acessomais.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipvc.acessomais.data.model.Local
import pt.ipvc.acessomais.viewmodel.LocalViewModel

@Composable
fun LocaisListScreen(
    viewModel: LocalViewModel,
    onLocalSelected: (Local) -> Unit
) {
    val listaLocais by viewModel.locais.collectAsState()

    if (listaLocais.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhum local guardado nesta conta.")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listaLocais) { local ->
                LocalItem(
                    local = local,
                    onClick = { onLocalSelected(local) },
                    onDelete = { viewModel.deleteLocal(local) }
                )
            }
        }
    }
}

@Composable
fun LocalItem(local: Local, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = local.nome, style = MaterialTheme.typography.titleLarge)

                // ADICIONADO: Email do criador por baixo do nome
                Text(
                    text = "Criado por: ${local.userEmail}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = local.tipo, style = MaterialTheme.typography.bodyMedium)
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Local",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}