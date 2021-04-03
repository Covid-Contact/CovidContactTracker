package cat.covidcontact.tracker.providers

import cat.covidcontact.data.user.UserRepository
import cat.covidcontact.usecases.login.MakeLogIn
import cat.covidcontact.usecases.login.MakeLogInImpl
import cat.covidcontact.usecases.signup.MakeSignUp
import cat.covidcontact.usecases.signup.MakeSignUpImpl
import cat.covidcontact.usecases.userinfo.GetUserData
import cat.covidcontact.usecases.userinfo.GetUserDataImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [DataProviders::class])
@InstallIn(SingletonComponent::class)
class UseCaseProviders {

    @Provides
    @Singleton
    fun provideMakeLogIn(
        userRepository: UserRepository
    ): MakeLogIn = MakeLogInImpl(userRepository)

    @Provides
    @Singleton
    fun provideMakeSignUp(
        userRepository: UserRepository
    ): MakeSignUp = MakeSignUpImpl(userRepository)

    @Provides
    @Singleton
    fun provideGetUserInfo(
        userRepository: UserRepository,
    ): GetUserData = GetUserDataImpl(userRepository)
}