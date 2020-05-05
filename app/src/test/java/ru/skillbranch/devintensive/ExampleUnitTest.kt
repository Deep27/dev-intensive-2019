package ru.skillbranch.devintensive

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.skillbranch.devintensive.extensions.*
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

class ExampleUnitTest {

    @Test
    fun testFullNameParsing() {
        assertEquals("John" to "Cena", Utils.parseFullName("John Cena"));
        assertEquals(null to null, Utils.parseFullName(null));
        assertEquals(null to null, Utils.parseFullName(""));
        assertEquals(null to null, Utils.parseFullName(" "));
        assertEquals("John" to null, Utils.parseFullName("John"));
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
        assertEquals("zhenya Stereotipov", Utils.transliteration("женя Стереотипов"))
        assertEquals("Amazing_Petr", Utils.transliteration("Amazing Петр", "_"))
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

        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("более чем через год", Date().add(2, TimeUnits.YEAR).humanizeDiff())
    }

    @Test
    fun testTruncate() {
        assertEquals("Bender Bending R...", "Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate())
        assertEquals("Bender Bending...", "Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate(15))
        assertEquals("A", "A     ".truncate(3))
    }

    @Test
    fun testStripHtml() {
//        Реализуй extension позволяющий очистить
//        строку от html тегов и html escape
//        последовательностей ("& < > ' ""), а так же удалить
//        пустые символы (пробелы) между словами если их больше 1.
        assertEquals("Образовательное IT-сообщество Skill Branch", "<p class=\"title\">Образовательное IT-сообщество Skill Branch</p>".stripHtml())
        assertEquals("Образовательное IT-сообщество Skill Branch", "<p>Образовательное       IT-сообщество Skill Branch</p>".stripHtml())
    }
}

