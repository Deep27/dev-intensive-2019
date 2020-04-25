package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import ru.skillbranch.devintensive.extensions.TimeUnit
import ru.skillbranch.devintensive.extensions.add
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.extensions.toUserView
import ru.skillbranch.devintensive.models.*
import java.util.*

class ExampleUnitTest {

    @Test
    fun testInstance() {
//        val user = User("1")
//        val user2 = User("2", "John", "Wick")
        val user3 = User("3", "John", "Cena", null, lastVisit = Date(), isOnline = true)

//        user.printMe()
//        user2.printMe()
        user3.printMe()
    }

    @Test
    fun testFactory() {
        val user1 = User.makeUser("John Cena")
        val user2 = user1.copy(id = "2")
        println("$user1\n$user2")
    }

    @Test
    fun testDecomposition() {
        val user = User.makeUser("John Wick")
        fun getUserInfo() = user
        val (id, firstName, lastName) = getUserInfo()
        println("$id, $firstName, $lastName")
        println(
            "${getUserInfo().component1()}," +
                    "${getUserInfo().component2()}," +
                    "${getUserInfo().component3()}"
        )
    }

    @Test
    fun testCopy() {
        val user = User.makeUser("John Wick")
        val user2 = user.copy()

        println("== ${user == user2}")
        println("=== ${user === user2}")
        println("hashCode == ${user.hashCode() == user2.hashCode()}")
    }

    @Test
    fun testExtensions() {
        val user = User.makeUser("John Cena")
        val user2 = user.copy(lastVisit = Date())
        val user3 = user.copy(lastVisit = Date().add(-2, TimeUnit.SECOND))
        val user4 = user.copy(lastName = "Wick", lastVisit = Date().add(2, TimeUnit.HOUR))

        println(
            """
            ${user.lastVisit?.format()}
            ${user2.lastVisit?.format()}
            ${user3.lastVisit?.format()}
            ${user4.lastVisit?.format()}
        """.trimIndent()
        )
    }

    @Test
    fun testDataMapping() {
        val user = User.makeUser("John Cena")
        println(user)
        val userView = user.toUserView()
        userView.printMe()
    }

    @Test
    fun testMessageFactory() {
        val user = User.makeUser("John Cena")
        val textMessage = BaseMessage.makeMessage(
            user, Chat("0"),
            payload = "any text message", type = "text"
        )
        val imageMessage = BaseMessage.makeMessage(
            user, Chat("0"),
            payload = "any image url", type = "image"
        )

        when (textMessage) {
            is TextMessage -> println("Text message")
            is ImageMessage -> println("Text message")
        }

        println(textMessage.formatMessage())
        println(imageMessage.formatMessage())
    }
}

