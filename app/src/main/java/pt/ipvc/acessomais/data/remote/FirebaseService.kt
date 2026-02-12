package pt.ipvc.acessomais.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pt.ipvc.acessomais.data.model.Local

class FirebaseService {
    private val db = Firebase.firestore
    private val collection = db.collection("locais")

    suspend fun uploadLocal(local: Local): Boolean {
        return try {
            // O uso de .await() requer a lib kotlinx-coroutines-play-services
            collection.document(local.id.toString()).set(local).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}