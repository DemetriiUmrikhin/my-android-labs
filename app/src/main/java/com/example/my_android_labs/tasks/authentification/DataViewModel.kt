package com.example.my_android_labs.tasks.authentification

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DataViewModel @Inject constructor(
    private val realtimeDb: DatabaseReference,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private var _items = mutableStateListOf<DataItem>()
    val items: List<DataItem> get() = _items

    private  var dbType: String? = null
    private var realtimeListener: ValueEventListener? = null
    private var firestoreListener: ListenerRegistration? = null

    fun init(dbType: String) {
        this.dbType = dbType
        when (dbType) {
            "realtime" -> setupRealtimeListener()
            "firestore" -> setupFirestoreListener()
        }
    }

    private fun setupRealtimeListener() {
        realtimeListener = realtimeDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newItems = mutableListOf<DataItem>()
                snapshot.children.forEach { child ->
                    val data = child.getValue(DataItem::class.java) ?: DataItem()
                    newItems.add(
                        DataItem(
                            id = child.key, // Используем ключ узла как ID
                            title = data.title,
                            content = data.content
                        )
                    )
                }
                _items.clear()
                _items.addAll(newItems)
                Log.d("RealtimeDB", "Items: ${newItems.joinToString("\n")}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RealtimeDB", "Error: ${error.message}")
            }
        })
    }

    private fun setupFirestoreListener() {
        firestoreListener = firestore.collection("items")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listen failed", error)
                    return@addSnapshotListener
                }

                val newItems = mutableListOf<DataItem>()
                snapshot?.documents?.forEach { doc ->
                    val data = doc.toObject(DataItem::class.java) ?: DataItem()
                    newItems.add(
                        DataItem(
                            id = doc.id, // Используем ID документа
                            title = data.title,
                            content = data.content
                        )
                    )
                }
                _items.clear()
                _items.addAll(newItems)
                Log.d("Firestore", "Items updated: ${newItems.size}")
            }
    }

    fun saveItem(dbType: String, item: DataItem) {
        when (dbType) {
            "realtime" -> {
                // Создаем чистый объект без ID
                val data = mapOf(
                    "title" to item.title,
                    "content" to item.content
                )

                if (item.id == null) {
                    val newRef = realtimeDb.push()
                    newRef.setValue(data)
                    // Обновляем локальный ID
                    item.id = newRef.key
                } else {
                    realtimeDb.child(item.id!!).setValue(data)
                }
            }
            "firestore" -> {
                // Создаем чистый объект без ID
                val data = hashMapOf(
                    "title" to item.title,
                    "content" to item.content
                )

                if (item.id == null) {
                    firestore.collection("items").add(data)
                        .addOnSuccessListener { docRef ->
                            item.id = docRef.id
                        }
                } else {
                    firestore.collection("items").document(item.id!!).set(data)
                }
            }
        }
    }


    fun getItemById(itemId: String, callback: (DataItem?) -> Unit) {
        val currentDbType = dbType  // Локальная копия для безопасности
        if (currentDbType == null) {
            Log.e("DataViewModel", "dbType not initialized!")
            callback(null)
            return
        }

        when (currentDbType) {  // Используем локальную копию
            "realtime" -> {
                realtimeDb.child(itemId).get().addOnSuccessListener { snapshot ->
                    val item = snapshot.getValue(DataItem::class.java)
                    item?.id = snapshot.key
                    callback(item)
                }
            }
            "firestore" -> {
                firestore.collection("items").document(itemId).get()
                    .addOnSuccessListener { document ->
                        val item = document.toObject(DataItem::class.java)
                        item?.id = document.id
                        callback(item)
                    }
            }
        }
    }

    fun deleteItem(dbType: String, itemId: String) {
        Log.d("DataViewModel", "Deleting item $itemId from $dbType")
        when (dbType) {
            "realtime" -> realtimeDb.child(itemId).removeValue()
            "firestore" -> firestore.collection("items").document(itemId).delete()
        }
    }


    override fun onCleared() {
        realtimeListener?.let { realtimeDb.removeEventListener(it) }
        firestoreListener?.remove()
    }
}