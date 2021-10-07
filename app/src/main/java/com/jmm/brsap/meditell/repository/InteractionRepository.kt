package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.jmm.brsap.meditell.model.Doctor
import com.jmm.brsap.meditell.model.InteractionModel
import com.jmm.brsap.meditell.model.Pharmacy
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
            val response = db.collection(FirebaseDB.INTERACTIONS).document("$newInteractionId").set(interaction).await()
            emit(true)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getInteractionOfTheDay(date:String,areaId:Int,userId:String): Flow<List<InteractionModel>> {
        return flow {
            val interactionsResponse = db.collection(FirebaseDB.INTERACTIONS)
                .whereEqualTo("interactedOn",date)
                .whereEqualTo("areaId",areaId)
                .whereEqualTo("interactedBy",userId)
                .get().await()
            Timber.d("Interaction response : ${interactionsResponse.documents}")
            val interactions = interactionsResponse.toObjects(InteractionModel::class.java)
            Timber.d("Converted into interactions : $interactions")
            for (interaction in interactions){
                if (interaction.interactionWasWith=="doctor"){
                    val doctorResponse = db.collection(FirebaseDB.DOCTORS).document(interaction.interactedWith.toString()).get().await()
                    Timber.d("doctor response : ${doctorResponse.data}")
                    val doctor = doctorResponse.toObject<Doctor>()
                    Timber.d("Converted into doctor : $doctor")
                    interaction.interactedWithModel = doctor

                }else{
                    val pharmacyResponse = db.collection(FirebaseDB.PHARMACY).document(interaction.interactedWith.toString()).get().await()
                    Timber.d("doctor response : ${pharmacyResponse.data}")
                    val pharmacy = pharmacyResponse.toObject<Pharmacy>()
                    Timber.d("Converted into pharmacy : $pharmacy")
                    interaction.interactedWithModel = pharmacy
                }
            }
            emit(interactions)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getInteractionDetail(interactionId: Int): Flow<InteractionModel> {
        return flow {
            val interactionResponse = db.collection(FirebaseDB.INTERACTIONS).document(interactionId.toString()).get().await()
            Timber.d("interactionResponse : ${interactionResponse.data}")
            val interaction = interactionResponse.toObject(InteractionModel::class.java)!!
            Timber.d("Converted into interaction : $interaction")

            if (interaction.interactionWasWith=="doctor"){
                val doctorResponse = db.collection(FirebaseDB.DOCTORS).document(interaction.interactedWith.toString()).get().await()
                Timber.d("doctor response : ${doctorResponse.data}")
                val doctor = doctorResponse.toObject<Doctor>()
                Timber.d("Converted into doctor : $doctor")
                interaction.interactedWithModel = doctor

            }else{
                val pharmacyResponse = db.collection(FirebaseDB.PHARMACY).document(interaction.interactedWith.toString()).get().await()
                Timber.d("doctor response : ${pharmacyResponse.data}")
                val pharmacy = pharmacyResponse.toObject<Pharmacy>()
                Timber.d("Converted into pharmacy : $pharmacy")
                interaction.interactedWithModel = pharmacy
            }

            emit(interaction)
        }.flowOn(Dispatchers.IO)
    }
}