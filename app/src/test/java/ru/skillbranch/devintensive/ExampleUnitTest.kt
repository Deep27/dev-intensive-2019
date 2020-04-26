package ru.skillbranch.devintensive

import org.junit.Assert.*
import org.junit.Test
import ru.skillbranch.devintensive.extensions.TimeUnits
import ru.skillbranch.devintensive.extensions.add
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.extensions.toUserView
import ru.skillbranch.devintensive.models.*
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

class ExampleUnitTest {

    @Test
    fun testFactory() {
        val user1 = User.makeUser("John Cena")
        val user2 = user1.copy(id = "2")
        assertEquals(user1.firstName, user2.firstName)
        assertEquals(user1.lastName, user2.lastName)
        assertEquals(user1.introBit, user2.introBit)
        assertEquals(user1.lastVisit, user2.lastVisit)
        assertEquals(user1.avatar, user2.avatar)
        assertNotEquals(user1.id, user2.id)
    }

    @Test
    fun testFullNameParsing() {
        assertEquals("John" to "Cena", Utils.parseFullName("John Cena"));
        assertEquals(null to null, Utils.parseFullName(null));
        assertEquals(null to null, Utils.parseFullName(""));
        assertEquals(null to null, Utils.parseFullName(" "));
        assertEquals("John" to null, Utils.parseFullName("John"));
    }

    @Test
    fun testDecomposition() {
        val user = User.makeUser("John Wick")
        fun getUserInfo() = user
        val (id, firstName, lastName) = getUserInfo()
        assertEquals(user.id, id)
        assertEquals(user.firstName, firstName)
        assertEquals(user.component3(), lastName)
    }

    @Test
    fun testCopy() {
        val user = User.makeUser("John Wick")
        val user2 = user.copy()
        assertEquals(user, user2)
        assertFalse(user === user2)
        assertEquals(user.hashCode(), user2.hashCode())
    }

    @Test
    fun testExtensions() {
        val user = User.makeUser("John Cena")
        val user2 = user.copy(lastVisit = Date())
        val user3 = user.copy(lastVisit = Date().add(-2, TimeUnits.SECOND))
        val user4 = user.copy(lastName = "Wick", lastVisit = Date().add(2, TimeUnits.HOUR))

        val regex = "^\\d{2}:\\d{2}:\\d{2} \\d{2}\\.\\d{2}\\.\\d{2}$".toRegex()

        assertNull(user.lastVisit?.format())
        assertTrue(regex.containsMatchIn(user2.lastVisit!!.format()))
        assertTrue(regex.containsMatchIn(user3.lastVisit!!.format()))
        assertTrue(regex.containsMatchIn(user4.lastVisit!!.format()))
    }

    @Test
    fun testToInitials() {
        assertEquals("JC", Utils.toInitials("john", "cena"))
        assertEquals("J", Utils.toInitials("John", null))
        assertEquals(null, Utils.toInitials(null, null))
        assertEquals(null, Utils.toInitials(" ", " "))
        assertEquals(null, Utils.toInitials("", ""))
    }

    @Test
    fun testTransliteration() {
        assertEquals("Zhenya Stereotipov", Utils.transliteration("Женя Стереотипов"))
        assertEquals("Amazing_Petr", Utils.transliteration("Amazing Петр", "_"))
    }

    @Test
    fun testDataMapping() {
        val user = User.makeUser("Женя Стереотипов")
        val userView = user.toUserView()
        assertEquals("ЖС", userView.initials)
        assertEquals("Zhenya Stereotipov", userView.nickname)
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

