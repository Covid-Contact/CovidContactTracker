package cat.covidcontact.tracker.authactivity.signup

import cat.covidcontact.usecases.signup.MakeSignUp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SignUpProvider {

    @Provides
    @Singleton
    fun provideMakeSignUp(): MakeSignUp = Any() as MakeSignUp
}