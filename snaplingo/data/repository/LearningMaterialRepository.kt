package com.example.snaplingo.data.repository

import com.example.snaplingo.data.model.LearningMaterial
import com.google.firebase.firestore.FirebaseFirestore

class LearningMaterialRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveLearningMaterial(
        userId: String,
        text: String,
        language: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val materialId = db
            .collection("users")
            .document(userId)
            .collection("learningMaterials")
            .document()
            .id

        val material = LearningMaterial(
            id = materialId,
            text = text,
            language = language,
            createdAt = System.currentTimeMillis()
        )

        db.collection("users")
            .document(userId)
            .collection("learningMaterials")
            .document(materialId)
            .set(material)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
    fun getLearningMaterials(
        userId: String,
        onSuccess: (List<LearningMaterial>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        db.collection("users")
            .document(userId)
            .collection("learningMaterials")
            .get()
            .addOnSuccessListener { result ->

                val materials = result.documents.mapNotNull { document ->

                    document.toObject(LearningMaterial::class.java)
                }

                onSuccess(materials)
            }
            .addOnFailureListener { exception ->

                onError(exception)
            }
    }
    fun deleteLearningMaterial(
        userId: String,
        materialId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        db.collection("users")
            .document(userId)
            .collection("learningMaterials")
            .document(materialId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}