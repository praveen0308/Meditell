package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.Pharmacy
import com.jmm.brsap.meditell.util.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PharmacyRepository  @Inject constructor(private val db: FirebaseFirestore) {

    suspend fun addNewPharmacy(pharmacy: Pharmacy): Flow<Boolean> {
        return flow {
            val lastAddedPharmacy = db.collection(FirebaseDB.AREAS).orderBy("pharmacyId", Query.Direction.DESCENDING).limit(1).get().await()
            Timber.d("Last added pharmacy : ${lastAddedPharmacy.documents}")
            val lastPharmacy = lastAddedPharmacy.toObjects(Area::class.java)
            Timber.d("Converted into pharmacies : $lastPharmacy")
            val newPharmacyId = if (lastPharmacy.isEmpty()) 10000 else lastPharmacy[0].areaId!!+1

            Timber.d("New Pharmacy ID :$newPharmacyId")
            pharmacy.pharmacyId = newPharmacyId
            Timber.d("Pharmacy to be added : $pharmacy")
            val response = db.collection(FirebaseDB.AREAS).document("$newPharmacyId").set(pharmacy)
            if (response.isSuccessful) emit(true)
            else emit(false)
        }.flowOn(Dispatchers.IO)
    }
}