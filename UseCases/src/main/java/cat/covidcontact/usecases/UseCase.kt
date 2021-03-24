package cat.covidcontact.usecases

interface UseCase<I : UseCase.UseCaseRequest, O : UseCase.UseCaseResponse> {
    suspend fun execute(request: I): UseCaseResult<O>

    interface UseCaseRequest
    interface UseCaseResponse
}
