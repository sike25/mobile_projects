package hu.ait.smartcart.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.smartcart.data.AppDatabase
import hu.ait.smartcart.data.ShopDAO
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideShopDao(appDatabase: AppDatabase): ShopDAO {
        return appDatabase.shopDao()
    }

    @Provides
    @Singleton
    fun provideShopAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
}