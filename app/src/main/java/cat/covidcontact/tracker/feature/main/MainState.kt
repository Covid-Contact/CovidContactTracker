package cat.covidcontact.tracker.feature.main

import cat.covidcontact.model.User
import cat.covidcontact.tracker.ScreenState

sealed class MainState : ScreenState() {
    class UserInfoNotFound(val email: String) : MainState()
    class UserInfoFound(val user: User) : MainState()
}
