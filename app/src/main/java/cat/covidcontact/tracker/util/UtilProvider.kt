package cat.covidcontact.tracker.util

import cat.covidcontact.tracker.util.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.util.fieldvalidator.FieldValidatorImpl
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