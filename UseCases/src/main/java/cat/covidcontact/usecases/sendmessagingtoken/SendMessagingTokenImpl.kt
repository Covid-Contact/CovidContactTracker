package cat.covidcontact.usecases.sendmessagingtoken

import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class SendMessagingTokenImpl @Inject constructor(
    private val userRepository: UserRepository
) : SendMessagingToken {
    override suspend fun execute(request: SendMessagingToken.Request) = runUseCase {
        userRepository.sendMessagingToken(request.email)
        SendMessagingToken.Response()
    }
}
