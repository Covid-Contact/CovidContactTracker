package cat.covidcontact.model

import cat.covidcontact.model.user.User

class ContactNetwork(
    val name: String,
    val password: String? = null,
    val owner: User,
) {
    private val _memberList: MutableList<User> = mutableListOf()
    val memberList: List<User>
        get() = _memberList

    init {
        _memberList.add(owner)
    }
}
