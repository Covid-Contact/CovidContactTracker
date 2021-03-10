package cat.covidcontact.tracker.usecase

interface UseCase<I : UseCase.UseCaseRequest, O : UseCase.UseCaseResponse> {
    fun execute(request: I): UseCaseResult<O>

    interface UseCaseRequest
    interface UseCaseResponse
}
