package pt.ipvc.acessomais.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {
    @Query("SELECT * FROM locais")
    fun getAllLocais(): Flow<List<LocalEntity>>

    @Query("SELECT * FROM locais WHERE userEmail = :email")
    fun getLocaisByUser(email: String): Flow<List<LocalEntity>>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocal(local: LocalEntity)

    @Delete
    suspend fun deleteLocal(local: LocalEntity)

    // FALTAVA ISTO:
    @Query("SELECT * FROM locais WHERE pendingSync = 1")
    suspend fun getPendingSyncLocals(): List<LocalEntity>

    @Query("UPDATE locais SET pendingSync = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: Int, status: Boolean)
}