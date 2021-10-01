package com.jmm.brsap.meditell.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.jmm.brsap.meditell.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

    @Singleton
    @Provides
    fun provideAreaRepository(db: FirebaseFirestore): AreaRepository {
        return AreaRepository(db)
    }

    @Singleton
    @Provides
    fun provideSalesRepresentativeRepo(db: FirebaseFirestore,userPreferencesRepository: UserPreferencesRepository): SalesRepresentativeRepository {
        return SalesRepresentativeRepository(db,userPreferencesRepository)
    }

    @Singleton
    @Provides
    fun provideInteractionRepo(db: FirebaseFirestore): InteractionRepository {
        return InteractionRepository(db)
    }


}
