package pt.ipvc.acessomais.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LocalEntity::class, UserEntity::class],
    version = 2, // Versão incrementada devido à alteração da tabela
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localDao(): LocalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "acessomais_db"
                )
                    .fallbackToDestructiveMigration() // Evita o crash ao recriar a BD
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}