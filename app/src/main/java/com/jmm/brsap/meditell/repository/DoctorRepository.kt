package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.Degree
import com.jmm.brsap.meditell.model.Doctor
import com.jmm.brsap.meditell.model.Pharmacy
import com.jmm.brsap.meditell.util.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DoctorRepository @Inject constructor(private val db: FirebaseFirestore) {

    suspend fun getDegrees(): Flow<List<Degree>> {
        return flow {
            val response = db.collection(FirebaseDB.DEGREE).get().await()
            val degrees = response.toObjects(Degree::class.java)
            emit(degrees)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun addNewDoctor(doctor: Doctor): Flow<Boolean> {
        return flow {
            val lastAddedDoctor =
                db.collection(FirebaseDB.DOCTORS).orderBy("doctorId", Query.Direction.DESCENDING)
                    .limit(1).get().await()
            Timber.d("Last added doctor : ${lastAddedDoctor.documents}")
            val lastDoctor = lastAddedDoctor.toObjects(Doctor::class.java)
            Timber.d("Converted into doctors : $lastDoctor")
            val newDoctorId = if (lastDoctor.isEmpty()) 10000 else lastDoctor[0].areaId!! + 1

            Timber.d("New Doctor ID :$newDoctorId")
            doctor.doctorId = newDoctorId
            Timber.d("Doctor to be added : $doctor")
            db.collection(FirebaseDB.DOCTORS).document("$newDoctorId").set(doctor).await()
            emit(true)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewDegree(degree: Degree): Flow<Int> {
        return flow {
            val lastAddedDegree =
                db.collection(FirebaseDB.DEGREE).orderBy("degreeId", Query.Direction.DESCENDING)
                    .limit(1).get().await()
            Timber.d("Last added degree : ${lastAddedDegree.documents}")
            val lastDegree = lastAddedDegree.toObjects(Degree::class.java)
            Timber.d("Converted into degrees : $lastDegree")
            val newDegreeId = if (lastDegree.isEmpty()) 100 else lastDegree[0].degreeId!! + 1

            Timber.d("New Degree ID :$newDegreeId")
            degree.degreeId = newDegreeId
            Timber.d("Degree to be added : $degree")
            val response = db.collection(FirebaseDB.DEGREE).document("$newDegreeId").set(degree)
            if (response.isSuccessful) emit(newDegreeId)
            else emit(0)

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDoctorsAndPharmacy(
        areaId: Int
    ): Flow<List<Any>> {
        return flow {
            val rDoctors = if (areaId == 0) db.collection(FirebaseDB.DOCTORS).get().await()
            else db.collection(FirebaseDB.DOCTORS).whereEqualTo("areaId", areaId).get().await()

            val rPharmacies = if (areaId == 0) db.collection(FirebaseDB.PHARMACY).get().await()
            else db.collection(FirebaseDB.PHARMACY).whereEqualTo("areaId", areaId).get().await()

            val doctors = rDoctors.toObjects<Doctor>()
            val pharmacies = rPharmacies.toObjects<Pharmacy>()
            val areaPersons = mutableListOf<Any>()
            if (!doctors.isNullOrEmpty()) {
                areaPersons.addAll(doctors)
            }
            if (!pharmacies.isNullOrEmpty()) {
                areaPersons.addAll(pharmacies)
            }

            emit(areaPersons)
        }.flowOn(Dispatchers.IO)
    }

}