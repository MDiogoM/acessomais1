package pt.ipvc.acessomais.data.model

import java.util.UUID

data class Local(
    val id: String = UUID.randomUUID().toString(),
    val userEmail: String, // Identificador do utilizador
    val nome: String,
    val tipo: String,
    val latitude: Double,
    val longitude: Double,
    val temRampa: Boolean,
    val temElevador: Boolean,
    val larguraEntradaCm: Int?,
    val temWcAdaptado: Boolean,
    val comentario: String?,
    val rating: Int,
    val updatedAt: Long = System.currentTimeMillis(),
    val pendingSync: Boolean = true
)