package com.jmm.brsap.meditell.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.jmm.brsap.meditell.model.SalesRepresentative
import com.jmm.brsap.meditell.util.FirebaseDB.SALES_REPRESENTATIVES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber


import javax.inject.Inject

class AuthRepository @Inject constructor(
    val db: FirebaseFirestore
) {
    suspend fun checkUserExist(userName: String): Flow<Boolean> {
        return flow {
            val response =
                db.collection(SALES_REPRESENTATIVES).whereEqualTo("userName", userName).get()
                    .await()
            if (response.size() > 0) emit(true)
            else emit(false)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun doLogin(userName: String, password: String): Flow<SalesRepresentative?> {
        return flow {
            val response = db.collection(SALES_REPRESENTATIVES).whereEqualTo("userName", userName)
                .whereEqualTo("password", password).get().await()

            if (response.size() > 0) {
                val representativeId = response.documents[0].id
                Timber.d("RepresentativeId : $representativeId")
                val salesRepresentative = response.documents[0].toObject<SalesRepresentative>()
                salesRepresentative!!.userId = representativeId
                emit(salesRepresentative)

            } else emit(null)
        }.flowOn(Dispatchers.IO)
    }
}