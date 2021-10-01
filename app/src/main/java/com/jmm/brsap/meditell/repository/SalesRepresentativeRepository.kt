package com.jmm.brsap.meditell.repository

import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.Doctor
import com.jmm.brsap.meditell.model.Pharmacy
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.FirebaseDB
import com.jmm.brsap.meditell.util.FirebaseDB.AREAS
import com.jmm.brsap.meditell.util.FirebaseDB.DOCTORS
import com.jmm.brsap.meditell.util.FirebaseDB.PHARMACY
import com.jmm.brsap.meditell.util.FirebaseDB.SALES_REPRESENTATIVES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import timber.log.Timber
import javax.inject.Inject

class SalesRepresentativeRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val userPreferences: UserPreferencesRepository
) {
    //    private val userId = userPreferences.userId.asLiveData()
    suspend fun addSchedule(userId: String, schedules: List<Schedule>): Flow<Boolean> {
        return flow {

            Timber.d(userId.toString())

            try {
                for (schedule in schedules) {
                    db.collection(SALES_REPRESENTATIVES).document(userId)
                        .collection("schedule").document(schedule.date!!).set(schedule)
                }
            } finally {
                emit(true)
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSchedule(userId: String): Flow<List<Schedule>> {
        return flow {
            val result = db.collection(SALES_REPRESENTATIVES).document(userId)
                .collection("schedule").get().await()
            val schedules = result.toObjects<Schedule>()

            for (schedule in schedules) {
                for (areaId in schedule.areaVisits!!) {
                    val area =
                        db.collection(FirebaseDB.AREAS).document(areaId.toString()).get().await()
                    val areaName = area["name"]
                    schedule.scheduleAreas.add(Pair(areaId, areaName.toString()))
                }
            }

            emit(schedules)

        }.flowOn(Dispatchers.Default)
    }

    suspend fun markAttendance(
        userId: String,
        scheduleDate: String,
        dateTime: String,
        action: String
    ): Flow<Boolean> {
        return flow {

            val scheduleRef = db.collection(SALES_REPRESENTATIVES).document(userId)
                .collection("schedule").document(scheduleDate)
            scheduleRef.update(action, dateTime).await()

            if (action=="checkIn") scheduleRef.update("dayStatus",1).await()
            else scheduleRef.update("dayStatus",2).await()

            emit(true)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCurrentDayStatus(
        userId: String,
        scheduleDate: String
    ): Flow<Int> {
        return flow {

            val currentDayScheduleRef = db.collection(SALES_REPRESENTATIVES).document(userId)
                .collection("schedule").document(scheduleDate)
            val currentDaySchedule = currentDayScheduleRef.get().await()
            Timber.d("current Day schedule : $currentDaySchedule")
            val schedule = currentDaySchedule.toObject<Schedule>()
            Timber.d("current schedule : $schedule")
            Timber.d("current day status : ${schedule!!.dayStatus}")
            emit(schedule.dayStatus)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCurrentDayAreas(
        userId: String,
        scheduleDate: String
    ): Flow<List<Area>> {
        return flow {
            val currentDaySchedule = db.collection(SALES_REPRESENTATIVES).document(userId)
                .collection("schedule").document(scheduleDate).get().await()

            val schedule = currentDaySchedule.toObject<Schedule>()
            val areas = mutableListOf<Area>()
            schedule?.let {
                for(areaId in schedule.areaVisits!!){
                    val area = db.collection(AREAS).document(areaId.toString()).get().await()
                    areas.add(area.toObject()!!)
                }
            }
            emit(areas)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCurrentAreaDoctorsAndPharmacy(
        areaId:Int
    ): Flow<List<Any>> {
        return flow {
            val rDoctors = db.collection(DOCTORS).whereEqualTo("areaId",areaId).get().await()
            val rPharmacies = db.collection(PHARMACY).whereEqualTo("areaId",areaId).get().await()
            val doctors =rDoctors.toObjects<Doctor>()
            val pharmacies =rPharmacies.toObjects<Pharmacy>()
            val areaPersons = mutableListOf<Any>()
            if (!doctors.isNullOrEmpty()){
                areaPersons.addAll(doctors)
            }
            if (!pharmacies.isNullOrEmpty()){
                areaPersons.addAll(pharmacies)
            }

            emit(areaPersons)
        }.flowOn(Dispatchers.IO)
    }



}