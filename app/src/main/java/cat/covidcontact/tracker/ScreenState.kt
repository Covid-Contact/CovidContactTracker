package cat.covidcontact.tracker

sealed class ScreenState<T> {
    object Loading : ScreenState<Nothing>()
    class Render<T>(renderState: T) : ScreenState<T>()
}
