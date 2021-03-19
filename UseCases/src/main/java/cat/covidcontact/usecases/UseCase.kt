package cat.covidcontact.usecases

interface UseCase<I : UseCase.UseCaseRequest, O : UseCase.UseCaseResponse> {
    fun execute(request: I): UseCaseResult<O>

    interface UseCaseRequest
    interface UseCaseResponse
}
