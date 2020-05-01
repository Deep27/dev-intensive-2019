package ru.skillbranch.devintensive

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BenderUiTest {

    @Rule
    @JvmField
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testSoftErrorsWorkflow() {
        commitAnswer("bender")
        assertAnswerIsIncorrect("Имя должно начинаться с заглавной буквы")

        commitAnswer("Bender")
        assertAnswerIsCorrect()

        commitAnswer("Сгибальщик")
        assertAnswerIsIncorrect("")
    }

    private fun commitAnswer(answer: String) = onView(withId(R.id.et_message))
        .perform(typeText(answer))
        .perform(pressImeActionButton())

    private fun assertAnswerIsCorrect() = onView(withId(R.id.tv_text))
        .check(ViewAssertions.matches(ViewMatchers.withText("Отлично - ты справился")))

    private fun assertAnswerIsIncorrect(errorMessage: String) = onView(withId(R.id.tv_text))
        .check(ViewAssertions.matches(ViewMatchers.withText(errorMessage)))
}