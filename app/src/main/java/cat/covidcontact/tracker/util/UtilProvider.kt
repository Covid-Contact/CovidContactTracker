package cat.covidcontact.tracker.util

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilProvider {

    @Provides
    @Singleton
    fun provideFieldValidator(): FieldValidator = FieldValidatorImpl()
}