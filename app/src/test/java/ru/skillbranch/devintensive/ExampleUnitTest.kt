package ru.skillbranch.devintensive

import org.junit.Assert.*
import org.junit.Test
import ru.skillbranch.devintensive.extensions.*
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
        assertEquals("R", Utils.toInitials("", "roman"))
        assertEquals("R", Utils.toInitials(null, "roman"))
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
    fun testPlural() {
        assertEquals("1 секунду", TimeUnits.SECOND.plural(1))
        assertEquals("2 секунды", TimeUnits.SECOND.plural(2))
        assertEquals("4 секунды", TimeUnits.SECOND.plural(4))
        assertEquals("5 секунд", TimeUnits.SECOND.plural(5))
        assertEquals("20 секунд", TimeUnits.SECOND.plural(20))
        assertEquals("21 секунду", TimeUnits.SECOND.plural(21))
        assertEquals("22 секунды", TimeUnits.SECOND.plural(22))
        assertEquals("24 секунды", TimeUnits.SECOND.plural(24))
        assertEquals("25 секунд", TimeUnits.SECOND.plural(25))
    }

    @Test
    fun testHumanizeDiff() {
        assertEquals("только что", Date().humanizeDiff())
        assertEquals("несколько секунд назад", Date().add(-20, TimeUnits.SECOND).humanizeDiff())
        assertEquals("минуту назад", Date().add(-60, TimeUnits.SECOND).humanizeDiff())
        assertEquals("5 минут назад", Date().add(-5, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("час назад", Date().add(-46, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("11 часов назад", Date().add(-11, TimeUnits.HOUR).humanizeDiff())
        assertEquals("день назад", Date().add(-25, TimeUnits.HOUR).humanizeDiff())
        assertEquals("101 день назад", Date().add(-101, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-2, TimeUnits.YEAR).humanizeDiff())

//        TODO
//        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
//        assertEquals("более чем через год", Date().add(2, TimeUnits.YEAR).humanizeDiff())
    }

    @Test
    fun testUserBuilder() {
        val user = User.makeUser("John Cena")
        val user2 = User.Builder()
            .id("0")
            .firstName("John")
            .lastName("Cena")
            .build()
        assertEquals(user, user2)
    }

    @Test
    fun testTruncate() {
        assertEquals("Bender Bending R...", "Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate())
        assertEquals("Bender Bending...", "Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate(15))
        assertEquals("A", "A     ".truncate(3))
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

