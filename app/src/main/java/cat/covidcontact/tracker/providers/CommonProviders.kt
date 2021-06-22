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

import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.work.WorkManager
import cat.covidcontact.tracker.MainActivity
import cat.covidcontact.tracker.common.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.common.fieldvalidator.FieldValidatorImpl
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.MessagesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonProviders {

    @Provides
    @Singleton
    fun provideFieldValidator(): FieldValidator = FieldValidatorImpl()

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideBluetoothAdapter(): BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    @Provides
    @Singleton
    fun provideMessageClient(
        mainActivity: MainActivity
    ): MessagesClient = Nearby.getMessagesClient(mainActivity)

    @Provides
    @Singleton
    fun provideMainActivity() = MainActivity.instance
}
