package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jmm.brsap.meditell.model.Area
import com.jmm.brsap.meditell.model.City
import com.jmm.brsap.meditell.util.FirebaseDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AreaRepository @Inject constructor(private val db:FirebaseFirestore) {

    suspend fun getAreas(cityId:Int=0): Flow<List<Area>> {
        return flow {
            val response = if (cityId==0){
                db.collection(FirebaseDB.AREAS).get().await()
            }else{
                db.collection(FirebaseDB.AREAS).whereEqualTo("cityId",cityId).get().await()
            }
            val areas = response.toObjects(Area::class.java)
            emit(areas)


        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCities(): Flow<List<City>> {
        return flow {
            val response = db.collection(FirebaseDB.CITIES).get()
                .await()
            val cities = response.toObjects(City::class.java)
            emit(cities)

        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewArea(area:Area): Flow<Boolean> {
        return flow {
            val lastAddedArea = db.collection(FirebaseDB.AREAS).orderBy("areaId",Query.Direction.DESCENDING).limit(1).get().await()
            Timber.d("Last added area : ${lastAddedArea.documents}")
            val areas = lastAddedArea.toObjects(Area::class.java)
            Timber.d("Converted into areas : $areas")
            val newAreaId = if (areas.isEmpty()) 10000 else areas[0].areaId!!+1

            Timber.d("New Area ID :$newAreaId")
            area.areaId = newAreaId
            Timber.d("Area to be added : $area,in city ${area.cityId}")
            val response = db.collection(FirebaseDB.AREAS).document("$newAreaId").set(area)
            if (response.isSuccessful) emit(true)
            else emit(false)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewCity(city:City): Flow<Int> {
        return flow {
            val lastAddedCity= db.collection(FirebaseDB.CITIES).orderBy("cityId",Query.Direction.DESCENDING).limit(1).get().await()
            Timber.d("Last added city : ${lastAddedCity.documents}")
            val cities = lastAddedCity.toObjects(City::class.java)
            Timber.d("Converted into cities : $cities")
            val newCityId = if (cities.isEmpty()) 1000 else cities[0].cityId!!+1

            Timber.d("New City ID :$newCityId")
            city.cityId = newCityId
            Timber.d("City to be added : $city")
            val response = db.collection(FirebaseDB.CITIES).document("$newCityId").set(city).await()
            emit(newCityId)

        }.flowOn(Dispatchers.IO)
    }
}







