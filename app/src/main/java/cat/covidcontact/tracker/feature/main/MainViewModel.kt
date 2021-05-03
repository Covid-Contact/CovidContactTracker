package cat.covidcontact.tracker.feature.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.Device
import cat.covidcontact.model.UserDevice
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.notify
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.tracker.messages.PublishCodeWorker
import cat.covidcontact.tracker.messages.SendCodeWorker
import cat.covidcontact.usecases.getuserdata.GetUserData
import cat.covidcontact.usecases.registerDevice.RegisterDevice
import cat.covidcontact.usecases.sendread.SendRead
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.google.android.gms.nearby.messages.MessagesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val messagesClient: MessagesClient,
    private val workManager: WorkManager,
    private val getUserData: GetUserData,
    private val registerDevice: RegisterDevice,
    private val sendRead: SendRead
) : BaseViewModel() {
    private val _userDevice = MutableLiveData<UserDevice>()
    val userDevice: LiveData<UserDevice>
        get() = _userDevice

    private val idList: MutableSet<String> = mutableSetOf()

    private val getUserDataHandler = UseCaseResultHandler<GetUserData.Response>(
        onSuccess = { MainState.UserInfoFound(it.user) },
        onFailure = {
            when (it) {
                is UserException.UserInfoNotFound -> MainState.UserInfoNotFound(it.email)
                else -> ScreenState.Nothing
            }
        }
    )

    private val registerDeviceHandler = UseCaseResultHandler<RegisterDevice.Response>(
        onSuccess = {
            _userDevice.value = it.userDevice
            PublishCodeWorker.deviceId = it.userDevice.device.id
            MainState.DeviceRegistered
        },
        onFailure = { ScreenState.Nothing }
    )

    private val sendReadHandler = UseCaseResultHandler<SendRead.Response>(
        onSuccess = {
            idList.clear()
            ScreenState.Nothing
        },
        onFailure = { ScreenState.Nothing }
    )

    fun onGetCurrentUser(email: String) {
        viewModelScope.launch {
            executeUseCase(getUserData, getUserDataHandler) {
                GetUserData.Request(email)
            }
        }
    }

    fun onRegisterDevice(user: User, device: Device) {
        viewModelScope.launch {
            executeUseCase(registerDevice, registerDeviceHandler) {
                RegisterDevice.Request(user, device)
            }
        }
    }

    fun onAddContactNetwork(contactNetwork: ContactNetwork) {
        _userDevice.value?.user?.addContactNetwork(contactNetwork)
        _userDevice.notify()
    }

    fun onLoadBluetoothInfo() {
        loadState(MainState.BluetoothInfo)
    }

    fun onConfigureMessageClient() {
        viewModelScope.launch {
            val isSubscribed = setUpMessageListener()
            if (isSubscribed) {
                setUpWorkManager()
            }
        }
    }

    private suspend fun setUpMessageListener(): Boolean {
        val messageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                val strMessage = String(message.content)
                Log.i("Test", "onFound: $strMessage")

                idList.add(strMessage)
            }
        }

        val task = messagesClient.subscribe(messageListener)
        task.await()

        task.exception?.printStackTrace()
        return task.isSuccessful
    }

    private fun setUpWorkManager() {
        SendCodeWorker.onSend = {
            viewModelScope.launch {
                executeUseCase(sendRead, sendReadHandler, isExecutingUseCaseStateLoad = false) {
                    SendRead.Request(idList, System.currentTimeMillis())
                }
            }
        }

        val publishWork = PeriodicWorkRequestBuilder<PublishCodeWorker>(PERIOD, TimeUnit.MINUTES)
            .build()
        val sendWork = PeriodicWorkRequestBuilder<SendCodeWorker>(2 * PERIOD, TimeUnit.MINUTES)
            .setInitialDelay(PERIOD, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PUBLISH_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            publishWork
        )

        workManager.enqueueUniquePeriodicWork(
            SEND_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            sendWork
        )
    }

    fun requireUserDevice(): UserDevice = userDevice.value ?: throw Exception("UserDevice not set")

    companion object {
        private const val PERIOD = 16L
        private const val PUBLISH_WORK_NAME = "publish"
        private const val SEND_WORK_NAME = "send"
    }
}
