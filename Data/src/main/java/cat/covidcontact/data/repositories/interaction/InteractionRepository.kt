package cat.covidcontact.data.repositories.interaction

interface InteractionRepository {
    suspend fun registerRead(currentDeviceId: String, deviceIds: Set<String>, time: Long)
    suspend fun notifyPositive(email: String)
}
