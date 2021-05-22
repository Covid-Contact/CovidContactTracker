package cat.covidcontact.data.controllers.interaction

import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.post.PostRead

class InteractionControllerImpl : InteractionController() {
    override suspend fun registerRead(postRead: PostRead): ServerResponse {
        return post("$url/read", postRead)
    }
}
