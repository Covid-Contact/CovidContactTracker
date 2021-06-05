package cat.covidcontact.usecases.updateprofile

import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.runUseCase

class UpdateProfileImpl(
    private val userRepository: UserRepository
) : UpdateProfile {

    override suspend fun execute(request: UpdateProfile.Request) = runUseCase {
        userRepository.updateUserData(
            request.email,
            request.city,
            request.studies,
            request.occupation,
            request.marriage,
            request.children,
            request.positive,
            request.vaccinated,
        )
        UpdateProfile.Response()
    }
}
