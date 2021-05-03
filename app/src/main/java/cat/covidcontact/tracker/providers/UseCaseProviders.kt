package cat.covidcontact.tracker.providers

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.adduserdata.AddUserData
import cat.covidcontact.usecases.adduserdata.AddUserDataImpl
import cat.covidcontact.usecases.createContactNetwork.CreateContactNetwork
import cat.covidcontact.usecases.createContactNetwork.CreateContactNetworkImpl
import cat.covidcontact.usecases.enableUserAddition.EnableUserAddition
import cat.covidcontact.usecases.enableUserAddition.EnableUserAdditionImpl
import cat.covidcontact.usecases.generateAccessCode.GenerateAccessCode
import cat.covidcontact.usecases.generateAccessCode.GenerateAccessCodeImpl
import cat.covidcontact.usecases.getContactNetworkByAccessCode.GetContactNetworkByAccessCode
import cat.covidcontact.usecases.getContactNetworkByAccessCode.GetContactNetworkByAccessCodeImpl
import cat.covidcontact.usecases.getuserdata.GetUserData
import cat.covidcontact.usecases.getuserdata.GetUserDataImpl
import cat.covidcontact.usecases.joinContactNetwork.JoinContactNetwork
import cat.covidcontact.usecases.joinContactNetwork.JoinContactNetworkImpl
import cat.covidcontact.usecases.login.MakeLogIn
import cat.covidcontact.usecases.login.MakeLogInImpl
import cat.covidcontact.usecases.registerDevice.RegisterDevice
import cat.covidcontact.usecases.registerDevice.RegisterDeviceImpl
import cat.covidcontact.usecases.sendread.SendRead
import cat.covidcontact.usecases.sendread.SendReadImpl
import cat.covidcontact.usecases.signup.MakeSignUp
import cat.covidcontact.usecases.signup.MakeSignUpImpl
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
    fun provideGetUserData(
        userRepository: UserRepository,
        contactNetworkRepository: ContactNetworkRepository
    ): GetUserData = GetUserDataImpl(userRepository, contactNetworkRepository)

    @Provides
    @Singleton
    fun provideAddUserInfo(
        userRepository: UserRepository
    ): AddUserData = AddUserDataImpl(userRepository)

    @Provides
    @Singleton
    fun provideRegisterDevice(
        userRepository: UserRepository
    ): RegisterDevice = RegisterDeviceImpl(userRepository)

    @Provides
    @Singleton
    fun provideCreateContactNetwork(
        contactNetworkRepository: ContactNetworkRepository
    ): CreateContactNetwork = CreateContactNetworkImpl(contactNetworkRepository)

    @Provides
    @Singleton
    fun provideEnableUserAddition(
        contactNetworkRepository: ContactNetworkRepository
    ): EnableUserAddition = EnableUserAdditionImpl(contactNetworkRepository)

    @Provides
    @Singleton
    fun provideGenerateAccessCode(
        contactNetworkRepository: ContactNetworkRepository
    ): GenerateAccessCode = GenerateAccessCodeImpl(contactNetworkRepository)

    @Provides
    @Singleton
    fun provideGetContactNetworkByAccessCode(
        contactNetworkRepository: ContactNetworkRepository
    ): GetContactNetworkByAccessCode = GetContactNetworkByAccessCodeImpl(contactNetworkRepository)

    @Provides
    @Singleton
    fun provideJoinContactNetworkByAccessCode(
        contactNetworkRepository: ContactNetworkRepository
    ): JoinContactNetwork = JoinContactNetworkImpl(contactNetworkRepository)

    @Provides
    @Singleton
    fun provideSendRead(
        contactNetworkRepository: ContactNetworkRepository
    ): SendRead = SendReadImpl(contactNetworkRepository)
}
