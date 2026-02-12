package pt.ipvc.acessomais.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pt.ipvc.acessomais.data.model.Local

class FirebaseService {
    private val db = Firebase.firestore
    private val collection = db.collection("locais")

    suspend fun getLocais(): List<Local> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Local::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveLocal(local: Local) {
        collection.document(local.id).set(local).await()
    }
}