/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cat.covidcontact.tracker.providers

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.data.repositories.interaction.InteractionRepository
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.adduserdata.AddUserData
import cat.covidcontact.usecases.adduserdata.AddUserDataImpl
import cat.covidcontact.usecases.createContactNetwork.CreateContactNetwork
import cat.covidcontact.usecases.createContactNetwork.CreateContactNetworkImpl
import cat.covidcontact.usecases.deleteaccount.DeleteAccount
import cat.covidcontact.usecases.deleteaccount.DeleteAccountImpl
import cat.covidcontact.usecases.deletecontactnetwork.DeleteContactNetwork
import cat.covidcontact.usecases.deletecontactnetwork.DeleteContactNetworkImpl
import cat.covidcontact.usecases.enableUserAddition.EnableUserAddition
import cat.covidcontact.usecases.enableUserAddition.EnableUserAdditionImpl
import cat.covidcontact.usecases.exitcontactnetwork.ExitContactNetwork
import cat.covidcontact.usecases.exitcontactnetwork.ExitContactNetworkImpl
import cat.covidcontact.usecases.generateAccessCode.GenerateAccessCode
import cat.covidcontact.usecases.generateAccessCode.GenerateAccessCodeImpl
import cat.covidcontact.usecases.getContactNetworkByAccessCode.GetContactNetworkByAccessCode
import cat.covidcontact.usecases.getContactNetworkByAccessCode.GetContactNetworkByAccessCodeImpl
import cat.covidcontact.usecases.getuserdata.GetUserData
import cat.covidcontact.usecases.getuserdata.GetUserDataImpl
import cat.covidcontact.usecases.joinContactNetwork.JoinContactNetwork
import cat.covidcontact.usecases.joinContactNetwork.JoinContactNetworkImpl
import cat.covidcontact.usecases.makelogin.MakeLogIn
import cat.covidcontact.usecases.makelogin.MakeLogInImpl
import cat.covidcontact.usecases.makelogout.MakeLogOut
import cat.covidcontact.usecases.makelogout.MakeLogOutImpl
import cat.covidcontact.usecases.notifypositive.NotifyPositive
import cat.covidcontact.usecases.notifypositive.NotifyPositiveImpl
import cat.covidcontact.usecases.registerDevice.RegisterDevice
import cat.covidcontact.usecases.registerDevice.RegisterDeviceImpl
import cat.covidcontact.usecases.sendmessagingtoken.SendMessagingToken
import cat.covidcontact.usecases.sendmessagingtoken.SendMessagingTokenImpl
import cat.covidcontact.usecases.sendread.SendRead
import cat.covidcontact.usecases.sendread.SendReadImpl
import cat.covidcontact.usecases.signup.MakeSignUp
import cat.covidcontact.usecases.signup.MakeSignUpImpl
import cat.covidcontact.usecases.updateprofile.UpdateProfile
import cat.covidcontact.usecases.updateprofile.UpdateProfileImpl
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
        interactionRepository: InteractionRepository
    ): SendRead = SendReadImpl(interactionRepository)

    @Provides
    @Singleton
    fun provideSendMessagingToken(
        userRepository: UserRepository
    ): SendMessagingToken = SendMessagingTokenImpl(userRepository)

    @Provides
    @Singleton
    fun provideNotifyPositive(
        interactionRepository: InteractionRepository
    ): NotifyPositive = NotifyPositiveImpl(interactionRepository)

    @Provides
    @Singleton
    fun provideUpdateProfile(
        userRepository: UserRepository
    ): UpdateProfile = UpdateProfileImpl(userRepository)

    @Provides
    @Singleton
    fun provideExitContactNetwork(
        contactNetworkRepository: ContactNetworkRepository
    ): ExitContactNetwork = ExitContactNetworkImpl(contactNetworkRepository)

    @Provides
    @Singleton
    fun provideMakeLogOut(
        userRepository: UserRepository
    ): MakeLogOut = MakeLogOutImpl(userRepository)

    @Provides
    @Singleton
    fun provideDeleteAccount(
        userRepository: UserRepository
    ): DeleteAccount = DeleteAccountImpl(userRepository)

    @Provides
    @Singleton
    fun provideDeleteContactNetwork(
        contactNetworkRepository: ContactNetworkRepository
    ): DeleteContactNetwork = DeleteContactNetworkImpl(contactNetworkRepository)
}
