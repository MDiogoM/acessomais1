package pt.ipvc.acessomais.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import pt.ipvc.acessomais.viewmodel.LocalViewModel

@Composable
fun LocaisMapScreen(viewModel: LocalViewModel) {
    val locais by viewModel.locais.collectAsState()
    val localFocado by viewModel.localRecemAdicionado.collectAsState()

    // Configuração inicial da câmara (ex: Viana do Castelo)
    val viana = LatLng(41.6932, -8.8328)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(viana, 13f)
    }

    // Se um local for selecionado na lista, o mapa move-se para lá
    LaunchedEffect(localFocado) {
        localFocado?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 15f
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        locais.forEach { local ->
            Marker(
                state = MarkerState(position = LatLng(local.latitude, local.longitude)),
                title = local.nome,
                snippet = local.comentario
            )
        }
    }
}