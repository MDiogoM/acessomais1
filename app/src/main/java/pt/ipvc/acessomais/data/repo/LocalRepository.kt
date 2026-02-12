package pt.ipvc.acessomais.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.ipvc.acessomais.data.local.LocalDao
import pt.ipvc.acessomais.data.model.Local

class LocalRepository(private val localDao: LocalDao) {
    // Corrigido para toDomain() conforme definido em LocalMappers.kt
    fun getLocais(email: String): Flow<List<Local>> =
        localDao.getLocaisByUser(email).map { list -> list.map { it.toDomain() } }

    fun getAllLocais(): Flow<List<Local>> =
        localDao.getAllLocais().map { list -> list.map { it.toDomain() } }

    suspend fun saveLocal(local: Local) {
        localDao.insert(local.toEntity())
    }

    suspend fun deleteLocal(local: Local) {
        localDao.delete(local.toEntity())
    }
}