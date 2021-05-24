package cat.covidcontact.usecases

suspend fun <O : UseCase.UseCaseResponse> runUseCase(
    method: suspend () -> O
): UseCaseResult<O> = try {
    val result = method()
    UseCaseResult.Success(result)
} catch (e: Exception) {
    e.printStackTrace()
    UseCaseResult.Error(e)
}
