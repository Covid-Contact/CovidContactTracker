package cat.covidcontact.tracker.providers

import cat.covidcontact.data.controllers.UserController
import cat.covidcontact.data.controllers.UserControllerImpl
import cat.covidcontact.data.user.UserRepository
import cat.covidcontact.data.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataProviders {

    @Provides
    @Singleton
    fun provideUserRepository(
        userController: UserController
    ): UserRepository = UserRepositoryImpl(userController)

    @Provides
    @Singleton
    fun provideUserController(): UserController = UserControllerImpl()
}