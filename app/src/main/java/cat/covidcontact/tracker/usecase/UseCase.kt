package cat.covidcontact.tracker.usecase

interface UseCase<I, O> {
    fun execute(parameters: I): UseCaseResult<O>
}
