package cat.covidcontact.data.controllers.interaction

import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.post.PostRead

abstract class InteractionController : CovidContactBaseController() {
    override val url = "${super.url}/interaction"

    abstract suspend fun registerRead(postRead: PostRead): ServerResponse
}
