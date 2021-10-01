package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmm.brsap.meditell.model.InteractionModel
import com.jmm.brsap.meditell.util.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class InteractionRepository @Inject constructor(private val db: FirebaseFirestore) {

    suspend fun addNewInteraction(interaction: InteractionModel): Flow<Boolean> {
        return flow {
            val lastAddedInteraction = db.collection(FirebaseDB.INTERACTIONS).orderBy("interactionId", Query.Direction.DESCENDING).limit(1).get().await()
            Timber.d("Last added interaction : ${lastAddedInteraction.documents}")
            val lastInteraction = lastAddedInteraction.toObjects(InteractionModel::class.java)
            Timber.d("Converted into interactions : $lastInteraction")
            val newInteractionId = if (lastInteraction.isEmpty()) 1000000 else lastInteraction[0].interactionId!!+1

            Timber.d("New Interaction ID :$newInteractionId")
            interaction.interactionId = newInteractionId
            Timber.d("Interaction to be added : $interaction")
            val response = db.collection(FirebaseDB.INTERACTIONS).document("$newInteractionId").set(interaction)
            if (response.isSuccessful) emit(true)
            else emit(false)
        }.flowOn(Dispatchers.IO)
    }
}