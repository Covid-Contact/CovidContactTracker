package cat.covidcontact.usecases

import cat.covidcontact.data.CommonException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import kotlin.reflect.KClass

fun <T> isUseCaseSuccess() = instanceOf<T>(UseCaseResult.Success::class.java)

fun <T> isUseCaseError() = instanceOf<T>(UseCaseResult.Error::class.java)

fun <T> assertException(result: T, exceptionClass: KClass<*>) =
    assertThat((result as UseCaseResult.Error<*>).exception, instanceOf(exceptionClass.java))

suspend fun <T : UseCase<I, *>, I : UseCase.UseCaseRequest> assertUseCaseError(
    useCase: T,
    request: I,
    exceptionClass: KClass<*>
) {
    val result = useCase.execute(request)
    assertThat(result, isUseCaseError())
    assertException(result, exceptionClass)
}

suspend fun <T : UseCase<I, O>, I : UseCase.UseCaseRequest,
    O : UseCase.UseCaseResponse> assertUseCaseSuccess(
    useCase: T,
    request: I,
    onAssertResult: suspend (O) -> Unit = {}
) {
    val result = useCase.execute(request)
    assertThat(result, isUseCaseSuccess())
    onAssertResult((result as UseCaseResult.Success).result)
}

@ExperimentalCoroutinesApi
fun <T : UseCase<I, *>, I : UseCase.UseCaseRequest> runErrorTest(
    useCase: T,
    request: I,
    exceptionClass: KClass<*>,
    onBeforeTest: suspend () -> Unit = {}
) = runBlockingTest {
    onBeforeTest()
    assertUseCaseError(useCase, request, exceptionClass)
}

@ExperimentalCoroutinesApi
fun <T : UseCase<I, *>, I : UseCase.UseCaseRequest> runNoInternetTest(
    useCase: T,
    request: I,
    onBeforeTest: suspend () -> Unit = {}
) = runErrorTest(useCase, request, CommonException.NoInternetException::class, onBeforeTest)

@ExperimentalCoroutinesApi
fun <T : UseCase<I, *>, I : UseCase.UseCaseRequest> runOtherErrorTest(
    useCase: T,
    request: I,
    onBeforeTest: suspend () -> Unit = {}
) = runErrorTest(useCase, request, Exception::class, onBeforeTest)

@ExperimentalCoroutinesApi
fun <T : UseCase<I, O>, I : UseCase.UseCaseRequest, O : UseCase.UseCaseResponse> runSuccessTest(
    useCase: T,
    request: I,
    onBeforeTest: suspend () -> Unit = {},
    onAssertResult: suspend (O) -> Unit = {}
) = runBlockingTest {
    onBeforeTest()
    assertUseCaseSuccess(useCase, request, onAssertResult)
}

fun <T> isEqualTo(other: T) = `is`(other)
