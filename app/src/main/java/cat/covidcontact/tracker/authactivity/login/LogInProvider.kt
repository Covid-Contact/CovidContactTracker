package cat.covidcontact.tracker.authactivity.login

import cat.covidcontact.tracker.authactivity.login.usecases.MakeLogIn
import cat.covidcontact.tracker.util.FieldValidator
import cat.covidcontact.tracker.util.FieldValidatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LogInProvider {

    @Provides
    @Singleton
    fun provideFieldValidator(): FieldValidator = FieldValidatorImpl()

    @Provides
    @Singleton
    fun provideMakeLogIn(): MakeLogIn = Any() as MakeLogIn
}