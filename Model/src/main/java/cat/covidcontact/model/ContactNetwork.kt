package cat.covidcontact.model

import cat.covidcontact.model.post.PostContactNetwork
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
        addUser(owner)
    }

    fun addUser(user: User) {
        if (!memberList.contains(user)) {
            _memberList.add(user)
            user.addContactNetwork(this)
        }
    }

    fun addUsers(userList: List<User>) {
        userList.forEach { addUser(it) }
    }

    fun createPost(): PostContactNetwork {
        val membersUsername = memberList.map { it.username }
        return PostContactNetwork(
            name = name,
            password = password,
            ownerEmail = owner.email,
            ownerUsername = owner.username,
            membersUsername = membersUsername
        )
    }

    override fun toString(): String {
        return "ContactNetwork(name='$name', " +
            "password=$password, " +
            "owner=$owner)"
    }

    companion object {
        @JvmStatic
        fun fromPost(postContactNetwork: PostContactNetwork): ContactNetwork {
            return with(postContactNetwork) {
                ContactNetwork(name, password, User(ownerUsername)).also { contactNetwork ->
                    val users = membersUsername.map { User(username = it) }
                    contactNetwork.addUsers(users)
                }
            }
        }
    }
}
