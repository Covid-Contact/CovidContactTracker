package cat.covidcontact.model.user

import cat.covidcontact.model.post.PostUser

class User(
    val username: String,
    val email: String = "",
    val gender: Gender = Gender.Other,
    val birthDate: Long = 0,
    var city: String? = null,
    var studies: String? = null,
    var occupation: Occupation? = null,
    var marriage: Marriage? = null,
    var children: Int? = null,
    var hasBeenPositive: Boolean? = null,
    var isVaccinated: Boolean? = null
) {

    fun createPost(): PostUser {
        return PostUser(
            email,
            username,
            gender,
            birthDate,
            city,
            studies,
            occupation,
            marriage,
            children,
            hasBeenPositive,
            isVaccinated
        )
    }

    companion object {
        @JvmStatic
        fun fromPost(postUser: PostUser): User {
            return with(postUser) {
                User(
                    username,
                    email,
                    gender,
                    birthDate,
                    city,
                    studies,
                    occupation,
                    marriage,
                    children,
                    hasBeenPositive,
                    isVaccinated
                )
            }
        }
    }
}