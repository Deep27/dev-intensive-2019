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

    class Builder {
        var id: String = ""
        var firstName: String? = null
        var lastName: String? = null
        var avatar: String? = null
        var rating: Int = 0
        var respect: Int = 0
        var lastVisit: Date? = null
        var isOnline: Boolean = false

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun firstName(firstName: String?): Builder {
            this.firstName = firstName
            return this
        }

        fun lastName(lastName: String?): Builder {
            this.lastName = lastName
            return this
        }

        fun avatar(avatar: String?): Builder {
            this.avatar = avatar
            return this
        }

        fun rating(rating: Int): Builder {
            this.rating = rating
            return this
        }

        fun respect(respect: Int): Builder {
            this.respect = respect
            return this
        }

        fun lastVisit(lastVisit: Date?): Builder {
            this.lastVisit = lastVisit
            return this
        }

        fun isOnline(isOnline: Boolean): Builder {
            this.isOnline = isOnline
            return this
        }

        fun build() = User(
            id, firstName, lastName, avatar, rating, respect,
            lastVisit, isOnline
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