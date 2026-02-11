package pt.ipvc.acessomais.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locais")
data class LocalEntity(
    @PrimaryKey
    val id: String,
    val userEmail: String, // Campo para persistência por conta
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
    val updatedAt: Long,
    val pendingSync: Boolean
)