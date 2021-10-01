package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.Doctor
import com.jmm.brsap.meditell.util.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DoctorRepository @Inject constructor(private val db:FirebaseFirestore) {

    suspend fun addNewDoctor(doctor: Doctor): Flow<Boolean> {
        return flow {
            val lastAddedDoctor = db.collection(FirebaseDB.AREAS).orderBy("doctorId", Query.Direction.DESCENDING).limit(1).get().await()
            Timber.d("Last added doctor : ${lastAddedDoctor.documents}")
            val lastDoctor = lastAddedDoctor.toObjects(Area::class.java)
            Timber.d("Converted into doctors : $lastDoctor")
            val newDoctorId = if (lastDoctor.isEmpty()) 10000 else lastDoctor[0].areaId!!+1

            Timber.d("New Doctor ID :$newDoctorId")
            doctor.doctorId = newDoctorId
            Timber.d("Doctor to be added : $doctor")
            val response = db.collection(FirebaseDB.AREAS).document("$newDoctorId").set(doctor)
            if (response.isSuccessful) emit(true)
            else emit(false)
        }.flowOn(Dispatchers.IO)
    }
}