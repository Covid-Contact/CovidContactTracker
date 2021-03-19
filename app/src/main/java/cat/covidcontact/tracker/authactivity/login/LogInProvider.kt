package cat.covidcontact.tracker.authactivity.login

import cat.covidcontact.usecases.login.MakeLogIn
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
    fun provideMakeLogIn(): MakeLogIn = Any() as MakeLogIn
}
