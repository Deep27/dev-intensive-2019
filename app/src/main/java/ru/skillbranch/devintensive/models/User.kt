package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String, var firstName: String?, var lastName: String?,
    var avatar: String?, var rating: Int = 0, var respect: Int = 0,
    val lastVisit: Date? = null, var isOnline: Boolean = false
) {
    var introBit: String

    constructor(id: String, firstName: String?, lastName: String?) :
            this(id, firstName, lastName, avatar = null)

    constructor(id: String) : this(id, "John", "Doe")

    init {
        introBit = getIntro()
        println(
            "It's alive!\n" +
                    if (lastName === "Doe") "Firstname: $firstName, Lastname: $lastName" else "And his name is $firstName $lastName!!!"
        )
    }

    fun printMe() = println(
        """
            id: $id,
            firstName: $firstName,
            lastName: $lastName,
            avatar: $avatar,
            rating: $rating,
            respect: $respect,
            lastVisit: $lastVisit,
            lastVisit: $lastVisit
            """.trimIndent()
    )

    private fun getIntro() = """
        tu tu tu tuuu,
        tu tu tu tuuuuuu!
        
        tu tu tu tuuu,
        tu tu tu tuuuuuu!
        
        tu tu tu tuuu,
        tu tu tu tuuuuuu!
    """.trimIndent()

    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?): User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(
                id = "$lastId",
                firstName = firstName,
                lastName = lastName
            )
        }
    }
}