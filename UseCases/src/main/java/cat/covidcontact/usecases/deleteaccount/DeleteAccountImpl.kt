package cat.covidcontact.usecases.deleteaccount

import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class DeleteAccountImpl @Inject constructor(
    private val userRepository: UserRepository
) : DeleteAccount {

    override suspend fun execute(request: DeleteAccount.Request) = runUseCase {
        userRepository.deleteAccount(request.email)
        DeleteAccount.Response()
    }
}
