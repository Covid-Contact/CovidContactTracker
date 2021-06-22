package cat.covidcontact.usecases.updateprofile

import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.model.user.Marriage
import cat.covidcontact.model.user.Occupation
import cat.covidcontact.usecases.runUseCase

class UpdateProfileImpl(
    private val userRepository: UserRepository
) : UpdateProfile {

    override suspend fun execute(request: UpdateProfile.Request) = runUseCase {
        userRepository.updateUserData(
            request.user.email,
            request.city,
            request.studies,
            request.occupation,
            request.marriage,
            request.children,
            request.positive,
            request.vaccinated,
        )

        request.user.apply {
            city = request.city
            studies = request.studies
            occupation = Occupation.valueOf(request.occupation)
            marriage = Marriage.valueOf(request.marriage)
            children = request.children
            hasBeenPositive = request.positive
            isVaccinated = request.vaccinated
        }
        UpdateProfile.Response()
    }
}
