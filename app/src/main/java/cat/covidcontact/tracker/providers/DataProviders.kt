package cat.covidcontact.tracker.providers

import cat.covidcontact.data.controllers.contactnetwork.ContactNetworkController
import cat.covidcontact.data.controllers.contactnetwork.ContactNetworkControllerImpl
import cat.covidcontact.data.controllers.interaction.InteractionController
import cat.covidcontact.data.controllers.interaction.InteractionControllerImpl
import cat.covidcontact.data.controllers.user.UserController
import cat.covidcontact.data.controllers.user.UserControllerImpl
import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepositoryImpl
import cat.covidcontact.data.repositories.interaction.InteractionRepository
import cat.covidcontact.data.repositories.interaction.InteractionRepositoryImpl
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.data.repositories.user.UserRepositoryImpl
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

    @Provides
    @Singleton
    fun provideContactNetworkRepository(
        contactNetworkController: ContactNetworkController
    ): ContactNetworkRepository = ContactNetworkRepositoryImpl(contactNetworkController)

    @Provides
    @Singleton
    fun provideContactNetworkController(): ContactNetworkController = ContactNetworkControllerImpl()

    @Provides
    fun provideInteractionRepository(
        interactionController: InteractionController
    ): InteractionRepository = InteractionRepositoryImpl(interactionController)

    @Provides
    @Singleton
    fun provideInteractionController(): InteractionController = InteractionControllerImpl()
}
