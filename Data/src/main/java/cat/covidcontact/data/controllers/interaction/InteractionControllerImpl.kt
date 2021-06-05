package cat.covidcontact.data.controllers.interaction

import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.post.PostRead

class InteractionControllerImpl : InteractionController() {
    override suspend fun registerRead(postRead: PostRead): ServerResponse {
        return post("$url/read", postRead)
    }

    override suspend fun notifyPositive(email: String): ServerResponse {
        return put("$url/positive", listOf("email" to email))
    }
}
