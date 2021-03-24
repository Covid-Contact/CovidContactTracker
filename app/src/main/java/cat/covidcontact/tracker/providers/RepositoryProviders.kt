package cat.covidcontact.tracker.providers

import cat.covidcontact.data.UserRepository
import cat.covidcontact.data.UserRepositoryImpl
import cat.covidcontact.data.services.HttpRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
class RepositoryProviders {

    @Provides
    @Singleton
    fun provideUserRepository(
        httpRequest: HttpRequest
    ): UserRepository = UserRepositoryImpl(httpRequest)

    @Provides
    @Singleton
    fun provideHttpRequest(): HttpRequest = HttpRequest()
}