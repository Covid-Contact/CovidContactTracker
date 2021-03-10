package cat.covidcontact.tracker

abstract class ScreenState {
    object Loading : ScreenState()
    object NoInternet : ScreenState()
    object OtherError : ScreenState()
}
