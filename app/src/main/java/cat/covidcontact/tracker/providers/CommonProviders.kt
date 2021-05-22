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
