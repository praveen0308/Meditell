package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.util.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AreaRepository @Inject constructor(val db:FirebaseFirestore) {

    suspend fun getAreas(): Flow<List<Area>> {
        return flow {
            val response = db.collection(FirebaseDB.AREAS).get()
                    .await()
            val areas = response.toObjects(Area::class.java)
            emit(areas)

        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewArea(area:Area): Flow<Boolean> {
        return flow {
            val lastAddedArea = db.collection(FirebaseDB.AREAS).orderBy("areaId",Query.Direction.DESCENDING).limit(1).get().await()
            val areas = lastAddedArea.toObjects(Area::class.java)
            val newAreaId = areas[0].areaId!!

            area.areaId = newAreaId+1
            val response = db.collection(FirebaseDB.AREAS).document("A${newAreaId+1}").set(area)
            if (response.isSuccessful) emit(true)
            else emit(false)
        }.flowOn(Dispatchers.IO)
    }
}