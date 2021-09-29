package com.jmm.brsap.meditell.repository

import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class SalesRepresentativeRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val userPreferences: UserPreferencesRepository
) {
    private val userId = userPreferences.userId.asLiveData()
    suspend fun addSchedule(schedules: List<Schedule>): Flow<Boolean> {
        return flow {
            Timber.d(userId.value.toString())

            try {
                for (schedule in schedules) {
                    db.collection(FirebaseDB.SALES_REPRESENTATIVES).document("MTS10000")
                        .collection("schedule").document(schedule.date!!).set(schedule)
                }
            } finally {
                emit(true)
            }

        }.flowOn(Dispatchers.IO)
    }
}