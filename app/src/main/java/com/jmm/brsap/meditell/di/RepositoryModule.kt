package com.jmm.brsap.meditell.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jmm.brsap.meditell.repository.AuthRepository
import com.jmm.brsap.meditell.repository.UserPreferencesRepository
import com.jmm.brsap.meditell.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(db:FirebaseFirestore):AuthRepository{
        return AuthRepository(db)
    }

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(@ApplicationContext context: Context):UserPreferencesRepository{
        return UserPreferencesRepository(context)
    }
}