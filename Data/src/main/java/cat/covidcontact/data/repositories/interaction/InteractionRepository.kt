package cat.covidcontact.data.repositories.interaction

interface InteractionRepository {
    suspend fun registerRead(
        currentDeviceId: String,
        deviceIds: Set<String>,
        time: Long,
        lat: Double?,
        lon: Double?
    )

    suspend fun notifyPositive(email: String)
}
